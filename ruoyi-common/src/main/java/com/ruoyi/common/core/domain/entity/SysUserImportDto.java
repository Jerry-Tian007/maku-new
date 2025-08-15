package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;

import java.util.Date;
import java.util.List;

public class SysUserImportDto {
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @Excel(name = "归属部门编号", type = Excel.Type.IMPORT, english = "Belonging department number")
    private Long deptId;

    /** 用户账号 */
    @Excel(name = "用户名称（登录账号名）", english = "User Name (Login Account Name)")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称（系统显示名）", english = "User nickname (system display name)")
    private String nickName;

    /** 密码 */
    @Excel(name = "用户密码", english = "Password")
    private String password;


    /** 角色ID */
    @Excel(name = "用户角色（编号1;编号2）", english = "User Role (Number 1; Number 2)")
    private String roleIds;


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}
