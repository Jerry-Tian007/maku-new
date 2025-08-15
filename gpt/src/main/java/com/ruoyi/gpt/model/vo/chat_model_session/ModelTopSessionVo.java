package com.ruoyi.gpt.model.vo.chat_model_session;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ModelTopSessionVo {
    private String sessionId;
    private String sessionTitle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
}
