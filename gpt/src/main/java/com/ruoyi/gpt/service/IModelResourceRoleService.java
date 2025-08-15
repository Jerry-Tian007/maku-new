package com.ruoyi.gpt.service;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.gpt.model.domain.ModelResourceRole;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleListDto;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleListVo;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleUpdateDto;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleVo;

/**
 * 角色模型资源Service接口
 *
 * @author Eric
 * @date 2024-07-22
 */
public interface IModelResourceRoleService {
    /**
     * 查询角色模型资源
     *
     * @param id 角色模型资源主键
     * @return 角色模型资源
     */
    public ModelResourceRole selectModelResourceRoleById(Long id);

    /**
     * 平台查询角色模型资源列表
     *
     * @param modelResourceRoleListDto 角色模型资源
     * @return 角色模型资源集合
     */
    public List<ModelResourceRoleVo> selectModelResourceRoleListForPlatform(ModelResourceRoleListDto modelResourceRoleListDto);


    /**
     * 查询角色模型资源列表
     *
     * @param modelResourceRole 角色模型资源
     * @return 角色模型资源集合
     */
    public List<ModelResourceRole> selectModelResourceRoleList(ModelResourceRole modelResourceRole);

    /**
     * 新增角色模型资源
     *
     * @param modelResourceRoleInsertDto 角色模型资源
     * @return 结果
     */
    public int insertModelResourceRole(ModelResourceRoleInsertDto modelResourceRoleInsertDto);

    /**
     * 修改角色模型资源
     *
     * @param modelResourceRoleUpdateDto 角色模型资源
     * @return 结果
     */
    public int updateModelResourceRole(ModelResourceRoleUpdateDto modelResourceRoleUpdateDto);

    /**
     * 批量删除角色模型资源
     *
     * @param ids 需要删除的角色模型资源主键集合
     * @return 结果
     */
    public int deleteModelResourceRoleByIds(List<Long> ids);

    /**
     * 删除角色模型资源信息
     *
     * @param id 角色模型资源主键
     * @return 结果
     */
    public int deleteModelResourceRoleById(Long id);

    Set<String> getModelResources(SysUser user);

    Set<String> getModelResources(Long roleId);

}
