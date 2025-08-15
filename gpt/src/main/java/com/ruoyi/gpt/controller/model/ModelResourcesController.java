package com.ruoyi.gpt.controller.model;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesInsertDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesListDto;
import com.ruoyi.gpt.model.dto.model_resources.ModelResourcesUpdateDto;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesAuthorizeUseVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesListVo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.domain.ModelResources;
import com.ruoyi.gpt.service.IModelResourcesService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 模型资源管理Controller
 *
 * @author Eric
 * @date 2024-07-09
 */
@RestController
@RequestMapping("/model/model-resources")
public class ModelResourcesController extends BaseController {
    @Autowired
    private IModelResourcesService modelResourcesService;

    /**
     * 查询模型资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:list')")
    @PostMapping("/list")
    public PageDataInfo<ModelResourcesListVo> list(@RequestBody ModelResourcesListDto modelResourcesListDto) {
        startPage();
        List<ModelResourcesListVo> list = modelResourcesService.selectModelResourcesListForPlatform(modelResourcesListDto);
        return getPageData(list);
    }

    /**
     * 查询当前用户授权的模型资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:list')")
    @GetMapping("/authorize/list")
    public R<List<ModelResourcesAuthorizeUseVo>> authorizeModelResourcesList() {
        List<ModelResourcesAuthorizeUseVo> list = modelResourcesService.getAuthorizeModelResourcesList();
        return R.success(list);
    }

    /**
     * 获取模型资源管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:query')")
    @GetMapping(value = "/{id}")
    public R<ModelResources> getInfo(@PathVariable("id") Long id) {
        return R.success(modelResourcesService.selectModelResourcesById(id));
    }

    /**
     * 新增模型资源管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:add')")
    @Log(title = "模型资源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody ModelResourcesInsertDto modelResourcesInsertDto) {
        return R.success(modelResourcesService.insertModelResources(modelResourcesInsertDto));
    }

    /**
     * 修改模型资源管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:edit')")
    @Log(title = "模型资源管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Integer> edit(@Validated @RequestBody ModelResourcesUpdateDto modelResourcesUpdateDto) {
        return R.success(modelResourcesService.updateModelResources(modelResourcesUpdateDto));
    }

    /**
     * 删除模型资源管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resources:remove')")
    @Log(title = "模型资源管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R<Integer> remove(@RequestBody Long[] ids) {
        return R.success(modelResourcesService.deleteModelResourcesByIds(ids));
    }
}
