package com.ruoyi.gpt.model.vo.kb_authorization;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class KbAuthorizationPageVo {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 知识库名称
     */
    private String kbName;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 操作人
     */
    private String createBy;
}
