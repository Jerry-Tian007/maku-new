package com.ruoyi.gpt.model.domain;


import lombok.Data;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 *  Entity
 *
 * @author Eric
 */
@Data
public class PersonalPromptEntity extends BaseEntity{
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

    /**
     * 阶段
     */
    private String phrase;

    /**
     * 查询提示类型
     */
    private String queryPromptType;

    private String releaseStatus;

    private Long kbManagerId;

}
