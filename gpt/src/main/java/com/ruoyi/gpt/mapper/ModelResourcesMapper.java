package com.ruoyi.gpt.mapper;

import java.util.List;
import java.util.Set;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesInsertDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesListDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesAuthorizeUseVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesListVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
import org.apache.ibatis.annotations.Param;


/**
 * 模型资源管理Mapper接口
 * 
 * @author Eric
 * @date 2024-07-09
 */
public interface ModelResourcesMapper 
{
    /**
     * 查询模型资源管理
     * 
     * @param id 模型资源管理主键
     * @return 模型资源管理
     */
    public ModelResources selectModelResourcesById(Long id);

    /**
     * 查询模型资源管理列表
     * 
     * @param modelResources 模型资源管理
     * @return 模型资源管理集合
     */
    public List<ModelResources> selectModelResourcesList(ModelResources modelResources);

    /**
     * 新增模型资源管理
     * 
     * @param modelResources 模型资源管理
     * @return 结果
     */
    @GenerateUniqueId
    public int insertModelResources(ModelResources modelResources);

    /**
     * 修改模型资源管理
     * 
     * @param modelResources 模型资源管理
     * @return 结果
     */
    public int updateModelResources(ModelResources modelResources);

    /**
     * 删除模型资源管理
     *
     * @param modelResources 模型资源管理
     * @return 结果
     */
    public int deleteModelResources(ModelResources modelResources);

    /**
     * 批量删除模型资源管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelResourcesByIds(Long[] ids);

    List<ModelResourcesListVo> selectModelResourcesListForPlatform(ModelResourcesListDto modelResourcesListDto);

    ModelResources verificationModelResources(ModelResourcesInsertDto modelResourcesInsertDto);

    List<Long> selectModelResourcesIdList(ModelResources modelResources);

    List<ModelResourcesAuthorizeUseVo> getAuthorizeModelResourcesList(@Param("sets")Set<String> modelResources);

    List<ModelResourcesAuthorizeUseVo> getAllModelResourcesList();
}
