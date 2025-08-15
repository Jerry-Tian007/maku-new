package com.ruoyi.gpt.configuration;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.model.dto.kb_file.KbFileUpdateTrainStatusDto;
import com.ruoyi.gpt.model.dto.message_record.MessageRecordInsertDto;
import com.ruoyi.gpt.service.IKbFileService;
import com.ruoyi.gpt.service.IMessageRecordService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * spring rabbitmq 生产者
 **/
@Component
public class RabbitmqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private IMessageRecordService messageRecordService;

    /**
     * 保存消息记录并发送
     *
     * @param exchange
     * @param routingKey
     * @param content
     */
    public void saveAndSendMessage(String exchange, String routingKey, String content) {
        String messageId = snowflakeGenerator.next().toString();
        MessageRecordInsertDto insertDto = new MessageRecordInsertDto();
        insertDto.setId(messageId);
        insertDto.setContent(content);
        insertDto.setExchange(exchange);
        insertDto.setRoutingKey(routingKey);
        messageRecordService.saveMessageRecord(insertDto);
        try {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(messageId);
            rabbitTemplate.convertAndSend(exchange, routingKey, content, correlationData);
        } catch (Exception e) {
            messageRecordService.updateMessageRecord(messageId, "failed");
        }
    }
}
