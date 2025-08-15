package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;


@Data
public class UserKbHistory extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 会话
     */
    private String sessionMark;
    /**
     * 会话名字
     */
    private String sessionName;
    /**
     * 状态
     */
    private Integer status;
}
