package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.PromptModelManage;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageListDto;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelManageListVo;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelUnManageListVo;

import java.util.List;

/**
 * 模型场景管理Mapper接口
 * 
 * @author Eric
 * @date 2024-09-19
 */
public interface PromptModelManageMapper 
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
     * @param promptModelManage 模型场景管理
     * @return 结果
     */
    @GenerateUniqueId
    public int insertPromptModelManage(PromptModelManage promptModelManage);

    /**
     * 修改模型场景管理
     * 
     * @param promptModelManage 模型场景管理
     * @return 结果
     */
    public int updatePromptModelManage(PromptModelManage promptModelManage);

    /**
     * 删除模型场景管理
     * 
     * @param id 模型场景管理主键
     * @return 结果
     */
    public int deletePromptModelManageById(Long id);

    /**
     * 批量删除模型场景管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptModelManageByIds(Long[] ids);

    List<PromptModelManageListVo> selectPromptManageByModel(PromptModelManageListDto promptModelManageListDto);

    List<PromptModelUnManageListVo> selectPromptUnManageByModel(PromptModelManageListDto promptModelManageListDto);
}
