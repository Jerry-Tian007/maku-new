package com.ruoyi.gpt.model.vo.message_record;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRecordListVo {
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 发送状态
     */
    private String sendingStatus;
}
