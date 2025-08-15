package com.ruoyi.gpt.model.dto.message_record;

import lombok.Data;

/**
 * 消息记录插入DTO类
 * 用于封装消息记录的创建数据
 */
@Data
public class MessageRecordInsertDto {
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
}
