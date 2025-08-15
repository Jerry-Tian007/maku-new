package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * chat对象 user_chat_history
 *
 * @author yecao
 * @date 2023-05-19
 */
@Data
public class UserChatHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 对话历史 */
    @Excel(name = "对话历史")
    private String history;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 问答时使用的prompt
     */
    private String promptId;

}
