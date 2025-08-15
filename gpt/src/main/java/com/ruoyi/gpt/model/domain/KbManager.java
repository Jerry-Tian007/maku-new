package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class KbManager extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 知识库名称
     */
    private String kbName;
    /**
     * 知识库描述
     */
    private String kbDescription;
    /**
     * 存在状态 1：存在，0：删除
     */
    private Integer kbStatus;
}
