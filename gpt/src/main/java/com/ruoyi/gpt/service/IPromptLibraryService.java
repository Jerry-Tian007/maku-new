package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.PromptLibrary;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibraryListDto;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibrarySelectListDto;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibraryListVo;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibrarySelectListVo;

/**
 * promptLibraryService接口
 * 
 * @author Eric
 * @date 2023-07-31
 */
public interface IPromptLibraryService 
{
    /**
     * 查询promptLibrary
     * 
     * @param id promptLibrary主键
     * @return promptLibrary
     */
    public PromptLibrary selectPromptLibraryById(Long id);

    /**
     * 查询promptLibrary列表
     * 
     * @param promptLibrary promptLibrary
     * @return promptLibrary集合
     */
    public List<PromptLibraryListVo> selectPromptLibraryList(PromptLibraryListDto promptLibraryListDtor);

    /**
     * 新增promptLibrary
     * 
     * @param promptLibrary promptLibrary
     * @return 结果
     */
    public int insertPromptLibrary(PromptLibrary promptLibrary);

    /**
     * 修改promptLibrary
     * 
     * @param promptLibrary promptLibrary
     * @return 结果
     */
    public int updatePromptLibrary(PromptLibrary promptLibrary);

    /**
     * 批量删除promptLibrary
     * 
     * @param ids 需要删除的promptLibrary主键集合
     * @return 结果
     */
    public int deletePromptLibraryByIds(Long[] ids);

    /**
     * 删除promptLibrary信息
     * 
     * @param id promptLibrary主键
     * @return 结果
     */
    public int deletePromptLibraryById(Long id);

    List<PromptLibrarySelectListVo> selectPromptLibrarySelectList(PromptLibrarySelectListDto promptLibrarySelectListDto);
}
