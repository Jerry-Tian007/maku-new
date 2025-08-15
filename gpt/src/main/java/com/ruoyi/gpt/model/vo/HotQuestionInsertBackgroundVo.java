package com.ruoyi.gpt.model.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class HotQuestionInsertBackgroundVo {

    @NotEmpty(message = "问题不能为空")
    private String question;

    @NotEmpty(message = "答案不能为空")
    private String answer;

    @NotEmpty(message = "地区不能为空")
    private String region;

    @NotEmpty(message = "角色不能为空")
    private String role;

    @NotEmpty(message = "语言不能为空")
    private String language;

    private String create;

    private JSONObject qa;
}
