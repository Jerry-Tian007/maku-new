package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * chatgpt对象 chatgpt_q_a_record
 *
 * @author Eric
 * @date 2023-04-18
 */
@Data
public class ChatgptQARecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private long id;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;

    /**
     * 问
     */
    @Excel(name = "问")
    private String question;

    /**
     * 答
     */
    @Excel(name = "答")
    private String answer;

    /**
     * 本次token使用量
     */
    @Excel(name = "token使用量")
    private Integer tokenUsage;

    /**
     * 使用的模型
     */
    private String model;

    /**
     * 是否准确
     */
    private String isAccuracy;

    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String endTime;

    private String sessionId;

    private Long manageId;

    private String manageTitle;

    private String isError;

    private String reasoningContent;

    private String linkAndTitle;

    /***
     * 所用到的Token数量
     */
    private Integer completionTokens;

}
