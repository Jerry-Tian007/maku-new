package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.gpt.mapper.PromptManageMapper;
import com.ruoyi.gpt.mapper.PromptModelManageMapper;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.domain.PromptModelManage;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManagePublishDto;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManageSelectDto;
import com.ruoyi.gpt.service.IFileCommonService;
import com.ruoyi.gpt.service.IPromptManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;



/**
 * promptManageService业务层处理
 * 
 * @author Eric
 * @date 2024-09-02
 */
@Service
public class PromptManageServiceImpl implements IPromptManageService 
{
    @Autowired
    private PromptManageMapper promptManageMapper;
    @Autowired
    private PromptModelManageMapper promptModelManageMapper;
    @Autowired
    private IFileCommonService iFileCommonService;
    /**
     * 查询promptManage
     * 
     * @param id promptManage主键
     * @return promptManage
     */
    @Override
    public PromptManage selectPromptManageById(Long id)
    {
        return promptManageMapper.selectPromptManageById(id);
    }

    /**
     * 查询promptManage列表
     * 
     * @param promptManage promptManage
     * @return promptManage
     */
    @Override
    public List<PromptManage> selectPromptManageList(PromptManage promptManage)
    {
        return promptManageMapper.selectPromptManageList(promptManage);
    }

    /**
     * 新增promptManage
     * 
     * @param promptManage promptManage
     * @return 结果
     */
    @Override
    public int insertPromptManage(PromptManage promptManage)
    {
        PromptManage promptManage1 = new PromptManage();
        promptManage1.setModel(promptManage.getModel());
        promptManage1.setTitle(promptManage.getTitle());
        if (promptManageMapper.selectPromptManageList(promptManage1).size()>1){
            throw new ServiceException("当前模型下已存在相同标题提示词，请修改标题后重试");
        }
        promptManage.setCreateTime(DateUtils.getNowDate());
        promptManage.setCreateBy(SecurityUtils.getUsername());
        return promptManageMapper.insertPromptManage(promptManage);
    }

    /**
     * 修改promptManage
     * 
     * @param promptManage promptManage
     * @return 结果
     */
    @Override
    public int updatePromptManage(PromptManage promptManage)
    {
        promptManage.setUpdateTime(DateUtils.getNowDate());
        promptManage.setUpdateBy(SecurityUtils.getUsername());
        return promptManageMapper.updatePromptManage(promptManage);
    }

    /**
     * 批量删除promptManage
     * 
     * @param ids 需要删除的promptManage主键
     * @return 结果
     */
    @Override
    public int deletePromptManageByIds(Long[] ids)
    {
        for (Long id:ids) {
            PromptModelManage promptModelManage = new PromptModelManage();
            promptModelManage.setManageId(id);
            List<PromptModelManage> promptModelManages = promptModelManageMapper.selectPromptModelManageList(promptModelManage);
            if (promptModelManages.size()>0){
                throw new ServiceException("当前提示词场景与模型绑定，请先取消绑定后在进行删除");
            }
        }
        return promptManageMapper.deletePromptManageByIds(ids);
    }

    /**
     * 删除promptManage信息
     * 
     * @param id promptManage主键
     * @return 结果
     */
    @Override
    public int deletePromptManageById(Long id)
    {
        return promptManageMapper.deletePromptManageById(id);
    }

    @Override
    public int publishPromptManage(PromptManagePublishDto promptManagePublishDto) {
        return promptManageMapper.publishPromptManage(promptManagePublishDto);
    }

    @Override
    public int selectPromptManage(PromptManageSelectDto promptManageSelectDto) {
        return promptManageMapper.selectPromptManage(promptManageSelectDto);
    }

    @Override
    public String uploadIcon(MultipartFile multipartFile) {
        return iFileCommonService.promptUpload(multipartFile);
    }

}
