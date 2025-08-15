package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptReleaseDto;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptInfoVO;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Eric
 */
public interface KbPersonalPromptMapper {

    /**
     * 创建
     *
     * @param personalPromptEntity
     * @return
     */
    @GenerateUniqueId
    Integer add(PersonalPromptEntity personalPromptEntity);

    /**
     * 更新
     *
     * @param personalPromptEntity
     * @return
     */
    Integer updateById(PersonalPromptEntity personalPromptEntity);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Long id);

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

    List<PersonalPromptInfoVO> kbManagerSelectPromptList(@Param("kbManagerId") Long kbManagerId, @Param("userId") Long userId);

    Integer updateByIds(@Param("promptEntity") PersonalPromptEntity promptEntity, @Param("promptIds") List<Long> promptIds);
}
