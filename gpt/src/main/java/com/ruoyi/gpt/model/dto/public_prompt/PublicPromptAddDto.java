package com.ruoyi.gpt.model.dto.public_prompt;


import lombok.Data;

/**
 *  添加
 *
 * @author Eric
 */
@Data
public class PublicPromptAddDto {
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
