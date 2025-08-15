package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

/**
 * 知识库文件书籍级别更新DTO
 */
@Data
public class KbFileBookLevelUpdateDto {
    /**
     * 文件id
     */
    private Long fileId;
    
    /**
     * 书籍级别
     */
    private Integer bookLevel;
}