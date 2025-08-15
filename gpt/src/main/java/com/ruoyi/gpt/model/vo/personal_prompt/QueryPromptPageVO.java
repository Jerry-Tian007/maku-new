package com.ruoyi.gpt.model.vo.personal_prompt;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 *  返回分页信息参数类
 *
 * @author Eric
 */
@Data
public class QueryPromptPageVO extends PersonalPromptPageVO {

    /**
     * 查询提示类型
     */
    private String kbName;

    /**
     * 查询提示类型
     */
    private String queryPromptType;




}
