package com.ruoyi.gpt.model.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class HotQuestionEditBackgroundVo {

    @NotNull(message = "记录Id不能为空")
    Long recordId;

    String region;

    String role;

    String question;

    String answer;

    @NotEmpty(message = "状态不能为空")
    String status;

    @NotEmpty(message = "语言不能为空")
    String language;

    JSONObject qa;
}
