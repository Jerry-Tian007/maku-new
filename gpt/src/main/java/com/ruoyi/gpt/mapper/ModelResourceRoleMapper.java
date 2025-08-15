package com.ruoyi.gpt.mapper;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ModelResourceRole;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleListDto;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleVo;
import org.apache.ibatis.annotations.Param;

/**
 * 角色模型资源Mapper接口
 * 
 * @author Eric
 * @date 2024-07-22
 */
public interface ModelResourceRoleMapper 
{
    /**
     * 查询角色模型资源
     * 
     * @param id 角色模型资源主键
     * @return 角色模型资源
     */
    public ModelResourceRole selectModelResourceRoleById(Long id);

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
     * @param modelResourceRole 角色模型资源
     * @return 结果
     */
    @GenerateUniqueId
    public int insertModelResourceRole(ModelResourceRole modelResourceRole);

    /**
     * 修改角色模型资源
     * 
     * @param modelResourceRole 角色模型资源
     * @return 结果
     */
    public int updateModelResourceRole(ModelResourceRole modelResourceRole);

    /**
     * 删除角色模型资源
     * 
     * @param id 角色模型资源主键
     * @return 结果
     */
    public int deleteModelResourceRoleById(Long id);

    /**
     * 批量删除角色模型资源
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelResourceRoleByIds(List<Long> ids);

    int deleteModelResourceRoleByRoleId(Long roleId);

    ModelResourceRole selectModelResourceRoleByRoleId(Long roleId);

    List<ModelResourceRoleVo> selectModelResourceRoleListForPlatform(ModelResourceRoleListDto modelResourceRoleListDto);

    int deleteModelResourceRoleByModelType(Long modelResourceId);

    Set<String> getModelResources(Long roleId);

    List<ModelResourceRoleVo> getModelResourcesByRoleIds(@Param("roleIds") List<Long> roleIds);
}
