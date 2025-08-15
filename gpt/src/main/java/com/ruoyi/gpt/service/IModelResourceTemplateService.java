package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.ModelResourceTemplate;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateListDto;
import com.ruoyi.gpt.model.vo.model_resource_template.ModelResourceTemplateListVo;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateUpdateDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;

/**
 * 模型资源模板管理Service接口
 * 
 * @author Eric
 * @date 2024-07-15
 */
public interface IModelResourceTemplateService 
{
    /**
     * 查询模型资源模板管理
     * 
     * @param id 模型资源模板管理主键
     * @return 模型资源模板管理
     */
    public ModelResourceTemplate selectModelResourceTemplateById(Long id);

    /**
     * 平台查询模型资源模板管理列表
     *
     * @param modelResourceTemplateListDto 模型资源模板管理
     * @return 模型资源模板管理集合
     */
    public List<ModelResourceTemplateListVo> selectModelResourceTemplateListForPlatform(ModelResourceTemplateListDto modelResourceTemplateListDto);


    /**
     * 查询模型资源模板管理列表
     * 
     * @param modelResourceTemplate 模型资源模板管理
     * @return 模型资源模板管理集合
     */
    public List<ModelResourceTemplate> selectModelResourceTemplateList(ModelResourceTemplate modelResourceTemplate);

    /**
     * 新增模型资源模板管理
     * 
     * @param modelResourceTemplateInsertDto 模型资源模板管理
     * @return 结果
     */
    public int insertModelResourceTemplate(ModelResourceTemplateInsertDto modelResourceTemplateInsertDto);

    /**
     * 修改模型资源模板管理
     * 
     * @param modelResourceTemplateUpdateDto 模型资源模板管理
     * @return 结果
     */
    public int updateModelResourceTemplate(ModelResourceTemplateUpdateDto modelResourceTemplateUpdateDto);

    /**
     * 批量删除模型资源模板管理
     * 
     * @param ids 需要删除的模型资源模板管理主键集合
     * @return 结果
     */
    public int deleteModelResourceTemplateByIds(Long[] ids);

    /**
     * 删除模型资源模板管理信息
     * 
     * @param id 模型资源模板管理主键
     * @return 结果
     */
    public int deleteModelResourceTemplateById(Long id);

    List<ModelResourcesSelectVo> getSelect();
}
