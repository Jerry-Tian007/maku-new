package com.ruoyi.gpt.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibraryListDto;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibrarySelectListDto;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibraryListVo;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibrarySelectListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.PromptLibraryMapper;
import com.ruoyi.gpt.model.domain.PromptLibrary;
import com.ruoyi.gpt.service.IPromptLibraryService;

/**
 * promptLibraryService业务层处理
 *
 * @author Eric
 * @date 2023-07-31
 */
@Service
public class PromptLibraryServiceImpl implements IPromptLibraryService
{
    @Autowired
    private PromptLibraryMapper promptLibraryMapper;

    /**
     * 查询promptLibrary
     *
     * @param id promptLibrary主键
     * @return promptLibrary
     */
    @Override
    public PromptLibrary selectPromptLibraryById(Long id)
    {
        return promptLibraryMapper.selectPromptLibraryById(id);
    }

    /**
     * 查询promptLibrary列表
     *
     * @param promptLibraryListDto PromptLibraryListDto
     * @return PromptLibraryListVo
     */
    @Override
    @DataScope(userAlias = "pl",deptAlias = "")
    public List<PromptLibraryListVo> selectPromptLibraryList(PromptLibraryListDto promptLibraryListDto)
    {
        promptLibraryListDto.setUserId(SecurityUtils.getUserId());
        return promptLibraryMapper.selectPromptLibraryList(promptLibraryListDto);
    }

    /**
     * 新增promptLibrary
     *
     * @param promptLibrary promptLibrary
     * @return 结果
     */
    @Override
    public int insertPromptLibrary(PromptLibrary promptLibrary)
    {

        promptLibrary.setUserId(SecurityUtils.getUserId());
        promptLibrary.setCreateBy(SecurityUtils.getUsername());
        promptLibrary.setCreateTime(DateUtils.getNowDate());
        return promptLibraryMapper.insertPromptLibrary(promptLibrary);
    }

    /**
     * 修改promptLibrary
     *
     * @param promptLibrary promptLibrary
     * @return 结果
     */
    @Override
    public int updatePromptLibrary(PromptLibrary promptLibrary)
    {
        promptLibrary.setUpdateTime(DateUtils.getNowDate());
        return promptLibraryMapper.updatePromptLibrary(promptLibrary);
    }

    /**
     * 批量删除promptLibrary
     *
     * @param ids 需要删除的promptLibrary主键
     * @return 结果
     */
    @Override
    public int deletePromptLibraryByIds(Long[] ids)
    {
        return promptLibraryMapper.deletePromptLibraryByIds(ids);
    }

    /**
     * 删除promptLibrary信息
     *
     * @param id promptLibrary主键
     * @return 结果
     */
    @Override
    public int deletePromptLibraryById(Long id)
    {
        return promptLibraryMapper.deletePromptLibraryById(id);
    }

    @Override
    public List<PromptLibrarySelectListVo> selectPromptLibrarySelectList(PromptLibrarySelectListDto promptLibrarySelectListDto) {
        return promptLibraryMapper.selectPromptLibrarySelectList(promptLibrarySelectListDto);
    }
}
