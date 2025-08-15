package com.ruoyi.gpt.model.domain;


import lombok.Data;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 *  Entity
 *
 * @author Eric
 */
@Data
public class PublicPromptEntity extends BaseEntity{
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 提示词
     */
    private String prompt;

    private Long personalPromptId;

}
