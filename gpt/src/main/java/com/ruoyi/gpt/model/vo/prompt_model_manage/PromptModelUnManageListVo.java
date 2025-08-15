package com.ruoyi.gpt.model.vo.prompt_model_manage;

import lombok.Data;

@Data
public class PromptModelUnManageListVo {
    private Long manageId;
    private String model;
    private String group;
    private String title;
    private String introduction;
//    private Long modelOrder;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private String createTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private String updateTime;
//    private String createBy;
//    private String updateBy;
}
