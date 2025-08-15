package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbPersonalPromptMapper;
import com.ruoyi.gpt.mapper.KbPublicPromptMapper;
import com.ruoyi.gpt.mapper.KbQueryPromptMapper;
import com.ruoyi.gpt.model.domain.PersonalPromptEntity;
import com.ruoyi.gpt.model.domain.PublicPromptEntity;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptAddDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptReleaseDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptUpdateDto;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptInfoVO;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptPageVO;
import com.ruoyi.gpt.model.vo.personal_prompt.QueryPromptPageVO;
import com.ruoyi.gpt.model.vo.public_prompt.PublicPromptPageVO;
import com.ruoyi.gpt.service.IKbPersonalPromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Eric
 */
@Slf4j
@Service
public class KbPersonalPromptServiceImpl implements IKbPersonalPromptService {

    @Autowired
    private KbPersonalPromptMapper kbPersonalPromptMapper;

    @Autowired
    private KbPublicPromptMapper kbPublicPromptMapper;

    @Autowired
    private KbQueryPromptMapper kbQueryPromptMapper;
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
        return kbPersonalPromptMapper.add(personalPromptEntity);
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
        personalPromptEntity.setUpdateBy(SecurityUtils.getUsername());
        personalPromptEntity.setUpdateTime(DateUtils.getNowDate());
        kbPersonalPromptMapper.updateById(personalPromptEntity);

        // if found PublicPromptEntity by PersonalPromptId, means have been released. action updating. 1. name existing,failed 2. name not existing updated.
        // if Not found  PublicPromptEntity by PersonalPromptId, means have not been released, action add. 1. name existing, failed,2. name not existing added.
        if ("1".equals(personalPromptUpdateDto.getIsUpdateRelease())) {
            PublicPromptEntity query = kbPublicPromptMapper.selectByName(personalPromptUpdateDto.getName());
            if (query != null) {
                throw new ServiceException("已存在相同名称的公共提示词，无法发布");
            }
            PublicPromptEntity publicPromptEntity = kbPublicPromptMapper.selectByPersonalPromptId(personalPromptUpdateDto.getId());
            if (publicPromptEntity == null) {
                publicPromptEntity = new PublicPromptEntity();
                publicPromptEntity.setName(personalPromptUpdateDto.getName());
                publicPromptEntity.setPrompt(personalPromptUpdateDto.getPrompt());
                publicPromptEntity.setPersonalPromptId(personalPromptEntity.getId());
                publicPromptEntity.setCreateBy(SecurityUtils.getUsername());
                publicPromptEntity.setCreateTime(DateUtils.getNowDate());
                kbPublicPromptMapper.add(publicPromptEntity);
            } else {
                publicPromptEntity.setPrompt(personalPromptUpdateDto.getPrompt());
                publicPromptEntity.setUpdateBy(SecurityUtils.getUsername());
                publicPromptEntity.setUpdateTime(DateUtils.getNowDate());
                publicPromptEntity.setName(personalPromptUpdateDto.getName());
                publicPromptEntity.setId(publicPromptEntity.getId());
                kbPublicPromptMapper.updateById(publicPromptEntity);
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
        if ("1".equals(kbPersonalPromptMapper.getById(id).getReleaseStatus())) {
            throw new RuntimeException("已发布的提示词不能删除,请先取消发布在尝试删除提示词");
        }
        return kbPersonalPromptMapper.deleteById(id);
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
        List<PersonalPromptPageVO> personalPromptPageVOS = kbPersonalPromptMapper.pageList(personalPromptPageQryDto);
        if (StringUtils.isEmpty(personalPromptPageVOS)) {
            PublicPromptPageVO publicPromptPageVO = kbPublicPromptMapper.selectUserDefaultPrompt();
            PersonalPromptPageVO personalPromptPageVO = new PersonalPromptPageVO();
            personalPromptPageVO.setPrompt(publicPromptPageVO.getPrompt());
            personalPromptPageVO.setName(publicPromptPageVO.getName());
            personalPromptPageVO.setRemark("userDefault");
            personalPromptPageVOS.add(personalPromptPageVO);
        }
        return personalPromptPageVOS;
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public PersonalPromptInfoVO getById(Long id) {
        return kbPersonalPromptMapper.getById(id);
    }

    @Override
    public Integer release(PersonalPromptReleaseDto personalPromptReleaseDto) {
        Integer release = kbPersonalPromptMapper.release(personalPromptReleaseDto);
        if (release == 0) {
            return 0;
        }
        if ("1".equals(personalPromptReleaseDto.getReleaseStatus())) {
            PersonalPromptInfoVO byId = kbPersonalPromptMapper.getById(personalPromptReleaseDto.getId());
            PublicPromptEntity query = kbPublicPromptMapper.selectByName(byId.getName());
            if (query != null) {
                throw new ServiceException("已存在相同名称的公共提示词，无法发布");
            }
            PublicPromptEntity publicPromptEntity = new PublicPromptEntity();
            publicPromptEntity.setName(byId.getName());
            publicPromptEntity.setPrompt(byId.getPrompt());
            publicPromptEntity.setPersonalPromptId(personalPromptReleaseDto.getId());
            publicPromptEntity.setCreateBy(SecurityUtils.getUsername());
            publicPromptEntity.setCreateTime(DateUtils.getNowDate());
            kbPublicPromptMapper.add(publicPromptEntity);
        } else {
            kbPublicPromptMapper.deleteByPersonalPromptId(personalPromptReleaseDto.getId());
        }
        return 1;
    }

    @Override
    public List<PersonalPromptInfoVO> kbManagerSelectPromptList(Long kbManagerId) {
        return kbPersonalPromptMapper.kbManagerSelectPromptList(kbManagerId, SecurityUtils.getUserId());
    }

    /**
     * 查询所有query_prompt_type不为空的记录
     *
     * @param personalPromptPageQryDto
     * @return
     */
    @Override
    public List<QueryPromptPageVO> queryPromptList(PersonalPromptPageQryDto personalPromptPageQryDto) {
        personalPromptPageQryDto.setUserId(SecurityUtils.getUserId());
        return kbQueryPromptMapper.queryPromptList(personalPromptPageQryDto);
    }
}
