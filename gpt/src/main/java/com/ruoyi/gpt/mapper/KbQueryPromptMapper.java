package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.vo.personal_prompt.QueryPromptPageVO;

import java.util.List;

/**
 * @author jt
 */
public interface KbQueryPromptMapper {

    /**
     * 查询所有query_prompt_type不为空的记录
     *
     * @param personalPromptPageQryDto
     * @return
     */
    List<QueryPromptPageVO> queryPromptList(PersonalPromptPageQryDto personalPromptPageQryDto);
}
