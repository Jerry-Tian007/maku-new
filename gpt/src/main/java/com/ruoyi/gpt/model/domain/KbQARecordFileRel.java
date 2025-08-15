package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class KbQARecordFileRel extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 问答记录id
     */
    private Long kbQARecordId;
    /**
     * 文件id
     */
    private Long kbFileId;
    /**
     * 文件名称
     */
    private String kbFileName;
}
