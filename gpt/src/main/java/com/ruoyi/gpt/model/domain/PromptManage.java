package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * promptManage对象 prompt_manage
 * 
 * @author Eric
 * @date 2024-09-02
 */
@Data
public class PromptManage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String model;

    private String group;

    private String title;

    private String content;

    private Long isPublish;

    private Long isSelect;

    private Long orderPublish;

    private Long orderSelect;

    private String introduction;

    private String prologue;

    private String presetProblem;

    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endTime;

    private String delFlag;

}
