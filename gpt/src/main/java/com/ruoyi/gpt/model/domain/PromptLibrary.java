package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * promptLibrary对象 prompt_library
 * 
 * @author Eric
 * @date 2023-07-31
 */
@Data
public class PromptLibrary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

}
