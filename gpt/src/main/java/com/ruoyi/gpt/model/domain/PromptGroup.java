package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * promptGroup对象 prompt_group
 * 
 * @author Eric
 * @date 2024-09-02
 */
@Data
public class PromptGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String icon;

    private Long isPublish;

    private Long orderPublish;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endTime;

}
