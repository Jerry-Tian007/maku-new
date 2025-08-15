package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class KbAuthorization extends BaseEntity {
    /**
     * 主键
     */
    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 知识库id
     */
    private Long kbId;
}
