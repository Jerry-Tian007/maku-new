package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class MessageRecord extends BaseEntity {
    /**
     * 消息id
     */
    private String id;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 交换机
     */
    private String exchange;
    /**
     * 路由键
     */
    private String routingKey;
    /**
     * 发送状态
     */
    private String sendingStatus;
}
