package com.ruoyi.gpt.model.dto.kb_manager;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class KbManagerInsertDto {
    /**
     * 知识库名称
     */
    @NotBlank(message = "知识库名称不能为空")
    private String kbName;
    /**
     * 知识库描述
     */
    private String kbDescription;

    private List<Long> promptIds;
}
