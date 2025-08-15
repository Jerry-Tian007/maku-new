package com.ruoyi.gpt.model.dto.model_api;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 会话参数
 *
 * @author tiankk
 */
@Data
public class ModelChatApiDto {
    /**
     * 模型类型
     */
    private String modelType;
    /**
     * 模型类型Id
     */
    private String modelTypeId;
    /**
     * 模型信息
     */
    private JSONObject modelInfo;
    /**
     * 会话参数
     */
    private JSONObject chatParams;
    /**
     * 历史记录
     */
    private JSONArray history;
    /**
     * 提示词
     */
    private String prompt;
    /**
     * 场景
     */
    private String scene;
    /**
     * 问题
     */
    private String question;
    /**
     * 是否开启联网搜索
     */
    private Boolean needOnlineSearch;

    private String fileMark;

    private String sessionId;
    /***
     * 所用到的Token数量
     */
    private Integer completionTokens;
}
