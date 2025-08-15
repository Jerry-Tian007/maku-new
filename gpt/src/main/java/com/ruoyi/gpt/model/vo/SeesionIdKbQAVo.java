package com.ruoyi.gpt.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SeesionIdKbQAVo {

    Long id;

    String history;

    String userId;

    String inverter;

    String power;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    String role;

    String region;

    String chatTitle;

    String isAccuracy;
}
