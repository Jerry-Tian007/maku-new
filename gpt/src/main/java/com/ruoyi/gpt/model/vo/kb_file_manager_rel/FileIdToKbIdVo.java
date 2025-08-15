package com.ruoyi.gpt.model.vo.kb_file_manager_rel;

import lombok.Data;

@Data
public class FileIdToKbIdVo {
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 知识库id
     */
    private Long kbId;
}
