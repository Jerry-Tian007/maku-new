package com.ruoyi.gpt.model.vo.kb_file;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
public class KbFileInfoVo {
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
     * 文件下载地址
     */
    private String downloadUrl;
    /**
     * 文件状态
     */
    private String trainStatus;
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

    private String serverFileNameUrl;

    private String downloadUrlMd;

    private List<String> downloadUrlYamls;

    private List<String> downloadUrlImages;
}
