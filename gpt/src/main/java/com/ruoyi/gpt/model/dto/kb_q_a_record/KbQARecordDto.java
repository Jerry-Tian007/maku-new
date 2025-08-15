package com.ruoyi.gpt.model.dto.kb_q_a_record;

import com.ruoyi.gpt.model.domain.KbQARecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class KbQARecordDto extends KbQARecord {

    private Date startTime;

    private Date endTime;

    private String model;

    private String createByAccount;

    private String kbName;

    private Integer completionTokens;

}
