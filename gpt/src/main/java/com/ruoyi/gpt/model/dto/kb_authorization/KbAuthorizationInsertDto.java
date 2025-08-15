package com.ruoyi.gpt.model.dto.kb_authorization;

import lombok.Data;

@Data
public class KbAuthorizationInsertDto {
    /**
     * 角色ids
     */
    private Long roleId;
    /**
     * 知识库ids
     */
    private Long[] kbIds;
}
