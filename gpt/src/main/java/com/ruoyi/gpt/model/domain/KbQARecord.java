package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 知识库问答记录对象 kb_q_a_record
 *
 * @author Eric
 * @date 2023-07-11
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KbQARecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 题问
     */
    @Excel(name = "提问")
    private String question;

    /**
     * 回答
     */
    @Excel(name = "回答")
    private String answer;

    /**
     * 接口返回
     */
    @Excel(name = "接口返回")
    private String response;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * yes=准确,no=不准确
     */
    @Excel(name = "是否准确", readConverterExp = "yes=准确,no=不准确")
    private String isAccuracy;

    /**
     * yes 已解决 no 未解决
     */
    private String isSolve;
    /**
     * 会话标识
     */
    @Excel(name = "会话标识")
    private String sessionId;

    /**
     * 会话标题
     */
    @Excel(name = "会话标题")
    private String chatTitle;

    /**
     * 问题向量
     */
    private String questionVector;

    /**
     * token使用量
     */
    private Integer tokenUsage;

    /**
     * 创建者
     */
    @Excel(name = "提问人")
    private String createBy;

    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 知识库名称
     */
    @Excel(name = "知识库名称")
    private String kbName;

    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    private String fileName;

    /**
     * 模型类型
     */
    private String modelType;

    private Long modelTypeId;

    /**
     * 思考过程
     */
    private String reasoningContent;

    /**
     *
     */
    private String chunkSearch;

    /**
     * 多个kbId则使用逗号分割
     */
    private String kbId;

    /***
     * 所用到的Token数量
     */
    private Integer completionTokens;

    /**
     *
     */
    private String rewrittenQuery;

    /**
     *
     */
    private String timeEstimation;

}
