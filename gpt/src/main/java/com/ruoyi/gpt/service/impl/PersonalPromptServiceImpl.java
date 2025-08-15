package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.PublicPromptMapper;
import com.ruoyi.gpt.model.domain.PublicPromptEntity;
import com.ruoyi.gpt.service.IPersonalPromptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.personal_prompt.*;
import com.ruoyi.gpt.model.vo.personal_prompt.*;
import com.ruoyi.gpt.mapper.PersonalPromptMapper;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author Eric
 */
@Service
public class PersonalPromptServiceImpl implements IPersonalPromptService {

    @Autowired
    private PersonalPromptMapper personalPromptMapper;

    @Autowired
    private PublicPromptMapper publicPromptMapper;

    /**
    * 创建
    *
    * @param personalPromptAddDto
    * @return
    */
    @Override
    public Integer add(PersonalPromptAddDto personalPromptAddDto) {
        PersonalPromptEntity personalPromptEntity = BeanConvertUtil.conver(personalPromptAddDto, PersonalPromptEntity.class);
        personalPromptEntity.setReleaseStatus("0");
        personalPromptEntity.setUserId(SecurityUtils.getUserId());
        personalPromptEntity.setCreateBy(SecurityUtils.getUsername());
        personalPromptEntity.setCreateTime(DateUtils.getNowDate());
        return personalPromptMapper.add(personalPromptEntity);
    }

    /**
     * 更新
     *
     * @param personalPromptUpdateDto
     * @return
     */
    @Override
    @Transactional
    public Integer update(PersonalPromptUpdateDto personalPromptUpdateDto) {
        PersonalPromptEntity personalPromptEntity = BeanConvertUtil.conver(personalPromptUpdateDto, PersonalPromptEntity.class);
        if (personalPromptEntity.getId() == -1) {
            personalPromptEntity.setCreateBy(SecurityUtils.getUsername());
            personalPromptEntity.setCreateTime(DateUtils.getNowDate());
            personalPromptEntity.setUserId(SecurityUtils.getUserId());
            personalPromptEntity.setReleaseStatus("0");
            personalPromptMapper.add(personalPromptEntity);
        } else {
            personalPromptEntity.setUpdateBy(SecurityUtils.getUsername());
            personalPromptEntity.setUpdateTime(DateUtils.getNowDate());
            personalPromptMapper.updateById(personalPromptEntity);
        }
        if ("1".equals(personalPromptUpdateDto.getIsUpdateRelease())) {
            PublicPromptEntity publicPromptEntity = publicPromptMapper.selectByPersonalPromptId(personalPromptUpdateDto.getId());
            PublicPromptEntity query = publicPromptMapper.selectByName(personalPromptUpdateDto.getName());
            if (query != null ) {
                throw new ServiceException("已存在相同名称的公共提示词，无法发布");
            }
            if (null == publicPromptEntity) {
                publicPromptEntity = new PublicPromptEntity();
                publicPromptEntity.setName(personalPromptUpdateDto.getName());
                publicPromptEntity.setPrompt(personalPromptUpdateDto.getPrompt());
                publicPromptEntity.setPersonalPromptId(personalPromptEntity.getId());
                publicPromptEntity.setCreateBy(SecurityUtils.getUsername());
                publicPromptEntity.setCreateTime(DateUtils.getNowDate());
                publicPromptMapper.add(publicPromptEntity);
            } else {
                publicPromptEntity.setPrompt(personalPromptUpdateDto.getPrompt());
                publicPromptEntity.setUpdateBy(SecurityUtils.getUsername());
                publicPromptEntity.setUpdateTime(DateUtils.getNowDate());
                publicPromptEntity.setName(personalPromptUpdateDto.getName());
                publicPromptEntity.setId(publicPromptEntity.getId());
                publicPromptMapper.updateById(publicPromptEntity);
            }
        }
        return 1;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        if ("1".equals(personalPromptMapper.getById(id).getReleaseStatus())) {
            throw new RuntimeException("已发布的提示词不能删除,请先取消发布在尝试删除提示词");
        }
        return personalPromptMapper.deleteById(id);
    }

    /**
     * 查询分页信息
     *
     * @param personalPromptPageQryDto
     * @return
     */
    @Override
    public List<PersonalPromptPageVO> pageList(PersonalPromptPageQryDto personalPromptPageQryDto) {
        personalPromptPageQryDto.setUserId(SecurityUtils.getUserId());
        return personalPromptMapper.pageList(personalPromptPageQryDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public PersonalPromptInfoVO getById(Long id) {
        return personalPromptMapper.getById(id);
    }

    @Override
    public Integer release(PersonalPromptReleaseDto personalPromptReleaseDto) {
        Integer release = personalPromptMapper.release(personalPromptReleaseDto);
        if (release == 0 ){
            return  0;
        }
        if ("1".equals(personalPromptReleaseDto.getReleaseStatus())) {
            PersonalPromptInfoVO byId = personalPromptMapper.getById(personalPromptReleaseDto.getId());
            PublicPromptEntity query = publicPromptMapper.selectByName(byId.getName());
            if (query != null) {
                throw new ServiceException("已存在相同名称的公共提示词，无法发布");
            }
            PublicPromptEntity publicPromptEntity = new PublicPromptEntity();
            publicPromptEntity.setName(byId.getName());
            publicPromptEntity.setPrompt(byId.getPrompt());
            publicPromptEntity.setPersonalPromptId(personalPromptReleaseDto.getId());
            publicPromptEntity.setCreateBy(SecurityUtils.getUsername());
            publicPromptEntity.setCreateTime(DateUtils.getNowDate());
            publicPromptMapper.add(publicPromptEntity);
        }else {
            publicPromptMapper.deleteByPersonalPromptId(personalPromptReleaseDto.getId());
        }
        return 1;
    }
}
