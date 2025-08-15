package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.gpt.mapper.PromptGroupMapper;
import com.ruoyi.gpt.mapper.PromptManageMapper;
import com.ruoyi.gpt.model.domain.PromptGroup;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupPublishDto;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupUnderManageDto;
import com.ruoyi.gpt.model.vo.prompt_group.PromptGroupHomeListVo;
import com.ruoyi.gpt.service.IFileCommonService;
import com.ruoyi.gpt.service.IPromptGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * promptGroupService业务层处理
 * 
 * @author Eric
 * @date 2024-09-02
 */
@Service
public class PromptGroupServiceImpl implements IPromptGroupService 
{
    @Autowired
    private PromptGroupMapper promptGroupMapper;

    @Autowired
    private PromptManageMapper promptManageMapper;

    @Autowired
    private IFileCommonService iFileCommonService;

    /**
     * 查询promptGroup
     * 
     * @param id promptGroup主键
     * @return promptGroup
     */
    @Override
    public PromptGroup selectPromptGroupById(Long id)
    {
        return promptGroupMapper.selectPromptGroupById(id);
    }

    /**
     * 查询promptGroup列表
     * 
     * @param promptGroup promptGroup
     * @return promptGroup
     */
    @Override
    public List<PromptGroup> selectPromptGroupList(PromptGroup promptGroup)
    {
        return promptGroupMapper.selectPromptGroupList(promptGroup);
    }

    /**
     * 新增promptGroup
     * 
     * @param promptGroup promptGroup
     * @return 结果
     */
    @Override
    public int insertPromptGroup(PromptGroup promptGroup)
    {
        PromptGroup promptGroup1 = new PromptGroup();
        promptGroup1.setName(promptGroup.getName());
        List<PromptGroup> promptGroups = promptGroupMapper.selectPromptGroupList(promptGroup1);
        if (promptGroups.size() > 0) {
            throw new RuntimeException("该分组名称已存在");
        }
        promptGroup.setCreateBy(SecurityUtils.getUsername());
        promptGroup.setCreateTime(DateUtils.getNowDate());
        return promptGroupMapper.insertPromptGroup(promptGroup);
    }

    /**
     * 修改promptGroup
     * 
     * @param promptGroup promptGroup
     * @return 结果
     */
    @Override
    public int updatePromptGroup(PromptGroup promptGroup)
    {
        promptGroup.setUpdateBy(SecurityUtils.getUsername());
        promptGroup.setUpdateTime(DateUtils.getNowDate());
        return promptGroupMapper.updatePromptGroup(promptGroup);
    }

    /**
     * 批量删除promptGroup
     * 
     * @param ids 需要删除的promptGroup主键
     * @return 结果
     */
    @Override
    public int deletePromptGroupByIds(Long[] ids)
    {
        for (Long id : ids) {
            PromptGroup promptGroup = promptGroupMapper.selectPromptGroupById(id);
            if (promptGroup.getIsPublish() == 1) {
                throw new ServiceException("已发布的分组不允许删除");
            }
            PromptManage promptManage = new PromptManage();
            promptManage.setGroup(promptGroup.getName());
            if (promptManageMapper.selectPromptManageList(promptManage).size()>0){
                throw new ServiceException("分组下存在提示信息，不允许删除");
            }
        }
        return promptGroupMapper.deletePromptGroupByIds(ids);
    }

    /**
     * 删除promptGroup信息
     * 
     * @param id promptGroup主键
     * @return 结果
     */
    @Override
    public int deletePromptGroupById(Long id)
    {
        return promptGroupMapper.deletePromptGroupById(id);
    }

    @Override
    public String uploadIcon(MultipartFile multipartFile) {
        return iFileCommonService.promptUpload(multipartFile);
    }

    @Override
    public List<PromptGroupHomeListVo> selectPromptGroupHomeList(PromptGroup promptGroup) {
        List<PromptGroup> promptGroups = promptGroupMapper.selectPromptGroupHomeList(promptGroup);
        List<PromptGroupHomeListVo> result = new ArrayList<>();
        for (PromptGroup promptGroup1 : promptGroups) {
            PromptGroupHomeListVo promptGroupHomeListVo = new PromptGroupHomeListVo();
            PromptGroupUnderManageDto promptGroupUnderManageDto = new PromptGroupUnderManageDto();
            promptGroupUnderManageDto.setGroup(promptGroup1.getName());
            List<PromptManage> promptManages = promptGroupMapper.selectPromptGroupUnderManageList(promptGroupUnderManageDto);
            promptGroupHomeListVo.setPromptManageList(promptManages);
            promptGroupHomeListVo.setIcon(promptGroup1.getIcon());
            promptGroupHomeListVo.setName(promptGroup1.getName());
            result.add(promptGroupHomeListVo);
        }
        return result;
    }

    @Override
    public int publishPromptGroup(PromptGroupPublishDto promptGroupPublishDto) {
        return promptGroupMapper.publishPromptGroup(promptGroupPublishDto);
    }

    @Override
    public List<PromptManage> selectPromptGroupUnderManageList(PromptGroupUnderManageDto promptGroupUnderManageDto) {
        return promptGroupMapper.selectPromptGroupUnderManageList(promptGroupUnderManageDto);
    }

    @Override
    public List<PromptGroup> selectPromptGroupSelectList(PromptGroup promptGroup) {
        return promptGroupMapper.selectPromptGroupSelectList(promptGroup);
    }

    @Override
    public PromptGroupHomeListVo selectPromptGroupHomeSelectList(PromptGroup promptGroup) {
        PromptGroupUnderManageDto promptGroupUnderManageDto = new PromptGroupUnderManageDto();
        promptGroupUnderManageDto.setGroup(promptGroup.getName());
        List<PromptManage> promptManages = promptGroupMapper.selectPromptGroupHomeSelectList(promptGroupUnderManageDto);
        PromptGroupHomeListVo promptGroupHomeListVo = new PromptGroupHomeListVo();
        promptGroupHomeListVo.setIcon("");
        promptGroupHomeListVo.setPromptManageList(promptManages);
        promptGroupHomeListVo.setName("精选");
        return promptGroupHomeListVo;
    }

}
