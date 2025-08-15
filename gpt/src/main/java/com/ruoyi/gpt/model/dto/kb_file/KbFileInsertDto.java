package com.ruoyi.gpt.model.dto.kb_file;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class KbFileInsertDto {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 上传文件原文件名
     */
    private String originalFileName;

    /**
     * 源文件名称，没有后缀
     */
    private String fileName;

    /**
     * 后缀，文件类型
     */
    private String suffix;

    /**
     * 文件下载路径
     */
    private String downloadUrl;

    /**
     * 服务端文件名
     */
    private String serverFileName;

    /**
     * 服务端文件路径
     */
    private String serverFileNameUrl;

    /**
     * 训练状态
     */
    private String trainStatus;

    /**
     * 训练时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trainTime;

    /**
     * 训练失败原因
     */
    private String trainErrorReason;

    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件页数
     */
    private Integer filePage;
}
