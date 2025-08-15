package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

@Data
public class KbFileUpdateBatchDto {
    /**
     * 文件ids
     */
    Long[] fileIds;
    /**
     * 知识库ids
     */
    Long[] kbIds;
}
