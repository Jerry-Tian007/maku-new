package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 私域错误日志对象 kb_error_info
 * 
 * @author Eric
 * @date 2023-07-11
 */
@Data
public class KbErrorInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 错误信息 */
    @Excel(name = "错误信息")
    private String errorInfo;

    @Excel(name = "模型类型")
    private String type;

}
