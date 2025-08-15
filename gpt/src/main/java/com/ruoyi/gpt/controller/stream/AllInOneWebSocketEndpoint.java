package com.ruoyi.gpt.controller.stream;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.ChatModelSessionMapper;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.mapper.UserKbHistoryMapper;
import com.ruoyi.gpt.model.domain.ChatModelSessionEntity;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.chat_model_session.ChatModelSessionAddDto;
import com.ruoyi.gpt.model.dto.chat_model_session.ChatModelSessionUpdateDto;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.service.IChatModelSessionService;
import com.ruoyi.gpt.service.IChatgptQARecordService;
import com.ruoyi.gpt.service.IModelApiService;
import com.ruoyi.gpt.service.impl.util.TokenServiceWs;
import com.ruoyi.gpt.service.impl.util.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
@ServerEndpoint(value = "/websocket/allInOne/chat/{sessionId}", configurator = WebSocketConfig.class)
public class AllInOneWebSocketEndpoint {

    public static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        AllInOneWebSocketEndpoint.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public static TokenServiceWs tokenServiceWs;

    @Autowired
    public void setTokenServiceWs(TokenServiceWs tokenServiceWs) {
        AllInOneWebSocketEndpoint.tokenServiceWs = tokenServiceWs;
    }

    public static UserKbHistoryMapper userKbHistoryMapper;

    @Autowired
    private void setUserKbHistoryMapper(UserKbHistoryMapper userKbHistoryMapper) {
        AllInOneWebSocketEndpoint.userKbHistoryMapper = userKbHistoryMapper;
    }

    public static SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private void setSnowflakeGenerator(SnowflakeGenerator snowflakeGenerator) {
        AllInOneWebSocketEndpoint.snowflakeGenerator = snowflakeGenerator;
    }

    public static IModelApiService iModelApiService;

    @Autowired
    private void setIModelApiService(IModelApiService iModelApiService) {
        AllInOneWebSocketEndpoint.iModelApiService = iModelApiService;
    }

    public static IChatModelSessionService iChatModelSessionService;

    @Autowired
    private void setIChatModelSessionService(IChatModelSessionService iChatModelSessionService) {
        AllInOneWebSocketEndpoint.iChatModelSessionService = iChatModelSessionService;
    }

    public static IChatgptQARecordService iChatgptQARecordService;

    @Autowired
    private void setIChatgptQARecordService(IChatgptQARecordService iChatgptQARecordService) {
        AllInOneWebSocketEndpoint.iChatgptQARecordService = iChatgptQARecordService;
    }

    public static ChatModelSessionMapper chatModelSessionMapper;

    @Autowired
    private void setChatModelSessionMapper(ChatModelSessionMapper chatModelSessionMapper) {
        AllInOneWebSocketEndpoint.chatModelSessionMapper = chatModelSessionMapper;
    }

    public static ChatgptQARecordMapper chatgptQARecordMapper;

