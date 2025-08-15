package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManagePublishDto;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManageSelectDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * promptManageService接口
 * 
 * @author Eric
 * @date 2024-09-02
 */
public interface IPromptManageService 
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
    public int insertPromptManage(PromptManage promptManage);

    /**
     * 修改promptManage
     * 
     * @param promptManage promptManage
     * @return 结果
     */
    public int updatePromptManage(PromptManage promptManage);

    /**
     * 批量删除promptManage
     * 
     * @param ids 需要删除的promptManage主键集合
     * @return 结果
     */
    public int deletePromptManageByIds(Long[] ids);

    /**
     * 删除promptManage信息
     * 
     * @param id promptManage主键
     * @return 结果
     */
    public int deletePromptManageById(Long id);

    int publishPromptManage(PromptManagePublishDto promptManagePublishDto);

    int selectPromptManage(PromptManageSelectDto promptManageSelectDto);

    String uploadIcon(MultipartFile file);
}
