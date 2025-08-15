package com.ruoyi.gpt.controller.stream;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbQARecordMapper;
import com.ruoyi.gpt.mapper.UserKbHistoryMapper;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.domain.KbQARecordManagerRel;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.domain.UserKbHistory;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQAChatDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.user_kb_history.UserKbHistoryInsertDto;
import com.ruoyi.gpt.service.IKbQARecordManagerRelService;
import com.ruoyi.gpt.service.IKbQARecordService;
import com.ruoyi.gpt.service.IModelApiService;
import com.ruoyi.gpt.service.impl.KbQAChatServiceImpl;
import com.ruoyi.gpt.service.impl.util.TokenServiceWs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@ServerEndpoint("/websocket/kb/qaChat/{sessionId}")
public class KBWebSocketEndpoint {

    public static KbQAChatServiceImpl kbQAChatService;

    @Autowired
    private void setKbQAChatServiceImpl(KbQAChatServiceImpl kbQAChatService) {
        KBWebSocketEndpoint.kbQAChatService = kbQAChatService;
    }

    public static KbQARecordMapper kbQARecordMapper;

    @Autowired
    private void setKbQARecordMapper(KbQARecordMapper kbQARecordMapper) {
        KBWebSocketEndpoint.kbQARecordMapper = kbQARecordMapper;
    }

    public static TokenServiceWs tokenServiceWs;

    @Autowired
    public void setTokenServiceWs(TokenServiceWs tokenServiceWs) {
        KBWebSocketEndpoint.tokenServiceWs = tokenServiceWs;
    }

    public static UserKbHistoryMapper userKbHistoryMapper;

    @Autowired
    private void setUserKbHistoryMapper(UserKbHistoryMapper userKbHistoryMapper) {
        KBWebSocketEndpoint.userKbHistoryMapper = userKbHistoryMapper;
    }

    public static SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private void setSnowflakeGenerator(SnowflakeGenerator snowflakeGenerator) {
        KBWebSocketEndpoint.snowflakeGenerator = snowflakeGenerator;
    }

    public static IKbQARecordManagerRelService iKbQARecordManagerRelService;

    @Autowired
    private void setIKbQARecordManagerRelService(IKbQARecordManagerRelService iKbQARecordManagerRelService) {
        KBWebSocketEndpoint.iKbQARecordManagerRelService = iKbQARecordManagerRelService;
    }

    public static IModelApiService iModelApiService;

    @Autowired
    private void setIModelApiService(IModelApiService iModelApiService) {
        KBWebSocketEndpoint.iModelApiService = iModelApiService;
    }

    public static IKbQARecordService iKbQARecordService;