    @Autowired
    private void setChatgptQARecordMapper(ChatgptQARecordMapper chatgptQARecordMapper) {
        AllInOneWebSocketEndpoint.chatgptQARecordMapper = chatgptQARecordMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        final String token = getHeader(session, "wsToken");
        LoginUser loginUser = tokenServiceWs.getLoginUserWs(token);
        SessionManager.saveSession(sessionId, session);
        SessionManager.saveSessionLoginUser(sessionId, loginUser);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject msgJSON = new JSONObject(message);
        ModelChatApiDto params = JSONUtil.toBean(msgJSON, ModelChatApiDto.class);
        try {
            Long qaRecordId = addChatGptQaRecord(params.getQuestion(), params.getModelType(), params.getSessionId());

            ModelResources modelResources2 = iModelApiService.calTpmMax(params);
            JSONObject entries = new JSONObject();
            entries.set("modelId", modelResources2.getId());
            entries.set("modelParams", modelResources2.getParameter());
            params.setModelInfo(entries);

            getAnswer(params, qaRecordId);
        } catch (Exception e) {
            handleError(session, "消息处理异常", e, params.getSessionId());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println(reason.getReasonPhrase());
        System.out.println("前端连接关闭: " + session.getId());
        SessionManager.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        handleError(session, "WebSocket错误", throwable, "");
    }

    public void getAnswer(ModelChatApiDto modelChatApiDto, Long qaRecordId) {
        threadPoolTaskExecutor.submit(() -> {
            JSONObject params = JSONUtil.parseObj(modelChatApiDto);
            try {
                URL apiUrl = new URL(DictUtils.getDictValue("python", "all_in_stream_one_api_url"));
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setChunkedStreamingMode(10);
                connection.getOutputStream().write(params.toString().getBytes());

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (StringUtils.isNotEmpty(line = reader.readLine())) {
                    JSONObject lineJSON = new JSONObject(line);
                    lineJSON.set("id", qaRecordId);
                    String event = lineJSON.getStr("event");
                    JSONObject responseData = lineJSON.getJSONObject("data");
                    try {
                        ModelResourcesTpmUseDto modelResourcesTpmUseDto = new ModelResourcesTpmUseDto();
                        modelResourcesTpmUseDto.setModelId(Long.parseLong(responseData.getStr("modelId")));
                        modelResourcesTpmUseDto.setToken(responseData.getInt("tokenUsage"));
                        iModelApiService.ModelResourcesTpmUse(modelResourcesTpmUseDto);

                        SessionManager.getSession(modelChatApiDto.getSessionId()).getBasicRemote().sendText(lineJSON.toString());

                        if ("finish".equals(event)) {
                            updateChatGptQaRecord(qaRecordId, responseData.getStr("data"),
                                    responseData.getInt("tokenUsage"),  responseData.getInt("completionTokens"));
                            updateChatGptQaRecordDetail(qaRecordId, responseData);
                        } else if ("error".equals(event)) {
                            updateChatGptQaRecord(qaRecordId, responseData.getStr("message"),
                                    responseData.getInt("tokenUsage"),0);
                        }
                    } catch (Exception e) {
                        JSONObject err = new JSONObject();
                        err.set("event", "error");
                        err.set("sessionId", modelChatApiDto.getSessionId());
                        JSONObject data = new JSONObject();
                        data.set("data", "web processing line error: " + e.getMessage());
                        data.set("modelId", responseData.getStr("modelId"));
                        data.set("tokenUsage", 0);
                        err.set("data", data);
                        err.set("time", DateUtils.getNowDate());

                        SessionManager.getSession(modelChatApiDto.getSessionId()).getBasicRemote().sendText(err.toString());
                        log.error("Error while processing line: {}", e.getMessage());
                    }
                }
                reader.close();
                connection.disconnect();
            } catch (Exception e) {
                log.error("Error while sending request to Python API: {}", e.getMessage());
            }
        });
    }

    private void handleError(Session session, String errorMsg, Throwable t, String sessionId) throws IOException {
        System.err.println(errorMsg + ": " + t.getMessage());
        if (session.isOpen()) {
            JSONObject err = new JSONObject();
            err.set("event", "error");
            err.set("sessionId", sessionId);
            JSONObject data = new JSONObject();
            data.set("data", "web processing line error: " + errorMsg);
            data.set("modelId", "");
            data.set("tokenUsage", 0);
            err.set("data", data);
            err.set("time", DateUtils.getNowDate());
            session.getBasicRemote().sendText(err.toString());
        }
    }

    private Long addChatGptQaRecord(String question, String model, String sessionId) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setQuestion(question);
        chatgptQARecord.setModel(model);
        chatgptQARecord.setSessionId(sessionId);
        if (iChatModelSessionService.selectChatModelSessionBySessionId(sessionId) == null) {
            ChatModelSessionAddDto chatModelSessionAddDto = new ChatModelSessionAddDto();
            chatModelSessionAddDto.setSessionId(sessionId);
            chatModelSessionAddDto.setModelName(model);
            chatModelSessionAddDto.setSessionTitle(question);

            ChatModelSessionEntity chatModelSessionEntity = BeanConvertUtil.conver(chatModelSessionAddDto, ChatModelSessionEntity.class);
            chatModelSessionEntity.setCreateBy(SessionManager.getLoginUser(sessionId).getUser().getUserName());
            chatModelSessionEntity.setCreateTime(DateUtils.getNowDate());
            chatModelSessionEntity.setUpdateTime(DateUtils.getNowDate());
            chatModelSessionEntity.setUserId(SessionManager.getLoginUser(sessionId).getUserId());
            chatModelSessionEntity.setDelFlag("0");
            chatModelSessionMapper.add(chatModelSessionEntity);
        } else {
            ChatModelSessionUpdateDto chatModelSessionUpdateDto = new ChatModelSessionUpdateDto();
            chatModelSessionUpdateDto.setSessionId(sessionId);

            ChatModelSessionEntity chatModelSessionEntity = BeanConvertUtil.conver(chatModelSessionUpdateDto, ChatModelSessionEntity.class);
            chatModelSessionEntity.setUpdateBy(SessionManager.getLoginUser(sessionId).getUser().getUserName());
            chatModelSessionEntity.setUpdateTime(DateUtils.getNowDate());
            chatModelSessionMapper.updateById(chatModelSessionEntity);
        }
        chatgptQARecord.setDeptId(SessionManager.getLoginUser(sessionId).getDeptId());
        chatgptQARecord.setUserId(SessionManager.getLoginUser(sessionId).getUserId());
        chatgptQARecord.setCreateBy(SessionManager.getLoginUser(sessionId).getUser().getUserName());
        chatgptQARecord.setCreateTime(DateUtils.getNowDate());
        chatgptQARecordMapper.insertChatgptQARecord(chatgptQARecord);
        return chatgptQARecord.getId();
    }

    private void updateChatGptQaRecord(Long id, String answer, Integer tokenUsage, Integer completionTokens) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setId(id);
        chatgptQARecord.setAnswer(answer);
        chatgptQARecord.setTokenUsage(tokenUsage);
        chatgptQARecord.setCompletionTokens(completionTokens);
        iChatgptQARecordService.updateChatgptQARecord(chatgptQARecord);
    }

    /**
     * 更新思考过程，关联文件
     *
     * @param id
     * @param result
     */
    private void updateChatGptQaRecordDetail(Long id, JSONObject result) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setId(id);
        chatgptQARecord.setLinkAndTitle(result.getJSONArray("linkAndTitle").toString());
        chatgptQARecord.setReasoningContent(result.get("reasoningContent").toString());
        iChatgptQARecordService.updateChatgptQARecordDetail(chatgptQARecord);
    }

    public static String getHeader(Session session, String headerName) {
        final String header = (String) session.getUserProperties().get(headerName);
        if (StrUtil.isBlank(header)) {
            log.error("获取header失败");
        }
        return header;
    }
}
