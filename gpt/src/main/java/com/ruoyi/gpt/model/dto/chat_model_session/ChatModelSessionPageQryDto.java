package com.ruoyi.gpt.model.dto.chat_model_session;

import java.util.Date;
import lombok.Data;

/**
 *  查询分页信息参数类
 *
 * @author Eric
 */
@Data
public class ChatModelSessionPageQryDto {
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
