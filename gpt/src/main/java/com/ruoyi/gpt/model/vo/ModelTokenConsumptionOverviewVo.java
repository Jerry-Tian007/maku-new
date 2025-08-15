package com.ruoyi.gpt.model.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import lombok.Data;

@Data
public class ModelTokenConsumptionOverviewVo extends ModelTokenConsumptionOverview {

    @Excel(name = "用户名")
    private String userName;

    @Excel(name = "部门")
    private String deptName;
}
