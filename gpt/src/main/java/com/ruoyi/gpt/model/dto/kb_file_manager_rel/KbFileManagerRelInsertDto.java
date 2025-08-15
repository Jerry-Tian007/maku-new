package com.ruoyi.gpt.model.dto.kb_file_manager_rel;

import lombok.Data;

@Data
public class KbFileManagerRelInsertDto {
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 所属知识库ids
     */
    private Long[] kbIds;
}
