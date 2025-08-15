package com.ruoyi.gpt.model.vo.kb_authorization;

import lombok.Data;

import java.util.Date;

@Data
public class KbAuthorizationPageSubVo {
    /**
     * 角色id
     */
    private Long roleId;
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
