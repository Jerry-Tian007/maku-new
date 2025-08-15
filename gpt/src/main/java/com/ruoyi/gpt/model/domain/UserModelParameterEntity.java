package com.ruoyi.gpt.model.domain;


import lombok.Data;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 *  Entity
 *
 * @author Eric
 */
@Data
public class UserModelParameterEntity extends BaseEntity{
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
