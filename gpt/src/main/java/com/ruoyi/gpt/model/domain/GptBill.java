package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * GPT账单对象 gpt_bill
 *
 * @author yecao
 * @date 2023-04-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GptBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private long id;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 账单金额 */
    @Excel(name = "账单金额")
    private BigDecimal amountOfBill;

    /**
     * 账单详情
     */
    @Excel(name = "账单详情")
    private String amountOfBillDetail;

    /** unpaid: */
    @Excel(name = "订单状态", readConverterExp = "unpaid=未支付,paid=已支付")
    private String billingStatus;

}
