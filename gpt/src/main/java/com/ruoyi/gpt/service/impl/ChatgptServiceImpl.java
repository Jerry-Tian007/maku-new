package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.enums.ExceptionStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.domain.ChatModelSessionEntity;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.chat_model_session.ChatModelSessionAddDto;
import com.ruoyi.gpt.model.dto.chat_model_session.ChatModelSessionUpdateDto;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.model.dto.model_api.ModelResourcesTpmUseDto;
import com.ruoyi.gpt.service.IChatModelSessionService;
import com.ruoyi.gpt.service.IChatgptQARecordService;
import com.ruoyi.gpt.service.IChatgptService;
import com.ruoyi.gpt.service.IModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class ChatgptServiceImpl implements IChatgptService {


    @Value("${chat.openChat}")
    private String OPEN_CHAT_URL;

    @Autowired
    private IModelApiService iModelApiService;

    @Autowired
    private IChatgptQARecordService iChatgptQARecordService;

    @Autowired
    private IChatModelSessionService iChatModelSessionService;

    @Override
    public JSONObject chat(ModelChatApiDto params) {
        System.out.println("请求参数：" + params);
        Long qaRecordId = addChatGptQaRecord(params.getQuestion(), params.getModelType(), params.getSessionId());
        Set<String> tokenModelResources = SecurityUtils.getLoginUser().getModelResources();
        if (!tokenModelResources.contains("all") && !tokenModelResources.contains(params.getModelTypeId())) {
            throw new ServiceException(ExceptionStatusEnum.PERMISSIONS_ERROR);
        }
        ModelResources modelResources2 = iModelApiService.calTpmMax(params);
        JSONObject entries = new JSONObject();
        entries.set("modelId", modelResources2.getId());
        entries.set("modelParams", modelResources2.getParameter());
        params.setModelInfo(entries);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<ModelChatApiDto> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> restResult = restTemplate.postForEntity(OPEN_CHAT_URL, entity, String.class);
        JSONObject response = JSONUtil.parseObj(restResult.getBody());
        System.out.println("返回结果：" + response.toString());
        ModelResourcesTpmUseDto modelResourcesTpmUseDto = new ModelResourcesTpmUseDto();
        modelResourcesTpmUseDto.setModelId(Long.parseLong(response.getStr("modelId")));
        modelResourcesTpmUseDto.setToken(response.getInt("tokenUsage"));
        iModelApiService.ModelResourcesTpmUse(modelResourcesTpmUseDto);
        response.set("id", qaRecordId);
        Integer code = response.getInt("code");
        Integer tokenUsage = response.getInt("tokenUsage");
        if (code != 200) {
            // 更新问答记录
            updateChatGptQaRecord(qaRecordId, response.getStr("message"), tokenUsage);
            return response;
        }
        updateChatGptQaRecord(qaRecordId, response.getStr("data"), tokenUsage);
        updateChatGptQaRecordDetail(qaRecordId, response);
        return response;
    }

    /**
     * 新增问题记录
     *
     * @param question
     * @param model
     */
    private Long addChatGptQaRecord(String question, String model,String sessionId) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setQuestion(question);
        chatgptQARecord.setModel(model);
        chatgptQARecord.setSessionId(sessionId);
        if (iChatModelSessionService.selectChatModelSessionBySessionId(sessionId) ==null){
            ChatModelSessionAddDto chatModelSessionAddDto = new ChatModelSessionAddDto();
            chatModelSessionAddDto.setSessionId(sessionId);
            chatModelSessionAddDto.setModelName(model);
            chatModelSessionAddDto.setSessionTitle(question);
            iChatModelSessionService.add(chatModelSessionAddDto);
        }else {
            ChatModelSessionUpdateDto chatModelSessionUpdateDto = new ChatModelSessionUpdateDto();
            chatModelSessionUpdateDto.setSessionId(sessionId);
            iChatModelSessionService.update(chatModelSessionUpdateDto);
        }
        iChatgptQARecordService.insertChatgptQARecord(chatgptQARecord);
        return chatgptQARecord.getId();
    }

    /**
     * 更新问题记录
     *
     * @param id
     * @param answer
     */
    private void updateChatGptQaRecord(Long id, String answer, Integer tokenUsage) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setId(id);
        chatgptQARecord.setAnswer(answer);
        chatgptQARecord.setTokenUsage(tokenUsage);
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

}
