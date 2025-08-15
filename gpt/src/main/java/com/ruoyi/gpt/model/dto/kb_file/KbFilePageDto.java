package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class KbFilePageDto {
    /**
     * 文档名称
     */
    private String fileName;
    /**
     * 文档格式
     */
    private String suffix;
    /**
     * 上传时间范围 开始时间点
     */
    private Date startTime;
    /**
     * 上传时间范围 结束时间点
     */
    private Date endTime;
    /**
     * 训练时间范围 开始
     */
    private Date trainStartTime;
    /**
     * 训练时间范围 结束
     */
    private Date trainEndTime;
    /**
     * 所属知识库
     */
    private List<Long> kbIds;

    /**
     * 知识库名称
     */
    private String kbName;

    /**
     * 训练状态
     */
    private String trainStatus;

    private String status;

    private Integer bookLevel;
}
