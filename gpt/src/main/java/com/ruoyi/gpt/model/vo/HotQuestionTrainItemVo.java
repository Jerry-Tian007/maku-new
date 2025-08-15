package com.ruoyi.gpt.model.vo;

import cn.hutool.json.JSONArray;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class HotQuestionTrainItemVo {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "区域不能为空")
    private String regionList;

    @NotEmpty(message = "角色不能为空")
    private String roleList;

    @NotNull(message = "recordId不能为空")
    private Long recordId;

    @NotEmpty(message = "question不能为空")
    private String question;

    @NotEmpty(message = "answer不能为空")
    private String answer;

    private JSONArray roleId;

    private JSONArray regionId;
}
