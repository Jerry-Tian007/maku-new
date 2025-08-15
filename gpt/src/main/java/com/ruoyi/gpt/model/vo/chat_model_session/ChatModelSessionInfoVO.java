package com.ruoyi.gpt.model.vo.chat_model_session;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


/**
 *  返回详情参数类
 *
 * @author Eric
 */
@Data
public class ChatModelSessionInfoVO {
            /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * session ID
     */
    private String sessionId;
    /**
     * session 标题
     */
    private String sessionTitle;
    /**
     * 
     */
    private String modelName;

    private Long userId;

    private String delFlag;

}
