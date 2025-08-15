package com.ruoyi.gpt.model.dto.user_model_parameter;


import lombok.Data;

/**
 *  更新
 *
 * @author Eric
 */
@Data
public class UserModelParameterUpdateDto {
            /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 提示词id
     */
    private Long promptId;
    /**
     * 提示词设置
     */
    private String promptSetting;
    /**
     * 配置设置
     */
    private String configureSetting;
    /**
     * 模型
     */
    private String model;

}

