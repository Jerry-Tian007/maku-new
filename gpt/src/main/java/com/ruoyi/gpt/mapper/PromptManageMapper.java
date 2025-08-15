package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManagePublishDto;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManageSelectDto;

import java.util.List;

/**
 * promptManageMapper接口
 * 
 * @author Eric
 * @date 2024-09-02
 */
public interface PromptManageMapper 
{
    /**
     * 查询promptManage
     * 
     * @param id promptManage主键
     * @return promptManage
     */
    public PromptManage selectPromptManageById(Long id);

    /**
     * 查询promptManage列表
     * 
     * @param promptManage promptManage
     * @return promptManage集合
     */
    public List<PromptManage> selectPromptManageList(PromptManage promptManage);

    /**
     * 新增promptManage
     * 
     * @param promptManage promptManage
     * @return 结果
     */
    @GenerateUniqueId
    public int insertPromptManage(PromptManage promptManage);

    /**
     * 修改promptManage
     * 
     * @param promptManage promptManage
     * @return 结果
     */
    public int updatePromptManage(PromptManage promptManage);

    /**
     * 删除promptManage
     * 
     * @param id promptManage主键
     * @return 结果
     */
    public int deletePromptManageById(Long id);

    /**
     * 批量删除promptManage
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptManageByIds(Long[] ids);

    int publishPromptManage(PromptManagePublishDto promptManagePublishDto);

    int selectPromptManage(PromptManageSelectDto promptManageSelectDto);
}
