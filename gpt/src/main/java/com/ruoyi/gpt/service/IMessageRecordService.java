package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.message_record.MessageRecordInsertDto;
import com.ruoyi.gpt.model.vo.message_record.MessageRecordListVo;

import java.util.List;

/**
 * 消息记录Service接口
 * 定义消息记录的CRUD操作和状态管理
 */
public interface IMessageRecordService {
    /**
     * 保存消息记录
     *
     * @param messageRecordInsertDto 消息记录插入DTO
     * @return 影响行数
     */
    Integer saveMessageRecord(MessageRecordInsertDto messageRecordInsertDto);

    /**
     * 删除消息记录
     *
     * @param id 消息ID
     * @return 影响行数
     */
    Integer deleteMessageRecord(String id);

    /**
     * 查询所有未成功发送的消息
     *
     * @return 未成功发送的消息列表
     */
    List<MessageRecordListVo> selectFailedMessageRecordList();

    /**
     * 修改消息发送状态
     *
     * @param id 消息ID
     * @param sendingStatus 发送状态(succeed:成功, failed:失败)
     * @return 影响行数
     */
    Integer updateMessageRecord(String id, String sendingStatus);
}
