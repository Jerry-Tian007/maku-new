package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.MessageRecordMapper;
import com.ruoyi.gpt.model.domain.MessageRecord;
import com.ruoyi.gpt.model.dto.message_record.MessageRecordInsertDto;
import com.ruoyi.gpt.model.vo.message_record.MessageRecordListVo;
import com.ruoyi.gpt.service.IMessageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息记录Service业务处理层
 */
@Service
public class MessageRecordServiceImpl implements IMessageRecordService {
    @Autowired
    private MessageRecordMapper messageRecordMapper;

    /**
     * 保存消息记录
     *
     * @param messageRecordInsertDto
     * @return
     */
    @Override
    public Integer saveMessageRecord(MessageRecordInsertDto messageRecordInsertDto) {
        MessageRecord messageRecord = BeanConvertUtil.conver(messageRecordInsertDto, MessageRecord.class);
        messageRecord.setCreateTime(new Date());
        messageRecord.setCreateBy(SecurityUtils.getUsername());
        return messageRecordMapper.insertUnsentMessaageRecord(messageRecord);
    }

    /**
     * 删除消息记录
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteMessageRecord(String id) {
        return messageRecordMapper.deleteUnsentMessageRecord(id);
    }

    /**
     * 查询所有未成功发送的消息
     *
     * @return
     */
    @Override
    public List<MessageRecordListVo> selectFailedMessageRecordList() {
        List<MessageRecord> messageRecords = messageRecordMapper.selectFailedMessageRecordList();
        return BeanConvertUtil.converList(messageRecords, MessageRecordListVo.class);
    }

    /**
     * 修改消息发送状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateMessageRecord(String id, String sendingStatus) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setId(id);
        messageRecord.setSendingStatus(sendingStatus);
        messageRecord.setUpdateTime(new Date());
        return messageRecordMapper.updateUnsentMessageRecord(messageRecord);
    }
}
