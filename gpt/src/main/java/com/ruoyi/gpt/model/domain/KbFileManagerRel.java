package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class KbFileManagerRel extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 知识库id
     */
    private Long kbId;
}
