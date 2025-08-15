package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 场景问答会话对象 chatgpt_q_a_record_scene_session
 * 
 * @author Eric
 * @date 2024-10-08
 */
@Data
public class ChatgptQARecordSceneSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 会话id */
    @Excel(name = "会话id")
    private String sessionId;

    /** 会话标题 */
    @Excel(name = "会话标题")
    private String sessionTitle;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    private String delFlag;

    private Long manageId;

}
