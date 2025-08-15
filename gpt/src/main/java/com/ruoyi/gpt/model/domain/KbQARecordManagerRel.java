package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class KbQARecordManagerRel extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 问答记录id
     */
    private Long kbQARecordId;
    /**
     * 知识库id
     */
    private Long kbManagerId;
}
