package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.PromptModelManage;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageAddDto;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageListDto;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelManageListVo;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelUnManageListVo;

import java.util.List;

/**
 * 模型场景管理Service接口
 * 
 * @author Eric
 * @date 2024-09-19
 */
public interface IPromptModelManageService 
{
    /**
     * 查询模型场景管理
     * 
     * @param id 模型场景管理主键
     * @return 模型场景管理
     */
    public PromptModelManage selectPromptModelManageById(Long id);

    /**
     * 查询模型场景管理列表
     * 
     * @param promptModelManage 模型场景管理
     * @return 模型场景管理集合
     */
    public List<PromptModelManage> selectPromptModelManageList(PromptModelManage promptModelManage);

    /**
     * 新增模型场景管理
     * 
     * @param promptModelManageAddDto 模型场景管理
     * @return 结果
     */
    public int insertPromptModelManage(PromptModelManageAddDto promptModelManageAddDto);

    /**
     * 修改模型场景管理
     * 
     * @param promptModelManage 模型场景管理
     * @return 结果
     */
    public int updatePromptModelManage(PromptModelManage promptModelManage);

    /**
     * 批量删除模型场景管理
     * 
     * @param ids 需要删除的模型场景管理主键集合
     * @return 结果
     */
    public int deletePromptModelManageByIds(Long[] ids);

    /**
     * 删除模型场景管理信息
     * 
     * @param id 模型场景管理主键
     * @return 结果
     */
    public int deletePromptModelManageById(Long id);

    List<PromptModelManageListVo> selectPromptManageByModel(PromptModelManageListDto promptModelManageListDto);

    List<PromptModelUnManageListVo> selectPromptUnManageByModel(PromptModelManageListDto promptModelManageListDto);
}
