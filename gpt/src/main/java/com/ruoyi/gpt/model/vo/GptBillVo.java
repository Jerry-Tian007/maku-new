package com.ruoyi.gpt.model.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.gpt.model.domain.GptBill;
import lombok.Data;

@Data
public class GptBillVo extends GptBill {

    @Excel(name = "用户名")
    private String userName;

    @Excel(name = "部门名称")
    private String deptName;

}
