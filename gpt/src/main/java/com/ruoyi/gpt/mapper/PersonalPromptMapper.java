package com.ruoyi.gpt.mapper;

import org.springframework.stereotype.Service;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.dto.personal_prompt.*;
import com.ruoyi.gpt.model.vo.personal_prompt.*;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import java.util.List;

/**
 * 
 *
 * @author Eric
 */
public interface PersonalPromptMapper{

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
}
