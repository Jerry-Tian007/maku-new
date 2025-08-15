package com.ruoyi.gpt.controller.model;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.gpt.model.vo.model_resources.ModelResourcesSelectVo;
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
import com.ruoyi.gpt.model.domain.ModelResourceTemplate;
import com.ruoyi.gpt.service.IModelResourceTemplateService;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateListDto;
import com.ruoyi.gpt.model.vo.model_resource_template.ModelResourceTemplateListVo;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateInsertDto;
import com.ruoyi.gpt.model.dto.model_resource_template.ModelResourceTemplateUpdateDto;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 模型资源模板管理Controller
 *
 * @author Eric
 * @date 2024-07-15
 */
@RestController
@RequestMapping("/model/model-resource-template")
public class ModelResourceTemplateController extends BaseController {
    @Autowired
    private IModelResourceTemplateService modelResourceTemplateService;

    /**
     * 查询模型资源模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:list')")
    @PostMapping("/list")
    public PageDataInfo<ModelResourceTemplateListVo> list(@RequestBody ModelResourceTemplateListDto modelResourceTemplateDto) {
        startPage();
        List<ModelResourceTemplateListVo> list = modelResourceTemplateService.selectModelResourceTemplateListForPlatform(modelResourceTemplateDto);
        return getPageData(list);
    }

    /**
     * 获取模型资源模板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:query')")
    @GetMapping(value = "/{id}")
    public R<ModelResourceTemplate> getInfo(@PathVariable("id") Long id) {
        return R.success(modelResourceTemplateService.selectModelResourceTemplateById(id));
    }

    /**
     * 新增模型资源模板管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:add')")
    @Log(title = "模型资源模板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody ModelResourceTemplateInsertDto modelResourceTemplateInsertDto) {
        return R.success(modelResourceTemplateService.insertModelResourceTemplate(modelResourceTemplateInsertDto));
    }

    /**
     * 修改模型资源模板管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:edit')")
    @Log(title = "模型资源模板管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Integer> edit(@RequestBody ModelResourceTemplateUpdateDto modelResourceTemplateUpdateDto) {
        return R.success(modelResourceTemplateService.updateModelResourceTemplate(modelResourceTemplateUpdateDto));
    }

    /**
     * 删除模型资源模板管理
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:remove')")
    @Log(title = "模型资源模板管理", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.success(modelResourceTemplateService.deleteModelResourceTemplateByIds(ids));
    }

    /**
     * 获取角色模型资源下拉树列表
     */
    @PreAuthorize("@ss.hasPermi('model:model-resource-template:query')")
    @GetMapping("/getSelect")
    public R<List<ModelResourcesSelectVo>> getSelect() {
        return R.success(modelResourceTemplateService.getSelect());
    }
}
