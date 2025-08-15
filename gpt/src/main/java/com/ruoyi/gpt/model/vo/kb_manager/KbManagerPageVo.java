package com.ruoyi.gpt.model.vo.kb_manager;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class KbManagerPageVo {
    /**
     * 知识库id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    /**
     * 知识库名称
     */
    private String kbName;
    /**
     * 知识库描述
     */
    private String kbDescription;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 操作时间
     */
    private Date createTime;

    private Integer fileNum;
}
