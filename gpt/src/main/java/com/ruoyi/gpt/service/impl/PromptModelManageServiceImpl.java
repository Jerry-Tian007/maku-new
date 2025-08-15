package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.PromptModelManageMapper;
import com.ruoyi.gpt.model.domain.PromptModelManage;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageAddDto;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageListDto;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelManageListVo;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelUnManageListVo;
import com.ruoyi.gpt.service.IPromptModelManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模型场景管理Service业务层处理
 * 
 * @author Eric
 * @date 2024-09-19
 */
@Service
public class PromptModelManageServiceImpl implements IPromptModelManageService 
{
    @Autowired
    private PromptModelManageMapper promptModelManageMapper;

    /**
     * 查询模型场景管理
     * 
     * @param id 模型场景管理主键
     * @return 模型场景管理
     */
    @Override
    public PromptModelManage selectPromptModelManageById(Long id)
    {
        return promptModelManageMapper.selectPromptModelManageById(id);
    }

    /**
     * 查询模型场景管理列表
     * 
     * @param promptModelManage 模型场景管理
     * @return 模型场景管理
     */
    @Override
    public List<PromptModelManage> selectPromptModelManageList(PromptModelManage promptModelManage)
    {
        return promptModelManageMapper.selectPromptModelManageList(promptModelManage);
    }

    /**
     * 新增模型场景管理
     * 
     * @param promptModelManageAddDto 模型场景管理
     * @return 结果
     */
    @Override
    public int insertPromptModelManage(PromptModelManageAddDto promptModelManageAddDto)
    {
        PromptModelManage promptModelManage1 = new PromptModelManage();
        promptModelManage1.setModel(promptModelManageAddDto.getModel());
        List<PromptModelManage> promptModelManages = promptModelManageMapper.selectPromptModelManageList(promptModelManage1);
        if (promptModelManages.size() > 5) {
            throw new ServiceException("模型："+promptModelManageAddDto.getModel()+"配置场景最多只能添加5个,请删除后再添加");
        }
        PromptModelManage promptModelManage = new PromptModelManage();
        promptModelManage.setModel(promptModelManageAddDto.getModel());
        promptModelManage.setManageId(promptModelManageAddDto.getManageId());
        promptModelManage.setModelOrder(promptModelManageAddDto.getManageOrder());
        promptModelManage.setCreateBy(SecurityUtils.getUsername());
        promptModelManage.setCreateTime(DateUtils.getNowDate());
        promptModelManageMapper.insertPromptModelManage(promptModelManage);
        return 1;
    }

    /**
     * 修改模型场景管理
     * 
     * @param promptModelManage 模型场景管理
     * @return 结果
     */
    @Override
    public int updatePromptModelManage(PromptModelManage promptModelManage)
    {
        promptModelManage.setUpdateTime(DateUtils.getNowDate());
        promptModelManage.setUpdateBy(SecurityUtils.getUsername());
        return promptModelManageMapper.updatePromptModelManage(promptModelManage);
    }

    /**
     * 批量删除模型场景管理
     * 
     * @param ids 需要删除的模型场景管理主键
     * @return 结果
     */
    @Override
    public int deletePromptModelManageByIds(Long[] ids)
    {
        return promptModelManageMapper.deletePromptModelManageByIds(ids);
    }

    /**
     * 删除模型场景管理信息
     * 
     * @param id 模型场景管理主键
     * @return 结果
     */
    @Override
    public int deletePromptModelManageById(Long id)
    {
        return promptModelManageMapper.deletePromptModelManageById(id);
    }

    @Override
    public List<PromptModelManageListVo> selectPromptManageByModel(PromptModelManageListDto promptModelManageListDto) {
        return promptModelManageMapper.selectPromptManageByModel(promptModelManageListDto);
    }

    @Override
    public List<PromptModelUnManageListVo> selectPromptUnManageByModel(PromptModelManageListDto promptModelManageListDto) {
        return promptModelManageMapper.selectPromptUnManageByModel(promptModelManageListDto);
    }
}
