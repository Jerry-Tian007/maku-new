package com.ruoyi.gpt.model.vo.user_model_parameter;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 *  返回分页信息参数类
 *
 * @author Eric
 */
@Data
public class UserModelParameterPageVO {
            /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;
    /**
     * 提示词id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
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
