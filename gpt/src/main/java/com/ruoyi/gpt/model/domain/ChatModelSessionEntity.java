package com.ruoyi.gpt.model.domain;


import lombok.Data;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 *  Entity
 *
 * @author Eric
 */
@Data
public class ChatModelSessionEntity extends BaseEntity{
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

    private Long userId;

    private String delFlag;

}
