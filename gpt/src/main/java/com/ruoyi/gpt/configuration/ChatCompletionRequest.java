package com.ruoyi.gpt.configuration;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.gpt.service.IKbQARecordService;
import com.ruoyi.gpt.service.impl.util.MyWebSocketUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    private static IKbQARecordService kbQARecordService;

    @Resource
    public void setKbQARecordService(IKbQARecordService kbQARecordService) {
        ChatCompletionRequest.kbQARecordService = kbQARecordService;
    }

    /**
     * 输入给模型的会话信息
     * 用户输入的内容；role=user
     * 挟带历史的内容；role=bot
     */
    private JSONArray history;

    private String approach;

    private String top;

    private String search;

    private String region;

    private String userId;

    private String role;

    private Long roleId;

    private Long regionId;

    private String trainType;

    private Float temperature;

    private String language;

    private String sessionId;

    private String power;

    private String inverter;

    private String needHidden;


    @Override
    public String toString() {
        JSONObject paramsMap = new JSONObject();
//        paramsMap.put("request_id", requestId);
        paramsMap.put("history", history);
        paramsMap.put("temperature", temperature);

        paramsMap.put("approach", approach);
        paramsMap.put("search", search);
        paramsMap.put("top", top);
        paramsMap.put("region", region);
        paramsMap.put("role", role);
        paramsMap.put("userId", userId);
        paramsMap.put("roleId", roleId);
        paramsMap.put("trainType", trainType);
        paramsMap.put("regionId", regionId);
        paramsMap.put("sessionId", sessionId);
        paramsMap.put("power", power);
        paramsMap.put("inverter", inverter);
        paramsMap.put("language", language);
        paramsMap.put("needHidden", needHidden);


        paramsMap.putOpt("config", kbQARecordService.getKbQAConfig());;
//
        System.out.println("parmasMap is" + paramsMap);
        try {
            return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
