package com.ruoyi.gpt.model.dto.kb_file_manager_rel;

import lombok.Data;

@Data
public class KbFileManagerRelUpdateBatchDto {
    /**
     * 文件ids
     */
    private Long[] fileIds;
    /**
     * 知识库ids
     */
    private Long[] kbIds;
}
