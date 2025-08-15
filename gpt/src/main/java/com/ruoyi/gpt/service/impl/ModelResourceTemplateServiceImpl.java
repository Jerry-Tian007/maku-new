package com.ruoyi.gpt.service.impl;

import java.util.Collections;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.ModelResourceTemplateMapper;
import com.ruoyi.gpt.model.domain.ModelResourceTemplate;
import com.ruoyi.gpt.service.IModelResourceTemplateService;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateListDto;
import com.ruoyi.gpt.model.vo.model_resource_template.ModelResourceTemplateListVo;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateUpdateDto;
import com.ruoyi.common.utils.bean.BeanConvertUtil;

/**
 * 模型资源模板管理Service业务层处理
 *
 * @author Eric
 * @date 2024-07-15
 */
@Service
public class ModelResourceTemplateServiceImpl implements IModelResourceTemplateService {
    @Autowired
    private ModelResourceTemplateMapper modelResourceTemplateMapper;

    /**
     * 查询模型资源模板管理
     *
     * @param id 模型资源模板管理主键
     * @return 模型资源模板管理
     */
    @Override
    public ModelResourceTemplate selectModelResourceTemplateById(Long id) {
        return modelResourceTemplateMapper.selectModelResourceTemplateById(id);
    }

    /**
     * 平台查询模型资源模板管理列表
     *
     * @param modelResourceTemplateListDto 模型资源模板管理
     * @return 模型资源模板管理
     */
    @Override
    public List<ModelResourceTemplateListVo> selectModelResourceTemplateListForPlatform(ModelResourceTemplateListDto modelResourceTemplateListDto) {
        return modelResourceTemplateMapper.selectModelResourceTemplateListForPlatform(modelResourceTemplateListDto);
    }

    /**
     * 查询模型资源模板管理列表
     *
     * @param modelResourceTemplate 模型资源模板管理
     * @return 模型资源模板管理
     */
    @Override
    public List<ModelResourceTemplate> selectModelResourceTemplateList(ModelResourceTemplate modelResourceTemplate) {
        return modelResourceTemplateMapper.selectModelResourceTemplateList(modelResourceTemplate);
    }

    /**
     * 新增模型资源模板管理
     *
     * @param modelResourceTemplateInsertDto 模型资源模板管理
     * @return 结果
     */
    @Override
    public int insertModelResourceTemplate(ModelResourceTemplateInsertDto modelResourceTemplateInsertDto) {
        ModelResourceTemplate modelResourceTemplate = BeanConvertUtil.conver(modelResourceTemplateInsertDto, ModelResourceTemplate.class);
        modelResourceTemplate.setCreateTime(DateUtils.getNowDate());
        modelResourceTemplate.setCreateBy(SecurityUtils.getUsername());
        return modelResourceTemplateMapper.insertModelResourceTemplate(modelResourceTemplate);
    }

    /**
     * 修改模型资源模板管理
     *
     * @param modelResourceTemplateUpdateDto 模型资源模板管理
     * @return 结果
     */
    @Override
    public int updateModelResourceTemplate(ModelResourceTemplateUpdateDto modelResourceTemplateUpdateDto) {
        ModelResourceTemplate modelResourceTemplate = BeanConvertUtil.conver(modelResourceTemplateUpdateDto, ModelResourceTemplate.class);
        modelResourceTemplate.setUpdateTime(DateUtils.getNowDate());
        modelResourceTemplate.setUpdateBy(SecurityUtils.getUsername());
        return modelResourceTemplateMapper.updateModelResourceTemplate(modelResourceTemplate);
    }

    /**
     * 批量删除模型资源模板管理
     *
     * @param ids 需要删除的模型资源模板管理主键
     * @return 结果
     */
    @Override
    public int deleteModelResourceTemplateByIds(Long[] ids) {
        return modelResourceTemplateMapper.deleteModelResourceTemplateByIds(ids);
    }

    /**
     * 删除模型资源模板管理信息
     *
     * @param id 模型资源模板管理主键
     * @return 结果
     */
    @Override
    public int deleteModelResourceTemplateById(Long id) {
        return modelResourceTemplateMapper.deleteModelResourceTemplateById(id);
    }

    @Override
    public List<ModelResourcesSelectVo> getSelect() {
        // 超级管理员查询所有
        if (SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            return modelResourceTemplateMapper.getAllSelect();
        }
        Long[] roleIds = SecurityUtils.getLoginUser().getUser().getRoleIds();
        if (roleIds == null || roleIds.length == 0) {
            return Collections.emptyList();
        }
        return modelResourceTemplateMapper.getSelect(roleIds);
    }
}
