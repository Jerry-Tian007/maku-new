package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.PromptGroup;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupPublishDto;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupUnderManageDto;

import java.util.List;

/**
 * promptGroupMapper接口
 * 
 * @author Eric
 * @date 2024-09-02
 */
public interface PromptGroupMapper 
{
    /**
     * 查询promptGroup
     * 
     * @param id promptGroup主键
     * @return promptGroup
     */
    public PromptGroup selectPromptGroupById(Long id);

    /**
     * 查询promptGroup列表
     * 
     * @param promptGroup promptGroup
     * @return promptGroup集合
     */
    public List<PromptGroup> selectPromptGroupList(PromptGroup promptGroup);

    /**
     * 新增promptGroup
     * 
     * @param promptGroup promptGroup
     * @return 结果
     */
    @GenerateUniqueId
    public int insertPromptGroup(PromptGroup promptGroup);

    /**
     * 修改promptGroup
     * 
     * @param promptGroup promptGroup
     * @return 结果
     */
    public int updatePromptGroup(PromptGroup promptGroup);

    /**
     * 删除promptGroup
     * 
     * @param id promptGroup主键
     * @return 结果
     */
    public int deletePromptGroupById(Long id);

    /**
     * 批量删除promptGroup
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptGroupByIds(Long[] ids);

    List<PromptGroup> selectPromptGroupHomeList(PromptGroup promptGroup);

    int publishPromptGroup(PromptGroupPublishDto promptGroupPublishDto);

    List<PromptManage> selectPromptGroupUnderManageList(PromptGroupUnderManageDto promptGroupUnderManageDto);

    List<PromptGroup> selectPromptGroupSelectList(PromptGroup promptGroup);

    List<PromptManage> selectPromptGroupHomeSelectList(PromptGroupUnderManageDto promptGroupUnderManageDto);
}
