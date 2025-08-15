package com.ruoyi.gpt.model.dto.personal_prompt;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *  查询分页信息参数类
 *
 * @author Eric
 */
@Data
public class PersonalPromptPageQryDto {
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

    private List<Long> kbManagerIds;

    /**
     * 查询提示类型
     */
    private String queryPromptType;
    /**
     * 查询提示类型
     */
    private String kbName;

}
