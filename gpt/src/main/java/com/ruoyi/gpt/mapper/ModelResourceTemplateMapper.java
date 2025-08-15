package com.ruoyi.gpt.mapper;

import java.util.List;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ModelResourceTemplate;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateListDto;
import com.ruoyi.gpt.model.vo.model_resource_template.ModelResourceTemplateListVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
import org.apache.ibatis.annotations.Param;

/**
 * 模型资源模板管理Mapper接口
 *
 * @author Eric
 * @date 2024-07-15
 */
public interface ModelResourceTemplateMapper {
    /**
     * 查询模型资源模板管理
     *
     * @param id 模型资源模板管理主键
     * @return 模型资源模板管理
     */
    public ModelResourceTemplate selectModelResourceTemplateById(Long id);

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
     * @param modelResourceTemplate 模型资源模板管理
     * @return 结果
     */
    @GenerateUniqueId
    public int insertModelResourceTemplate(ModelResourceTemplate modelResourceTemplate);

    /**
     * 修改模型资源模板管理
     *
     * @param modelResourceTemplate 模型资源模板管理
     * @return 结果
     */
    public int updateModelResourceTemplate(ModelResourceTemplate modelResourceTemplate);

    /**
     * 删除模型资源模板管理
     *
     * @param id 模型资源模板管理主键
     * @return 结果
     */
    public int deleteModelResourceTemplateById(Long id);

    /**
     * 批量删除模型资源模板管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelResourceTemplateByIds(Long[] ids);

    List<ModelResourcesSelectVo> getSelect(@Param("roleIds") Long[] roleIds);

    List<ModelResourcesSelectVo> getAllSelect();

    List<ModelResourceTemplateListVo> selectModelResourceTemplateListForPlatform(ModelResourceTemplateListDto modelResourceTemplateListDto);
}
