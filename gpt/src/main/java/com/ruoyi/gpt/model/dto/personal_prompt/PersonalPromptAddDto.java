package com.ruoyi.gpt.model.dto.personal_prompt;


import lombok.Data;

/**
 *  添加
 *
 * @author Eric
 */
@Data
public class PersonalPromptAddDto {
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
    /**
     * 用户id
     */
    private Long userId;


    private String releaseStatus;

}
