package com.ruoyi.gpt.model.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 用户blob文件对象 user_blob
 *
 * @author Eric
 * @date 2023-05-29
 */
@Data
public class KbFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

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
     * blob
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
    private Double fileSize;
    /**
     * 文件页数
     */
    private Integer filePage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trainFinishTime;

    private String trainDuration;

    private String fileToken;

    /**
     * chunk存储的url
     */
    private String jsonCosUrl;

    private String status;

    private String downloadUrlMd;

    /**
     * 书籍级别
     */
    private Integer bookLevel;

}
