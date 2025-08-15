package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptAddDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptReleaseDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptUpdateDto;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptInfoVO;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptPageVO;
import com.ruoyi.gpt.model.vo.query_prompt.QueryPromptInfoVO;
import com.ruoyi.gpt.model.vo.query_prompt.QueryPromptPageVO;

import java.util.List;

/**
 * 
 *
 * @author Eric
 */
public interface IKbQueryPromptService {


    /**
     * 知识库数据列表查询
     *
     * @param kbManagerPageDto
     * @return
     */
    List<QueryPromptInfoVO> list(QueryPromptPageVO kbManagerPageDto);



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

    List<PersonalPromptInfoVO> kbManagerSelectPromptList(Long kbManagerId);
}
