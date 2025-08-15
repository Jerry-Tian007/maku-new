package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptAddDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptPageQryDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptSaveDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptUpdateDto;
import com.ruoyi.gpt.model.vo.public_prompt.PublicPromptInfoVO;
import com.ruoyi.gpt.model.vo.public_prompt.PublicPromptPageVO;

import java.util.List;

/**
 * 
 *
 * @author Eric
 */
public interface IKbPublicPromptService {

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
