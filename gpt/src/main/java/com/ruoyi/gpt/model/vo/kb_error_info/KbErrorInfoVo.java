package com.ruoyi.gpt.model.vo.kb_error_info;

import lombok.Data;

import java.util.Date;

@Data
public class KbErrorInfoVo {
    private Long id;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 备注
     */
    private String remark;
    /**
     * 类型
     */
    private String type;
}
