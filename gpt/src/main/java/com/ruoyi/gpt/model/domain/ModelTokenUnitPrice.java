package com.ruoyi.gpt.model.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型单位token价格对象 model_token_unit_price
 * 
 * @author yecao
 * @date 2023-04-28
 */
@Data
public class ModelTokenUnitPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private long id;

    /** 模型 */
    @Excel(name = "模型")
    private String model;

    /** 该模型token单价 */
    @Excel(name = "该模型token单价")
    private BigDecimal unitModelTokenPrice;

}
