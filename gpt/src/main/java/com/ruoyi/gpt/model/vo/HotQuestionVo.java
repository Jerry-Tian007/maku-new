package com.ruoyi.gpt.model.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.gpt.model.domain.HotQuestion;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class HotQuestionVo extends HotQuestion {

    private Date startTime;

    private Date endTime;

    //传给前端防止前端js 识别不了 过长的时间戳作为id
    private String stringId;

}
