package com.ruoyi.gpt.controller.prompt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManagePublishDto;
import com.ruoyi.gpt.model.dto.prompt_manage.PromptManageSelectDto;
import com.ruoyi.gpt.service.IPromptManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 提示词管理Controller
 * 
 * @author Eric
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/prompt/manage")
public class PromptManageController extends BaseController
{
    @Autowired
    private IPromptManageService promptManageService;

    /**
     * 查询提示词列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:list')")
    @GetMapping("/list")
    public TableDataInfo list(PromptManage promptManage)
    {
        startPage();
        List<PromptManage> list = promptManageService.selectPromptManageList(promptManage);
        return getDataTable(list);
    }

    /**
     * 导出提示词列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:export')")
    @Log(title = "promptManage", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromptManage promptManage)
    {
        List<PromptManage> list = promptManageService.selectPromptManageList(promptManage);
        ExcelUtil<PromptManage> util = new ExcelUtil<PromptManage>(PromptManage.class);
        util.exportExcel(response, list, "promptManage数据");
    }

    /**
     * 获取提示词详细信息
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promptManageService.selectPromptManageById(id));
    }

    /**
     * 新增提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:add')")
    @Log(title = "promptManage", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromptManage promptManage)
    {
        return toAjax(promptManageService.insertPromptManage(promptManage));
    }

    /**
     * 修改提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:edit')")
    @Log(title = "promptManage", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody PromptManage promptManage)
    {
        return toAjax(promptManageService.updatePromptManage(promptManage));
    }

    /**
     * 删除提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:remove')")
    @Log(title = "promptManage", businessType = BusinessType.DELETE)
	@PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(promptManageService.deletePromptManageByIds(ids));
    }

    /**
     * 发布提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:publish')")
    @Log(title = "promptManage", businessType = BusinessType.UPDATE)
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody PromptManagePublishDto promptManagePublishDto)
    {
        return toAjax(promptManageService.publishPromptManage(promptManagePublishDto));
    }

    /**
     * 精选提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:select')")
     @Log(title = "promptManage", businessType = BusinessType.UPDATE)
    @PostMapping("/select")
    public AjaxResult select(@RequestBody PromptManageSelectDto promptManageSelectDto)
    {
        return toAjax(promptManageService.selectPromptManage(promptManageSelectDto));
    }
    /**
     * 提示词icon上传
     */
    @PreAuthorize("@ss.hasPermi('prompt:manage:upload')")
    @Log(title = "promptGroup", businessType = BusinessType.OTHER)
    @PostMapping("/uploadIcon")
    public AjaxResult uploadIcon(@RequestParam("file") MultipartFile file)
    {
        List<String> range = new ArrayList<>(Arrays.asList("jpg", "png", "jpeg"));
        if (!range.contains(FileNameUtils.getFileExtension(file.getOriginalFilename()))) {
            return AjaxResult.error("文件格式不支持");
        }
        return AjaxResult.success(promptManageService.uploadIcon(file));
    }

}
