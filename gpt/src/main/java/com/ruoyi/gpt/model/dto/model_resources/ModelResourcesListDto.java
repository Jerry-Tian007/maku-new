package com.ruoyi.gpt.model.dto.model_resources;

import lombok.Data;

import java.util.Date;

@Data
public class ModelResourcesListDto {

    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型类型
     */
    private String type;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;


}
