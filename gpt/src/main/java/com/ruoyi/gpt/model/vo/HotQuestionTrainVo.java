package com.ruoyi.gpt.model.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class HotQuestionTrainVo {
    @NotEmpty(message = "训练参数不能为空")
    @Valid
    private List<HotQuestionTrainItemVo> list;
}
