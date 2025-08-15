package com.ruoyi.gpt.model.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 模型资源模板管理对象 model_resource_template
 *
 * @author Eric
 * @date 2024-07-15
 */
@Data
public class ModelResourceTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 模型类型
     */
    private String type;

    /**
     * 模型图标
     */
    private String icon;

    /**
     * 模型模板
     */
    private String template;
    /**
     * 场景
     */
    private String scene;

}
