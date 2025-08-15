package com.ruoyi.gpt.model.vo.kb_file_manager_rel;

import lombok.Data;

@Data
public class FileIdToKbNameVo {
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 知识库名称
     */
    private String kbName;
    /**
     * 知识库Id
     */
    private String kbId;
}
