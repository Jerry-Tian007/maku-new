package com.ruoyi.gpt.model.vo.kb_file;

import lombok.Data;

@Data
public class KbFileTrainVo {
    private Long fileId;
    private String fileName;
    private String suffix;
    private String blobUrl;
    private String trainStatus;
}
