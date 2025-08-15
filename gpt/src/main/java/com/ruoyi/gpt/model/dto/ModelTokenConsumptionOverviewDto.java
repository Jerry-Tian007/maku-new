package com.ruoyi.gpt.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import lombok.Data;

import java.util.Date;

@Data
public class ModelTokenConsumptionOverviewDto extends ModelTokenConsumptionOverview {

    private String deptName;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

}
