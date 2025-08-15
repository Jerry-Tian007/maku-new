package com.ruoyi.gpt.controller.model;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.domain.ModelResourceRole;
import com.ruoyi.gpt.service.IModelResourceRoleService;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleListDto;
import com.ruoyi.gpt.model.vo.model_resource_role.ModelResourceRoleListVo;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_role.ModelResourceRoleUpdateDto;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色模型资源Controller
 *
 * @author Eric
 * @date 2024-07-22
 */
@RestController
@RequestMapping("/model/model-resource-role")
public class ModelResourceRoleController extends BaseController {
    @Autowired
    private IModelResourceRoleService modelResourceRoleService;

    /**
     * 查询角色模型资源列表
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:list')")
    @PostMapping("/list")
    public PageDataInfo<ModelResourceRole> list(@RequestBody ModelResourceRole modelResourceRole) {
        startPage();
        List<ModelResourceRole> list = modelResourceRoleService.selectModelResourceRoleList(modelResourceRole);
        return getPageData(list);
    }

    /**
     * 查询角色模型资源列表-平台
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:list')")
    @PostMapping("/platform-list")
    public PageDataInfo<ModelResourceRoleVo> platformList(@RequestBody ModelResourceRoleListDto modelResourceRoleDto) {
        startPage();
        List<ModelResourceRoleVo> list = modelResourceRoleService.selectModelResourceRoleListForPlatform(modelResourceRoleDto);
        return getPageData(list);
    }

    /**
     * 获取角色模型资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:query')")
    @GetMapping(value = "/{id}")
    public R<ModelResourceRole> getInfo(@PathVariable("id") Long id) {
        return R.success(modelResourceRoleService.selectModelResourceRoleById(id));
    }

    /**
     * 新增角色模型资源
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:add')")
    @Log(title = "角色模型资源", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody ModelResourceRoleInsertDto modelResourceRoleInsertDto) {
        return R.success(modelResourceRoleService.insertModelResourceRole(modelResourceRoleInsertDto));
    }

    /**
     * 修改角色模型资源
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:edit')")
    @Log(title = "角色模型资源", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Integer> edit(@RequestBody ModelResourceRoleUpdateDto modelResourceRoleUpdateDto) {
        return R.success(modelResourceRoleService.updateModelResourceRole(modelResourceRoleUpdateDto));
    }

    /**
     * 获取角色模型资源集合
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-role:query')")
    @GetMapping("/getModelListByRoleId/{roleId}")
    public R<Set<String>> getModelListByRoleId(@PathVariable("roleId") Long roleId) {
        return R.success(modelResourceRoleService.getModelResources(roleId));
    }
}
