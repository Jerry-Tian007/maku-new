package com.ruoyi.gpt.model.dto.public_prompt;

import java.util.Date;
import lombok.Data;

/**
 *  查询分页信息参数类
 *
 * @author Eric
 */
@Data
public class PublicPromptPageQryDto {
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


}
