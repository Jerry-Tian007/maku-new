package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

@Data
public class KbFileUpdateDto {
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 知识库ids
     */
    private Long[] kbIds;
}
