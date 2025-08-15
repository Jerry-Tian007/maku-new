package com.ruoyi.gpt.model.vo.kb_file;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class KbFilePageVo {
    /**
     * 文件id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * 文件名称
     */
    private String originalFileName;
    /**
     * 文件状态
     */
    private String trainStatus;
    /**
     * 失败原因
     */
    private String trainErrorReason;
    /**
     * 所属知识库名称
     */
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
    private String suffix;
    /**
     * 上传时间
     */
    private Date createTime;
    /**
     * 训练时间
     */
    private Date trainTime;

    private Date trainFinishTime;

    private String trainDuration;

    private String fileToken;

    private String fileSize;

    private String filePage;

    private String status;

    private String jsonCosUrl;

    /**
     * 书籍级别
     */
    private Integer bookLevel;

}
