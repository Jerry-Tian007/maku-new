package com.ruoyi.gpt.model.vo.model_resources;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ModelResourcesSelectVo {

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
     * 模型类型
     */
    private String icon;

    /**
     * 模型类型
     */
    private String scene;
}
