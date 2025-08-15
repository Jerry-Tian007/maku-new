package com.ruoyi.gpt.model.vo.kb_file;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import lombok.Data;

import java.util.Date;

@Data
public class KbFileExportVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * 文件名称
     */
    @Excel(name = "文件名称",english = "File Name")
    private String originalFileName;
    /**
     * 文件状态
     */
    @Excel(name = "文件状态" ,english = "File status", readConverterExp = "untrained=未训练,training=训练中,success=训练完成,error=训练失败,cancelFailed=取消失败，deleteFailed=删除失败"
    ,englishReadConverterExp = "untrained=Untrained,training=Training,success=Training success,error=Training failed,cancelFailed=Cancel failed,deleteFailed=Delete failed")
    private String trainStatus;
    /**
     * 失败原因
     */
    private String trainErrorReason;
    /**
     * 所属知识库名称
     */
    @Excel(name = "所属知识库名称" ,english = "Knowledge Base Name")
    private String kbName;
    /**
     * 所属知识库ID
     */
    private String kbId;
    /**
     * 上传人
     */

    private String createBy;
    /**
     * 文件类型
     */
    @Excel(name = "文件类型",english = "File Type")
    private String suffix;
    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "上传时间",english = "Upload Time", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 训练时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "训练时间",english = "Train Time", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date trainTime;

    /**
     * 训练完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "训练完成时间",english = "Train Finsh Time", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date trainFinishTime;

    /**
     * 训练时长
     */
    @Excel(name = "训练时长(秒)" ,english = "Train Duration(second)")
    private String trainDuration;
}
