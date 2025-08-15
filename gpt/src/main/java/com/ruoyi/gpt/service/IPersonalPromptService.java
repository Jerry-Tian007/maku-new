package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptAddDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptReleaseDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptUpdateDto;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptInfoVO;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptPageVO;

import java.util.List;

/**
 * @author Eric
 */
public interface IPersonalPromptService {

    /**
     * 创建
     *
     * @param personalPromptAddDto
     * @return
     */
    Integer add(PersonalPromptAddDto personalPromptAddDto);

    /**
     * 更新
     *
     * @param personalPromptUpdateDto
     * @return
     */
    Integer update(PersonalPromptUpdateDto personalPromptUpdateDto);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查询分页信息
     *
     * @param personalPromptPageQryDto
     * @return
     */
    List<PersonalPromptPageVO> pageList(PersonalPromptPageQryDto personalPromptPageQryDto);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    PersonalPromptInfoVO getById(Long id);

    Integer release(PersonalPromptReleaseDto personalPromptReleaseDto);
}
