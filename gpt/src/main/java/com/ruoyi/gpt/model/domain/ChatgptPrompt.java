package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * gpt提示语对象 chatgpt_prompt
 * 
 * @author Eric
 * @date 2023-05-23
 */
@Data
public class ChatgptPrompt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 正文 */
    @Excel(name = "正文")
    private String body;

    /** 角色 */
    @Excel(name = "角色")
    private String rule;

}
