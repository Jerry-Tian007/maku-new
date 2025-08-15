package com.ruoyi.gpt.model.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * GPT消费概览对象 model_token_consumption_overview
 *
 * @author yecao
 * @date 2023-04-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelTokenConsumptionOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private long id;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** toten总消耗量 */
    @Excel(name = "toten总消耗量")
    private Integer totalTokenConsumption;

    /** 总消耗金额 */
    @Excel(name = "总消耗金额")
    private BigDecimal totalConsumptionAmount;

    /** 当前账单消耗token量 */
    @Excel(name = "当前账单消耗token量")
    private Integer currentBillTokenConsumption;

    /** 当前账单消耗金额 */
    @Excel(name = "当前账单消耗金额")
    private BigDecimal currentBillConsumptionAmount;

    /**
     * 最高消费限制
     */
    private BigDecimal maxConsumptionAmount;

    private String overviewDetail;

    /**
     * 账单类型
     */
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastGenerateBillTime;
}
