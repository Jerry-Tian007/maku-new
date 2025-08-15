package com.ruoyi.gpt.configuration;


import com.ruoyi.gpt.model.vo.message_record.MessageRecordListVo;
import com.ruoyi.gpt.service.IMessageRecordService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * RabbitMQ配置类
 * 负责声明交换机、队列、绑定关系以及配置消息确认机制
 * 同时提供定时任务重试发送失败的消息
 */
@Configuration
@EnableScheduling
public class RabbitmqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitMQ模板，用于发送消息
    @Autowired
    private IMessageRecordService messageRecordService; // 消息记录服务，用于更新消息状态

    // 交换机名称常量
    public static final String EXCHANGE_NAME = "messageExchange";
    // 队列名称常量
    public static final String UPDATE_QUEUE_NAME = "update_queue";
    public static final String DELETE_QUEUE_NAME = "delete_queue";
    public static final String TRAIN_QUEUE_NAME = "train_queue";
    public static final String RESULT_QUEUE_NAME = "result_queue";

    /**
     * 声明主题交换机
     * @return 主题交换机实例
     */
    @Bean("messageExchange")
    public Exchange messageExchange() {
        // 创建持久化的主题交换机
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    //    /**
    //     * 声明更新队列
    //     * @return 更新队列实例
    //     */
    //    @Bean("update_queue")
    //    public Queue updateQueue() {
    //        return QueueBuilder.durable(UPDATE_QUEUE_NAME).build();
    //    }
    //

    /**
     * 声明删除队列
     * @return 删除队列实例
     */
    @Bean("delete_queue")
    public Queue deleteQueue() {
        return QueueBuilder.durable(DELETE_QUEUE_NAME).build();
    }

    /**
     * 声明训练队列
     * @return 训练队列实例
     */
    @Bean("train_queue")
    public Queue trainQueue() {
        return QueueBuilder.durable(TRAIN_QUEUE_NAME).build();
    }

    /**
     * 声明结果队列
     * @return 结果队列实例
     */
    @Bean("result_queue")
    public Queue resultQueue() {
        return QueueBuilder.durable(RESULT_QUEUE_NAME).build();
    }

    //    /**
    //     * 绑定更新队列和交换机
    //     * @param queue 更新队列
    //     * @param exchange 交换机
    //     * @return 绑定关系
    //     */
    //    @Bean
    //    public Binding updateQueueExchange(@Qualifier("update_queue") Queue queue,
    //                                       @Qualifier("messageExchange") Exchange exchange) {
    //        return BindingBuilder.bind(queue).to(exchange).with("update").noargs();
    //    }
    //

    /**
     * 绑定删除队列和交换机
     * @param queue 删除队列
     * @param exchange 交换机
     * @return 绑定关系
     */
    @Bean
    public Binding deleteQueueExchange(@Qualifier("delete_queue") Queue queue,
                                       @Qualifier("messageExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("delete").noargs();
    }

    /**
     * 绑定训练队列和交换机
     * @param queue 训练队列
     * @param exchange 交换机
     * @return 绑定关系
     */
    @Bean
    public Binding trainQueueExchange(@Qualifier("train_queue") Queue queue,
                                      @Qualifier("messageExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("train").noargs();
    }

    /**
     * 绑定结果队列和交换机
     * @param queue 结果队列
     * @param exchange 交换机
     * @return 绑定关系
     */
    @Bean
    public Binding resultQueueExchange(@Qualifier("result_queue") Queue queue,
                                       @Qualifier("messageExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("result").noargs();
    }

    /**
     * 初始化方法，启用消息确认和返回回调
     * 在对象构造后执行
     */
    @PostConstruct
    public void enableCallBack() {
        // ConfirmCallback：确认消息是否成功发送到交换机
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息成功发送至交换机！");
                // 更新消息记录状态为成功
                messageRecordService.updateMessageRecord(correlationData.getId(), "succeed");
            } else {
                System.out.println("消息发送失败，交换机未收到消息！");
                // 更新消息记录状态为失败
                messageRecordService.updateMessageRecord(correlationData.getId(), "failed");
            }
        }));

        // ReturnCallback：处理消息路由失败的情况
        rabbitTemplate.setReturnsCallback(returned -> System.out.println("消息路由失败，未发送至相应队列！\n应答码" + returned.getReplyCode()
                + "\n原因" + returned.getReplyText()
                + "\n交换机" + returned.getExchange()
                + "\n路由键" + returned.getRoutingKey()
                + "\n消息" + returned.getMessage().toString()));
    }

    /**
     * 定时任务：每10秒重试发送失败的消息
     * fixedRate = 10000 表示每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void retryUnsentMessages() {
        // 获取所有发送失败的消息记录
        List<MessageRecordListVo> messageRecordListVos = messageRecordService.selectFailedMessageRecordList();
        for (MessageRecordListVo messageRecordListVo : messageRecordListVos) {
            try {
                System.out.println("尝试重新发送消息：" + messageRecordListVo.getId());
                // 创建关联数据，设置消息ID
                CorrelationData correlationData = new CorrelationData();
                correlationData.setId(messageRecordListVo.getId());
                // 重新发送消息
                rabbitTemplate.convertAndSend(messageRecordListVo.getExchange(), messageRecordListVo.getRoutingKey(), messageRecordListVo.getContent(), correlationData);
            } catch (Exception e) {
                System.out.println("消息重新发送失败：" + e.getMessage());
            }
        }
    }
}
