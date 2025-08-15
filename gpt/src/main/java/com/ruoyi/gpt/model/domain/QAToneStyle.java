package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 语气风格对象 q_a_tone_style
 * 
 * @author Eric
 * @date 2023-08-21
 */
@Data
public class QAToneStyle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 模型 */
    @Excel(name = "模型")
    private String model;

    /** 语气 */
    @Excel(name = "语气")
    private String tone;

    /** 风格 */
    @Excel(name = "风格")
    private String style;

    /** 温度 */
    @Excel(name = "温度")
    private String temperature;

}
