package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 知识图谱信息对象 graph_info
 *
 * @author yecao
 * @date 2025-04-09
 */
@Data
public class GraphInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 图谱id
     */
    @Excel(name = "图谱id")
    private String spaceId;

    /**
     * 图谱中文名
     */
    @Excel(name = "图谱中文名")
    private String cnName;

    /**
     * 简介
     */
    @Excel(name = "简介")
    private String des;

    /**
     * $column.columnComment
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /**
     * 同步时间
     */
    private Date syncTime;

}
