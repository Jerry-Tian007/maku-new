package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 模型场景管理对象 prompt_model_manage
 * 
 * @author Eric
 * @date 2024-09-19
 */
@Data
public class PromptModelManage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 模型 */
    @Excel(name = "模型")
    private String model;

    /** 场景id */
    @Excel(name = "场景id")
    private Long manageId;

    /** 模型排序 */
    @Excel(name = "模型排序")
    private Long modelOrder;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

}
