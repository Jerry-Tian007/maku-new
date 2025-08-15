package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.PersonalPromptMapper;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import com.ruoyi.gpt.service.IPublicPromptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.public_prompt.*;
import com.ruoyi.gpt.model.vo.public_prompt.*;
import com.ruoyi.gpt.mapper.PublicPromptMapper;
import com.ruoyi.gpt.model.domain.PublicPromptEntity;
import java.util.List;

/**
 * 
 *
 * @author Eric
 */
@Service
public class PublicPromptServiceImpl implements IPublicPromptService {

    @Autowired
    private PublicPromptMapper publicPromptMapper;

    @Autowired
    private PersonalPromptMapper personalPromptMapper;
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
        return publicPromptMapper.add(publicPromptEntity);
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
        return publicPromptMapper.updateById(publicPromptEntity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        return publicPromptMapper.deleteById(id);
    }

    /**
     * 查询分页信息
     *
     * @param publicPromptPageQryDto
     * @return
     */
    @Override
    public List<PublicPromptPageVO> pageList(PublicPromptPageQryDto publicPromptPageQryDto) {
        return publicPromptMapper.pageList(publicPromptPageQryDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public PublicPromptInfoVO getById(Long id) {
        return publicPromptMapper.getById(id);
    }

    @Override
    public Integer save(PublicPromptSaveDto publicPromptSaveDto) {
        PublicPromptInfoVO byId = publicPromptMapper.getById(publicPromptSaveDto.getId());
        PersonalPromptEntity personalPromptEntity = new PersonalPromptEntity();
        personalPromptEntity.setReleaseStatus("0");
        personalPromptEntity.setCreateBy(SecurityUtils.getUsername());
        personalPromptEntity.setCreateTime(DateUtils.getNowDate());
        personalPromptEntity.setPrompt(byId.getPrompt());
        personalPromptEntity.setName(byId.getName());
        personalPromptEntity.setUserId(SecurityUtils.getUserId());
        return personalPromptMapper.add(personalPromptEntity);
    }
}
