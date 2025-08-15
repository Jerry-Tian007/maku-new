package com.ruoyi.gpt.model.dto.public_prompt;


import lombok.Data;

/**
 *  更新
 *
 * @author Eric
 */
@Data
public class PublicPromptUpdateDto {
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

