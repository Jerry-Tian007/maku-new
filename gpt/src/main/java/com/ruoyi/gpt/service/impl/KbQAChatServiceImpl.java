package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.enums.ExceptionStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.model.domain.*;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQAChatDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.model.dto.user_kb_history.UserKbHistoryInsertDto;
import com.ruoyi.gpt.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class KbQAChatServiceImpl implements IKbQAChatService {

    @Value("${chat.ragChat}")
    private String RAG_CHAT_URL;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private IModelApiService iModelApiService;

    @Autowired
    private IKbQARecordService iKbQARecordService;

    @Autowired
    private IUserKbHistoryService iUserKbHistoryService;

    @Autowired
    private IKbQARecordManagerRelService iKbQARecordManagerRelService;

    @Autowired
    private IKbQARecordFileRelService iKbQARecordFileRelService;

    @Autowired
    private IKbFileService iKbFileService;

    @Override
    public JSONObject chat(KbQAChatDto kbQAChatDto) {
        // 新增问答记录
        JSONArray kbId = kbQAChatDto.getRagParams().getJSONArray("kbId");
        StringBuilder kbIds = new StringBuilder();
        if (kbId != null && !kbId.isEmpty()) {
            for (int i = 0; i < kbId.size(); i++) {
                kbIds.append(kbId.get(i).toString()).append(";");
            }
        }
        Long qaRecordId = addQaRecord(kbQAChatDto.getSessionId(), kbQAChatDto.getQuestion(), kbQAChatDto.getChatTitle(),
                kbQAChatDto.getModelType(), Long.valueOf(kbQAChatDto.getModelTypeId()), kbIds.toString());
        log.info("qaRecordId: {}", qaRecordId);
        // 新增用户问答历史
        addUserKbHistory(kbQAChatDto.getSessionId(), kbQAChatDto.getChatTitle());
        log.info("新增用户问答历史");
        // 新增问答记录知识库关联关系
        if (kbQAChatDto.getKbManagerIds() != null && kbQAChatDto.getKbManagerIds().length > 0) {
            addQARecordManagerRel(qaRecordId, kbQAChatDto.getKbManagerIds());
            log.info("新增问答记录知识库关联关系");
        }

        Set<String> tokenModelResources = SecurityUtils.getLoginUser().getModelResources();
        if (!tokenModelResources.contains("all") && !tokenModelResources.contains(kbQAChatDto.getModelTypeId())) {
            throw new ServiceException(ExceptionStatusEnum.PERMISSIONS_ERROR);
        }
        ModelChatApiDto modelChatApiDto = BeanConvertUtil.conver(kbQAChatDto, ModelChatApiDto.class);
        ModelResources modelResources2 = iModelApiService.calTpmMax(modelChatApiDto);
        JSONObject entries = new JSONObject();
        entries.set("modelId", modelResources2.getId());
        entries.set("modelParams", modelResources2.getParameter());
        kbQAChatDto.setModelInfo(entries);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<KbQAChatDto> entity = new HttpEntity<>(kbQAChatDto, headers);
        ResponseEntity<String> restResult = restTemplate.postForEntity(RAG_CHAT_URL, entity, String.class);
        JSONObject response = JSONUtil.parseObj(restResult.getBody());
        log.info("response: {}", response);
        ModelResourcesTpmUseDto modelResourcesTpmUseDto = new ModelResourcesTpmUseDto();
        modelResourcesTpmUseDto.setModelId(Long.parseLong(response.getStr("modelId")));
        modelResourcesTpmUseDto.setToken(response.getInt("tokenUsage"));
        iModelApiService.ModelResourcesTpmUse(modelResourcesTpmUseDto);
        Integer code = response.getInt("code");
        String responseStr = response.toString();
        Integer tokenUsage = response.getInt("tokenUsage");
        String chunkSearch = response.getStr("chunkSearch");
        String rewrittenQuery = response.getStr("rewrittenQuery");
        String timeEstimation = response.getStr("timeEstimation");
        if (code != 200) {
            // 更新问答记录
            updateQaRecord(qaRecordId, responseStr, response.getStr("message"), "", "", tokenUsage, "", 0, "", "");
            return response;
        }
        JSONObject data = response.getJSONObject("data");
        String answer = data.getStr("answer");
        String reasoningContent = data.getStr("reasoningContent");
        Integer completionTokens = data.getInt("completionTokens");
        String questionVector = String.join(",", (String[]) data.getJSONArray("questionVector").toArray(String.class));
        // 更新问答记录
        updateQaRecord(qaRecordId, responseStr, answer, reasoningContent, questionVector, tokenUsage,  StringUtils.isNotNull(chunkSearch) ? chunkSearch : "", completionTokens, rewrittenQuery , StringUtils.isNotNull(timeEstimation) ? timeEstimation : "");
        List<String> originalFileNames = data.getJSONArray("originalFileNames").toList(String.class);
        // 新增问答记录和文件的关联关系
        addQARecordFileRel(qaRecordId, originalFileNames);
        log.info("新增问答记录和文件的关联关系");
        response.set("id", qaRecordId);
        return response;
    }

    /**
     * 新增问答记录知识库关联关系
     *
     * @param qaRecordId
     * @param kbManagerIds
     */
    public void addQARecordManagerRel(Long qaRecordId, Long[] kbManagerIds) {
        List<KbQARecordManagerRel> kbQARecordManagerRels = new ArrayList<>(kbManagerIds.length);
        for (Long kbManagerId : kbManagerIds) {
            KbQARecordManagerRel kbQARecordManagerRel = new KbQARecordManagerRel();
            kbQARecordManagerRel.setId(snowflakeGenerator.next());
            kbQARecordManagerRel.setKbQARecordId(qaRecordId);
            kbQARecordManagerRel.setKbManagerId(kbManagerId);
            kbQARecordManagerRel.setCreateBy(SecurityUtils.getUsername());
            kbQARecordManagerRel.setCreateTime(DateUtils.getNowDate());
            kbQARecordManagerRels.add(kbQARecordManagerRel);
        }
        iKbQARecordManagerRelService.insertKbQARecordManagerRel(kbQARecordManagerRels);
    }

    /**
     * 新增问答记录
     */
    public Long addQaRecord(String sessionId, String question, String chatTitle, String modelType, Long modelTypeId, String kbId) {
        KbQARecord kbQARecord = new KbQARecord();
        kbQARecord.setSessionId(sessionId);
        kbQARecord.setQuestion(question);
        kbQARecord.setChatTitle(chatTitle);
        kbQARecord.setModelType(modelType);
        kbQARecord.setModelTypeId(modelTypeId);
        kbQARecord.setKbId(kbId);
        iKbQARecordService.insertKbQARecord(kbQARecord);
        return kbQARecord.getId();
    }

    /**
     * 新增用户问答历史
     *
     * @param sessionMark 会话标识
     * @param sessionName 绘画名称
     */
    public void addUserKbHistory(String sessionMark, String sessionName) {
        UserKbHistoryInsertDto userKbHistoryInsertDto = new UserKbHistoryInsertDto();
        userKbHistoryInsertDto.setSessionMark(sessionMark);
        userKbHistoryInsertDto.setSessionName(sessionName);
        iUserKbHistoryService.insertUserKbHistory(userKbHistoryInsertDto);
    }

    /**
     * 更新问答记录
     *
     * @param qaRecordId       问答ID
     * @param response         接口返回值
     * @param answer           答案
     * @param reasoningContent 思考过程
     * @param questionVector   问题向量
     * @param tokenUsage       token用量
     */
    private void updateQaRecord(Long qaRecordId, String response, String answer, String reasoningContent, String questionVector, Integer tokenUsage, String chunkSearch, Integer completionTokens, String rewrittenQuery, String timeEstimation) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setId(qaRecordId);
        kbQARecordDto.setResponse(response);
        kbQARecordDto.setAnswer(answer);
        kbQARecordDto.setReasoningContent(reasoningContent);
        kbQARecordDto.setQuestionVector(questionVector);
        kbQARecordDto.setTokenUsage(tokenUsage);
        kbQARecordDto.setChunkSearch(chunkSearch);
        kbQARecordDto.setCompletionTokens(completionTokens);
        kbQARecordDto.setRewrittenQuery(rewrittenQuery);
        kbQARecordDto.setTimeEstimation(timeEstimation);
        iKbQARecordService.updateKbQAAnswer(kbQARecordDto);
    }

    /**
     * 新增问答记录和文件的关联关系
     */
    private void addQARecordFileRel(Long qaRecordId, List<String> originalFileNames) {
        // TODO
        if (!CollectionUtils.isEmpty(originalFileNames)) {
            List<KbQARecordFileRel> kbQARecordFileRels = new ArrayList<>(originalFileNames.size());
            for (int i = 0; i < originalFileNames.size(); i++) {
                String originalFileName = originalFileNames.get(i);
                String suffix = FilenameUtils.getExtension(originalFileName).toLowerCase();
                String fileName = FilenameUtils.getBaseName(originalFileName);
                KbFile kbFile = iKbFileService.selectKbFileByFileNameAndSuffix(fileName, suffix);
                if (kbFile != null) {
                    KbQARecordFileRel kbQARecordFileRel = new KbQARecordFileRel();
                    kbQARecordFileRel.setId(snowflakeGenerator.next());
                    kbQARecordFileRel.setKbQARecordId(qaRecordId);
                    kbQARecordFileRel.setKbFileName(originalFileName);
                    kbQARecordFileRel.setKbFileId(kbFile.getId());
                    kbQARecordFileRels.add(kbQARecordFileRel);
                }
            }
            if (!CollectionUtils.isEmpty(kbQARecordFileRels)) {
                iKbQARecordFileRelService.insertKbQARecordFileRel(kbQARecordFileRels);
            }
        } else {
            System.out.println("引用文献为空");
        }
    }
}
