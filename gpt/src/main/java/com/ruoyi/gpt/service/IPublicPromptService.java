package com.ruoyi.gpt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ruoyi.gpt.model.dto.public_prompt.*;
import com.ruoyi.gpt.model.vo.public_prompt.*;
import com.ruoyi.gpt.mapper.PublicPromptMapper;
import java.util.List;

import com.ruoyi.gpt.model.domain.PublicPromptEntity;

/**
 * 
 *
 * @author Eric
 */
public interface IPublicPromptService {

    /**
     * 创建
     *
     * @param publicPromptAddDto
     * @return
     */
    Integer add(PublicPromptAddDto publicPromptAddDto);

    /**
     * 更新
     *
     * @param publicPromptUpdateDto
     * @return
     */
    Integer update(PublicPromptUpdateDto publicPromptUpdateDto);

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

    Integer save(PublicPromptSaveDto publicPromptSaveDto);
}
