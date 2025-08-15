package com.ruoyi.gpt.mapper;

import org.springframework.stereotype.Service;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.dto.public_prompt.*;
import com.ruoyi.gpt.model.vo.public_prompt.*;
import com.ruoyi.gpt.model.domain.PublicPromptEntity;
import java.util.List;

/**
 * 
 *
 * @author Eric
 */
public interface PublicPromptMapper{

        /**
         * 创建
         *
         * @param publicPromptEntity
         * @return
         */
        @GenerateUniqueId
        Integer add(PublicPromptEntity publicPromptEntity);

        /**
         * 更新
         *
         * @param publicPromptEntity
         * @return
         */
        Integer updateById(PublicPromptEntity publicPromptEntity);

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
         * @param publicPromptPageQryDto
         * @return
         */
        List<PublicPromptPageVO> pageList(PublicPromptPageQryDto publicPromptPageQryDto);

        /**
         * 查询详情
         *
         * @param id
         * @return
         */
        PublicPromptInfoVO getById(Long id);

        Integer deleteByPersonalPromptId(Long id);

        PublicPromptEntity selectByPersonalPromptId(Long id);

        PublicPromptEntity selectByName(String name);
}
