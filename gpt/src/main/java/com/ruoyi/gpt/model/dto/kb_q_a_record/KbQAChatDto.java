package com.ruoyi.gpt.model.dto.kb_q_a_record;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 会话参数
 *
 * @author tiankk
 */
@Data
public class KbQAChatDto {
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
    private JSONObject ragParams;
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
     * 会话ID
     */
    private String sessionId;
    /**
     * 问题
     */
    private String question;
    /**
     * 会话标题
     */
    private String chatTitle;
    /**
     * 知识库ID
     */
    private Long[] kbManagerIds;
}
