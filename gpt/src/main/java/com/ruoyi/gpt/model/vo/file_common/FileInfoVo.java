package com.ruoyi.gpt.model.vo.file_common;

import lombok.Data;

/**
 * 文件基本信息
 *
 * @author tiankk
 */
@Data
public class FileInfoVo {
    /**
     * 原文件名全称，包含后缀
     */
    private String originalFileFullName;
    /**
     * 原文件名称，没有后缀
     */
    private String originalFileName;

    /**
     * 后缀，文件类型
     */
    private String suffix;

    /**
     * 文件下载路径
     */
    private String downloadUrl;

    /**
     * 服务端文件名，没有后缀
     */
    private String serverFileName;

    /**
     * 服务端文件名全称，包含后缀
     */
    private String serverFileFullName;

    /**
     * 服务端文件路径
     */
    private String serverFileNameUrl;

    /**
     * 文件大小
     */
    private Double fileSize;

    /**
     * 文件页数
     */
    private Integer filePage;
    /**
     * 文件上传模块，用于获取存储地址
     */
    private String purpose;
}
