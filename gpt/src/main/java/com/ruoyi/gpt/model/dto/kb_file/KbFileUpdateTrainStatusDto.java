package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

import java.util.Date;

@Data
public class KbFileUpdateTrainStatusDto {
    /**
     * id
     */
    private Long[] id;
    /**
     * 训练时间
     */
    private Date trainTime;

    private Date trainFinishTime;
    /**
     * 训练状态
     */
    private String trainStatus;

    private String trainDuration;
    /**
     * 训练失败原因
     */
    private String trainErrorReason;

    private String fileToken;

    private String jsonCosUrl;

    private String downloadUrlMd;
}
