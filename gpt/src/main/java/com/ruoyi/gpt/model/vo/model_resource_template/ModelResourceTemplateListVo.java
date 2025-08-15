package com.ruoyi.gpt.model.vo.model_resource_template;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型资源模板管理对象 model_resource_template
 *
 * @author Eric
 * @date 2024-07-15
 */
@Data
public class ModelResourceTemplateListVo extends BaseEntity {
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
     * 模型模板
     */
    private String template;

    private String scene;

}
