package com.ruoyi.gpt.service;

import java.util.List;

import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesInsertDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesListDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesUpdateDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesAuthorizeUseVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesListVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;


/**
 * 模型资源管理Service接口
 *
 * @author Eric
 * @date 2024-07-09
 */
public interface IModelResourcesService {
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
     * @param modelResourcesInsertDto 模型资源管理
     * @return 结果
     */
    public int insertModelResources(ModelResourcesInsertDto modelResourcesInsertDto);

    /**
     * 修改模型资源管理
     *
     * @param modelResourcesUpdateDto 模型资源管理
     * @return 结果
     */
    public int updateModelResources(ModelResourcesUpdateDto modelResourcesUpdateDto);

    /**
     * 批量删除模型资源管理
     *
     * @param ids 需要删除的模型资源管理主键集合
     * @return 结果
     */
    public int deleteModelResourcesByIds(Long[] ids);


    List<ModelResourcesListVo> selectModelResourcesListForPlatform(ModelResourcesListDto modelResourcesListDto);


    boolean verificationModelResources(ModelResourcesInsertDto modelResourcesInsertDto);

    String encrypt(String parameter);

    List<ModelResourcesAuthorizeUseVo> getAuthorizeModelResourcesList();
}
