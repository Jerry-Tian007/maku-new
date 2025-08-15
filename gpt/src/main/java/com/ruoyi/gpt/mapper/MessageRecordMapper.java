package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.model.domain.MessageRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageRecordMapper {
    Integer insertUnsentMessaageRecord(@Param("MessageRecord") MessageRecord MessageRecord);

    Integer deleteUnsentMessageRecord(@Param("id") String id);

    List<MessageRecord> selectFailedMessageRecordList();

    Integer updateUnsentMessageRecord(@Param("MessageRecord") MessageRecord MessageRecord);
}
