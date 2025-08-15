package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * gpt总结对象 chatgpt_summary
 * 
 * @author Eric
 * @date 2023-09-26
 */
@Data
public class ChatgptSummary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** userId */
    @Excel(name = "identityId")
    private String identityId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 总结 */
    @Excel(name = "总结")
    private String summary;

    /** 业务名称 */
    @Excel(name = "业务名称")
    private String businessName;

    @Excel(name = "历史记录")
    private String history;

}
