package com.ruoyi.gpt.model.dto.kb_manager;

import lombok.Data;

import java.util.List;

@Data
public class KbManagerUpdateDto {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 知识库名称
     */
    private String kbName;
    /**
     * 知识库描述
     */
    private String kbDescription;

    private List<Long> promptIds;
}
