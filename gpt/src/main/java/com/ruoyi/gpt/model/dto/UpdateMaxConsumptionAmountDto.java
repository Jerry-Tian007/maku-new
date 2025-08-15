package com.ruoyi.gpt.model.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class UpdateMaxConsumptionAmountDto extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    private String userName;

    /**
     * 概览id
     */
    private Long overviewId;

    /**
     * 金额
     */
    private String consumptionAmount;

    /**
     * 操作类型 decrease 减少 increase 增加
     */
    private String operationType;

}
