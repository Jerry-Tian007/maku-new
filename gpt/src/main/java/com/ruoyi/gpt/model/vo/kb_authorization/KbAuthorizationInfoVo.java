package com.ruoyi.gpt.model.vo.kb_authorization;

import lombok.Data;

import java.util.Date;

@Data
public class KbAuthorizationInfoVo {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 操作人
     */
    private String createBy;
    /**
     * 知识库名称
     */
    private String kbName;
}
