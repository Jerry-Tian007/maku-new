package com.ruoyi.gpt.controller.prompt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.PromptModelManage;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageAddDto;
import com.ruoyi.gpt.model.dto.Prompt_model_manage.PromptModelManageListDto;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelManageListVo;
import com.ruoyi.gpt.model.vo.prompt_model_manage.PromptModelUnManageListVo;
import com.ruoyi.gpt.service.IPromptModelManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 模型场景管理Controller
 * 
 * @author Eric
 * @date 2024-09-19
 */
@RestController
@RequestMapping("/prompt/model-manage")
public class PromptModelManageController extends BaseController
{
    @Autowired
    private IPromptModelManageService promptModelManageService;

    /**
     * 查询模型场景管理列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:list')")
    @GetMapping("/list")
    public TableDataInfo list(PromptModelManage promptModelManage)
    {
        startPage();
        List<PromptModelManage> list = promptModelManageService.selectPromptModelManageList(promptModelManage);
        return getDataTable(list);
    }

    /**
     * 查询模型已添加场景
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:list')")
    @GetMapping("/manage-list")
    public TableDataInfo manageList(PromptModelManageListDto promptModelManageListDto)
    {
        startPage();
        List<PromptModelManageListVo> list = promptModelManageService.selectPromptManageByModel(promptModelManageListDto);
        return getDataTable(list);
    }

    /**
     * 查询模型未添加场景
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:list')")
    @GetMapping("/un-manage-list")
    public TableDataInfo unManageList(PromptModelManageListDto promptModelManageListDto)
    {
        startPage();
        List<PromptModelUnManageListVo> list = promptModelManageService.selectPromptUnManageByModel(promptModelManageListDto);
        return getDataTable(list);
    }

    /**
     * 导出模型场景管理列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:export')")
    @Log(title = "模型场景管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromptModelManage promptModelManage)
    {
        List<PromptModelManage> list = promptModelManageService.selectPromptModelManageList(promptModelManage);
        ExcelUtil<PromptModelManage> util = new ExcelUtil<PromptModelManage>(PromptModelManage.class);
        util.exportExcel(response, list, "模型场景管理数据");
    }

    /**
     * 获取模型场景管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promptModelManageService.selectPromptModelManageById(id));
    }

    /**
     * 新增模型场景管理
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:add')")
    @Log(title = "模型场景管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromptModelManageAddDto promptModelManageAddDto)
    {
        return toAjax(promptModelManageService.insertPromptModelManage(promptModelManageAddDto));
    }

    /**
     * 修改模型场景管理
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:edit')")
    @Log(title = "模型场景管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody PromptModelManage promptModelManage)
    {
        return toAjax(promptModelManageService.updatePromptModelManage(promptModelManage));
    }

    /**
     * 删除模型场景管理
     */
    @PreAuthorize("@ss.hasPermi('prompt:model-manage:remove')")
    @Log(title = "模型场景管理", businessType = BusinessType.DELETE)
	@PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(promptModelManageService.deletePromptModelManageByIds(ids));
    }
}
