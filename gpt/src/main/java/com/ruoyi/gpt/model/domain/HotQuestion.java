package com.ruoyi.gpt.model.domain;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * 热点问题对象 hot_question
 *
 * @author lijw
 * @date 2023-11-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HotQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 提问 */
    @Excel(name = "提问")
    private String question;

    /** 回答 */
    @Excel(name = "回答")
    private String answer;

    /** 国家 */
    @Excel(name = "地区")
    private String region;

    /** 角色 */
    @Excel(name = "角色")
    private String role;

    /** 准确性评价 */
    @Excel(name = "准确性评价")
    private String isAccuracy;

    /** 训练状态 */
    @Excel(name = "训练状态")
    private String status;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createBy;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @Excel(name = "语言")
    private String language;

    @Excel(name = "记录编号")
    private Long recordId;

    @Excel(name = "训练时间")
    private Date trainTime;

    @Excel(name = "训练流")
    private String trainFlow;

}
