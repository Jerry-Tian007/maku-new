package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbPersonalPromptMapper;
import com.ruoyi.gpt.mapper.KbPublicPromptMapper;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import com.ruoyi.gpt.model.domain.PublicPromptEntity;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptAddDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptPageQryDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptSaveDto;
import com.ruoyi.gpt.model.dto.public_prompt.PublicPromptUpdateDto;
import com.ruoyi.gpt.model.vo.public_prompt.PublicPromptInfoVO;
import com.ruoyi.gpt.model.vo.public_prompt.PublicPromptPageVO;
import com.ruoyi.gpt.service.IKbPublicPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eric
 */
@Service
public class KbPublicPromptServiceImpl implements IKbPublicPromptService {

    @Autowired
    private KbPublicPromptMapper kbPublicPromptMapper;

    @Autowired
    private KbPersonalPromptMapper kbPersonalPromptMapper;

    /**
     * 创建
     *
     * @param publicPromptAddDto
     * @return
     */
    @Override
    public Integer add(PublicPromptAddDto publicPromptAddDto) {
        PublicPromptEntity publicPromptEntity = BeanConvertUtil.conver(publicPromptAddDto, PublicPromptEntity.class);
        publicPromptEntity.setCreateBy(SecurityUtils.getUsername());
        publicPromptEntity.setCreateTime(DateUtils.getNowDate());
        return kbPublicPromptMapper.add(publicPromptEntity);
    }

    /**
     * 更新
     *
     * @param publicPromptUpdateDto
     * @return
     */
    @Override
    public Integer update(PublicPromptUpdateDto publicPromptUpdateDto) {
        PublicPromptEntity publicPromptEntity = BeanConvertUtil.conver(publicPromptUpdateDto, PublicPromptEntity.class);
        publicPromptEntity.setUpdateBy(SecurityUtils.getUsername());
        publicPromptEntity.setUpdateTime(DateUtils.getNowDate());
        return kbPublicPromptMapper.updateById(publicPromptEntity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        return kbPublicPromptMapper.deleteById(id);
    }

    /**
     * 查询分页信息
     *
     * @param publicPromptPageQryDto
     * @return
     */
    @Override
    public List<PublicPromptPageVO> pageList(PublicPromptPageQryDto publicPromptPageQryDto) {
        return kbPublicPromptMapper.pageList(publicPromptPageQryDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public PublicPromptInfoVO getById(Long id) {
        return kbPublicPromptMapper.getById(id);
    }

    @Override
    public Integer save(PublicPromptSaveDto publicPromptSaveDto) {
        PublicPromptInfoVO byId = kbPublicPromptMapper.getById(publicPromptSaveDto.getId());
        PersonalPromptEntity personalPromptEntity = new PersonalPromptEntity();
        personalPromptEntity.setReleaseStatus("0");
        personalPromptEntity.setCreateBy(SecurityUtils.getUsername());
        personalPromptEntity.setCreateTime(DateUtils.getNowDate());
        personalPromptEntity.setPrompt(byId.getPrompt());
        personalPromptEntity.setName(byId.getName());
        personalPromptEntity.setUserId(SecurityUtils.getUserId());
        return kbPersonalPromptMapper.add(personalPromptEntity);
    }
}
