package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 部门ad域信息对象 dept_ad_info
 * 
 * @author Eric
 * @date 2023-05-31
 */
@Data
public class DeptAdInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** ad */
    @Excel(name = "ad")
    private String adUrl;

    /** ad */
    @Excel(name = "ad")
    private String adAdminAccount;

    /** ad */
    @Excel(name = "ad")
    private String adAdminSecret;

    /** ad */
    @Excel(name = "ad")
    private String adSearchBase;

    /** ad */
    @Excel(name = "ad")
    private String adSearchFilter;

    /** 账号后缀 */
    @Excel(name = "账号后缀")
    private String adUsernameSuffix;

    /** roleId */
    @Excel(name = "roleId")
    private String roleIds;

}
