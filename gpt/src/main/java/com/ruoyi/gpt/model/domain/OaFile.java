package com.ruoyi.gpt.model.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OaFile {

    /**
     * 所有者名称
     */
    @NotNull(message = "ownerName不能为null")
    private String ownerName;
    /**
     * 所有者id
     */
    @NotNull(message = "ownerId不能为null")
    private Long ownerId;
    /**
     * 文件唯一标识
     */
    @NotNull
    @NotBlank(message = "fileId不能为空")
    private String fileId;
    /**
     * 文件存储url（需要可以直接下载的链接）
     */
    @NotNull
    @NotBlank(message = "fileUrl不能为空")
    private String fileUrl;
    /**
     * 文件名
     */
    @NotNull
    @NotBlank(message = "fileName不能为空")
    private String fileName;
    /**
     * 文件后缀
     */
    @NotNull
    @NotBlank(message = "fileSuffix不能为空")
    private String fileSuffix;
    /**
     * 是否需要识别表格(yes 是，no 否)
     */
    @NotNull
    @NotBlank(message = "isIdentifyForm不能为空")
    private String isIdentifyForm;
}
