package com.ruoyi.gpt.model.dto.chat_model_session;


import lombok.Data;

/**
 *  添加
 *
 * @author Eric
 */
@Data
public class ChatModelSessionAddDto {
            /**
     * id
     */
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


}