    @Autowired
    private void setIKbQARecordService(IKbQARecordService iKbQARecordService) {
        KBWebSocketEndpoint.iKbQARecordService = iKbQARecordService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        System.out.println("前端连接建立: " + sessionId);
        SessionManager.saveSession(sessionId, session);
        String token = getHeader(session, "wsToken");
        LoginUser loginUserWs = tokenServiceWs.getLoginUserWs(token);
        SessionManager.saveSessionLoginUser(sessionId, loginUserWs);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject msgJSON = new JSONObject(message);
            KbQAChatDto kbQAChatDto = JSONUtil.toBean(msgJSON, KbQAChatDto.class);
            // 记录问答历史
            Long qaRecordId = insertQaHistory(kbQAChatDto);

            ModelChatApiDto modelChatApiDto = BeanConvertUtil.conver(kbQAChatDto, ModelChatApiDto.class);
            ModelResources modelResources2 = iModelApiService.calTpmMax(modelChatApiDto);
            JSONObject entries = new JSONObject();
            entries.set("modelId", modelResources2.getId());
            entries.set("modelParams", modelResources2.getParameter());
            kbQAChatDto.setModelInfo(entries);

            getAnswer(kbQAChatDto, qaRecordId);
        } catch (Exception e) {
            handleError(session, "消息处理异常", e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("前端连接关闭: " + session.getId());
        SessionManager.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        handleError(session, "WebSocket错误", throwable);
    }

    public Long insertQaHistory(KbQAChatDto kbQAChatDto) {
        // 新增问答记录
        Long qaRecordId = addQaRecord(kbQAChatDto.getSessionId(), kbQAChatDto.getQuestion(), kbQAChatDto.getChatTitle(),
                kbQAChatDto.getModelType(), Long.valueOf(kbQAChatDto.getModelTypeId()));
        // 新增用户问答历史
        addUserKbHistory(kbQAChatDto.getSessionId(), kbQAChatDto.getChatTitle());
        log.info("新增用户问答历史");
        // 新增问答记录知识库关联关系
        if (kbQAChatDto.getKbManagerIds() != null && kbQAChatDto.getKbManagerIds().length > 0) {
            addQARecordManagerRel(kbQAChatDto.getSessionId(), qaRecordId, kbQAChatDto.getKbManagerIds());
            log.info("新增问答记录知识库关联关系");
        }
        return qaRecordId;
    }

    public void getAnswer(KbQAChatDto kbQAChatDto, Long qaRecordId) {
        JSONObject params = JSONUtil.parseObj(kbQAChatDto);
        try {
            URL apiUrl = new URL(DictUtils.getDictValue("python", "url"));
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(10);
            connection.getOutputStream().write(params.toString().getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    SessionManager.getSession(kbQAChatDto.getSessionId()).getAsyncRemote().sendText(line);

                    JSONObject lineJSON = new JSONObject(line);
                    String event = lineJSON.getStr("event");
                    JSONObject responseData = lineJSON.getJSONObject("data");

                    if ("finish".equals(event)) {
                        String answer = responseData.getStr("answer");
                        String reasoningContent = responseData.getStr("reasoningContent");
                        String questionVector = String.join(",", (String[]) responseData.getJSONArray("questionVector").toArray(String.class));
                        Integer tokenUsage = responseData.getInt("tokenUsage");
                        updateQaRecord(qaRecordId, responseData.toString(), answer,
                                reasoningContent, questionVector, tokenUsage);
                    } else if ("error".equals(event)) {
                        updateQaRecord(qaRecordId, responseData.toString(), responseData.getStr("data"),
                                "", "", responseData.getInt("tokenUsage"));
                    }
                } catch (Exception e) {
                    log.error("Error while processing line: {}", e.getMessage());
                }
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            log.error("Error while sending request to Python API: {}", e.getMessage());
        }
    }

    private void handleError(Session session, String errorMsg, Throwable t) {
        System.err.println(errorMsg + ": " + t.getMessage());
        if (session.isOpen()) {
            session.getAsyncRemote().sendText("服务端错误: " + errorMsg);
        }
        SessionManager.removeSession(session);
    }

    public Long addQaRecord(String sessionId, String question, String chatTitle, String modelType, Long modelTypeId) {
        KbQARecord kbQARecord = new KbQARecord();
        kbQARecord.setSessionId(sessionId);
        kbQARecord.setQuestion(question);
        kbQARecord.setChatTitle(chatTitle);
        kbQARecord.setModelType(modelType);
        kbQARecord.setModelTypeId(modelTypeId);

        kbQARecord.setUserId(SessionManager.getLoginUser(sessionId).getUserId());
        kbQARecord.setDeptId(SessionManager.getLoginUser(sessionId).getDeptId());
        kbQARecord.setCreateBy(SessionManager.getLoginUser(sessionId).getUsername());
        kbQARecord.setCreateTime(DateUtils.getNowDate());
        kbQARecordMapper.insertKbQARecord(kbQARecord);
        return kbQARecord.getId();
    }

    public void addUserKbHistory(String sessionId, String sessionName) {
        UserKbHistoryInsertDto userKbHistoryInsertDto = new UserKbHistoryInsertDto();
        userKbHistoryInsertDto.setSessionMark(sessionId);
        userKbHistoryInsertDto.setSessionName(sessionName);

        UserKbHistory check = userKbHistoryMapper.selectBySessionMark(userKbHistoryInsertDto.getSessionMark());
        if (check != null) {
            return;
        }
        UserKbHistory userKbHistory = BeanConvertUtil.conver(userKbHistoryInsertDto, UserKbHistory.class);
        userKbHistory.setCreateBy(SessionManager.getLoginUser(sessionId).getUsername());
        userKbHistory.setCreateTime(new Date());
        userKbHistory.setStatus(1);

        userKbHistoryMapper.insertUserKbHistory(userKbHistory);
    }

    public void addQARecordManagerRel(String sessionId, Long qaRecordId, Long[] kbManagerIds) {
        List<KbQARecordManagerRel> kbQARecordManagerRels = new ArrayList<>(kbManagerIds.length);
        for (Long kbManagerId : kbManagerIds) {
            KbQARecordManagerRel kbQARecordManagerRel = new KbQARecordManagerRel();
            kbQARecordManagerRel.setId(snowflakeGenerator.next());
            kbQARecordManagerRel.setKbQARecordId(qaRecordId);
            kbQARecordManagerRel.setKbManagerId(kbManagerId);
            kbQARecordManagerRel.setCreateBy(SessionManager.getLoginUser(sessionId).getUsername());
            kbQARecordManagerRel.setCreateTime(DateUtils.getNowDate());
            kbQARecordManagerRels.add(kbQARecordManagerRel);
        }
        iKbQARecordManagerRelService.insertKbQARecordManagerRel(kbQARecordManagerRels);
    }

    private void updateQaRecord(Long qaRecordId, String response, String answer, String reasoningContent, String questionVector, Integer tokenUsage) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setId(qaRecordId);
        kbQARecordDto.setResponse(response);
        kbQARecordDto.setAnswer(answer);
        kbQARecordDto.setReasoningContent(reasoningContent);
        kbQARecordDto.setQuestionVector(questionVector);
        kbQARecordDto.setTokenUsage(tokenUsage);
        iKbQARecordService.updateKbQAAnswer(kbQARecordDto);
    }

    public static String getHeader(Session session, String headerName) {
        final String header = (String) session.getUserProperties().get(headerName);
        if (StrUtil.isBlank(header)) {
            log.error("获取header失败");
        }
        return header;
    }
}
