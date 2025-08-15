package com.ruoyi.gpt.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class HotQuestionAnalyzeVo {

    @NotEmpty(message = "区域不能为空")
    String region;

    @NotEmpty(message = "角色不能为空")
    String role;

    JSONArray regionId;

    JSONArray roleId;

    JSONObject config;

    JSONArray id;

    @NotNull(message = "问题数量不能为空")
    Integer questionNum;

}
