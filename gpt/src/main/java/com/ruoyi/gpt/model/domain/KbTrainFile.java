package com.ruoyi.gpt.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 知识库训练文件对象 kb_train_file
 * 
 * @author Eric
 * @date 2023-09-19
 */
@Data
public class KbTrainFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件id */
    private String fileId;

    /** 文件url */
    @Excel(name = "文件url")
    private String fileUrl;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 是否表单识别 */
    @Excel(name = "是否表单识别")
    private String isIdentifyForm;

    /** 所输入id */
    @Excel(name = "所输入id")
    private Long ownerId;

    /** 所属人姓名 */
    @Excel(name = "所属人姓名")
    private String ownerName;

    /** 行业id */
    @Excel(name = "行业id")
    private Long businessId;

    /** 行业名称 */
    @Excel(name = "行业名称")
    private String businessName;

    /** 训练时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Excel(name = "训练时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trainTime;

    /** 训练状态 */
    @Excel(name = "训练状态")
    private String trainStatus;

    private String trainFlow;

}
