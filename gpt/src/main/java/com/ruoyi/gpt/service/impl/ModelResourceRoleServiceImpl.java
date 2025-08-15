package com.ruoyi.gpt.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbNameVo;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.ModelResourceRoleMapper;
import com.ruoyi.gpt.model.domain.ModelResourceRole;
import com.ruoyi.gpt.service.IModelResourceRoleService;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleListDto;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleListVo;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleUpdateDto;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import org.springframework.util.CollectionUtils;

/**
 * 角色模型资源Service业务层处理
 *
 * @author Eric
 * @date 2024-07-22
 */
@Service
public class ModelResourceRoleServiceImpl implements IModelResourceRoleService {
    @Autowired
    private ModelResourceRoleMapper modelResourceRoleMapper;

    /**
     * 查询角色模型资源
     *
     * @param id 角色模型资源主键
     * @return 角色模型资源
     */
    @Override
    public ModelResourceRole selectModelResourceRoleById(Long id) {
        return modelResourceRoleMapper.selectModelResourceRoleById(id);
    }

    /**
     * 平台查询角色模型资源列表
     *
     * @param modelResourceRoleListDto 角色模型资源
     * @return 角色模型资源
     */
    @Override
    public List<ModelResourceRoleVo> selectModelResourceRoleListForPlatform(ModelResourceRoleListDto modelResourceRoleListDto) {
        List<ModelResourceRoleVo> modelResourceRoles = modelResourceRoleMapper.selectModelResourceRoleListForPlatform(modelResourceRoleListDto);
        List<Long> roleIds = modelResourceRoles.stream().map(ModelResourceRoleVo::getRoleId).collect(Collectors.toList());
        List<ModelResourceRoleVo> modelResources = modelResourceRoleMapper.getModelResourcesByRoleIds(roleIds);
        Map<Long, String> fileIdToKbNamesMap = modelResources.stream()
                .collect(Collectors.groupingBy(
                        ModelResourceRoleVo::getRoleId,
                        Collectors.mapping(ModelResourceRoleVo::getModelResourceType, Collectors.joining(","))));
        for (ModelResourceRoleVo modelResourceRoleVo : modelResourceRoles) {
            modelResourceRoleVo.setModelResourceType(fileIdToKbNamesMap.get(modelResourceRoleVo.getRoleId()));
        }
        return modelResourceRoles;
    }

    /**
     * 查询角色模型资源列表
     *
     * @param modelResourceRole 角色模型资源
     * @return 角色模型资源
     */
    @Override
    public List<ModelResourceRole> selectModelResourceRoleList(ModelResourceRole modelResourceRole) {
        return modelResourceRoleMapper.selectModelResourceRoleList(modelResourceRole);
    }

    /**
     * 新增角色模型资源
     *
     * @param modelResourceRoleInsertDto 角色模型资源
     * @return 结果
     */
    @Override
    public int insertModelResourceRole(ModelResourceRoleInsertDto modelResourceRoleInsertDto) {
        modelResourceRoleMapper.deleteModelResourceRoleByRoleId(modelResourceRoleInsertDto.getRoleId());
        for (Long type : modelResourceRoleInsertDto.getModelResourceTemplateIds()) {
            ModelResourceRole modelResourceRole = new ModelResourceRole();
            modelResourceRole.setModelResourceTemplateId(type);
            modelResourceRole.setRoleId(modelResourceRoleInsertDto.getRoleId());
            modelResourceRole.setCreateTime(DateUtils.getNowDate());
            modelResourceRole.setCreateBy(SecurityUtils.getUsername());
            modelResourceRoleMapper.insertModelResourceRole(modelResourceRole);
        }
        return 1;
    }

    /**
     * 修改角色模型资源
     *
     * @param modelResourceRoleUpdateDto 角色模型资源
     * @return 结果
     */
    @Override
    public int updateModelResourceRole(ModelResourceRoleUpdateDto modelResourceRoleUpdateDto) {
        modelResourceRoleMapper.deleteModelResourceRoleByRoleId(modelResourceRoleUpdateDto.getRoleId());
        for (Long type : modelResourceRoleUpdateDto.getModelResourceTemplateIds()) {
            ModelResourceRole modelResourceRole = new ModelResourceRole();
            modelResourceRole.setModelResourceTemplateId(type);
            modelResourceRole.setRoleId(modelResourceRoleUpdateDto.getRoleId());
            modelResourceRole.setCreateTime(DateUtils.getNowDate());
            modelResourceRole.setCreateBy(SecurityUtils.getUsername());
            modelResourceRoleMapper.insertModelResourceRole(modelResourceRole);
        }
        return 1;
    }

    /**
     * 批量删除角色模型资源
     *
     * @param ids 需要删除的角色模型资源主键
     * @return 结果
     */
    @Override
    public int deleteModelResourceRoleByIds(List<Long> ids) {
        return modelResourceRoleMapper.deleteModelResourceRoleByIds(ids);
    }

    /**
     * 删除角色模型资源信息
     *
     * @param id 角色模型资源主键
     * @return 结果
     */
    @Override
    public int deleteModelResourceRoleById(Long id) {
        return modelResourceRoleMapper.deleteModelResourceRoleById(id);
    }

    @Override
    public Set<String> getModelResources(SysUser user) {
        Set<String> permsSet = new HashSet<>();
        if (user.isAdmin()) {
            permsSet.add("all");
        } else {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                for (SysRole role : roles) {
                    Set<String> modelResources = modelResourceRoleMapper.getModelResources(role.getRoleId());
                    for (String res : modelResources) {
                        if (StringUtils.isNotEmpty(res)) {
                            permsSet.addAll(Arrays.asList(res.trim().split(",")));
                        }
                    }
                }
            }
        }
        return permsSet;
    }

    @Override
    public Set<String> getModelResources(Long roleId) {
        return modelResourceRoleMapper.getModelResources(roleId);
    }

}
