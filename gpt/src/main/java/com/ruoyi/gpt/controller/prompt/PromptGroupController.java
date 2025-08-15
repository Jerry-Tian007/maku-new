package com.ruoyi.gpt.controller.prompt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileNameUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.PromptGroup;
import com.ruoyi.gpt.model.domain.PromptManage;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupPublishDto;
import com.ruoyi.gpt.model.dto.prompt_group.PromptGroupUnderManageDto;
import com.ruoyi.gpt.model.vo.prompt_group.PromptGroupHomeListVo;
import com.ruoyi.gpt.service.IPromptGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * promptGroupController
 * 
 * @author Eric
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/prompt/group")
public class PromptGroupController extends BaseController
{
    @Autowired
    private IPromptGroupService promptGroupService;

    /**
     * 查询promptGroup列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(PromptGroup promptGroup)
    {
        startPage();
        List<PromptGroup> list = promptGroupService.selectPromptGroupList(promptGroup);
        return getDataTable(list);
    }
    /**
     * 查询分类列表-主页
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:list')")
    @GetMapping("/home-list")
    public R<List<PromptGroupHomeListVo>> homeList(PromptGroup promptGroup)
    {
        List<PromptGroupHomeListVo> list = promptGroupService.selectPromptGroupHomeList(promptGroup);
        return R.success(list);
    }

    /**
     * 查询精选列表-主页
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:list')")
    @GetMapping("/home-select-list")
    public R<PromptGroupHomeListVo> homeSelectList(PromptGroup promptGroup)
    {
        PromptGroupHomeListVo list = promptGroupService.selectPromptGroupHomeSelectList(promptGroup);
        return R.success(list);
    }

    /**
     * 查询分类下拉列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:list')")
    @GetMapping("/select-list")
    public R<List<PromptGroup>> selectList(PromptGroup promptGroup)
    {
        List<PromptGroup> list = promptGroupService.selectPromptGroupSelectList(promptGroup);
        return R.success(list);
    }

    /**
     * 查询分类下所有提示词
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:list')")
    @GetMapping("/manage-list")
    public TableDataInfo underManageList(PromptGroupUnderManageDto promptGroupUnderManageDto)
    {
        startPage();
        List<PromptManage> list = promptGroupService.selectPromptGroupUnderManageList(promptGroupUnderManageDto);
        return getDataTable(list);
    }

    /**
     * 导出promptGroup列表
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:export')")
    @Log(title = "promptGroup", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromptGroup promptGroup)
    {
        List<PromptGroup> list = promptGroupService.selectPromptGroupList(promptGroup);
        ExcelUtil<PromptGroup> util = new ExcelUtil<PromptGroup>(PromptGroup.class);
        util.exportExcel(response, list, "promptGroup数据");
    }

    /**
     * 获取promptGroup详细信息
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promptGroupService.selectPromptGroupById(id));
    }

    /**
     * 新增promptGroup
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:add')")
    @Log(title = "promptGroup", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromptGroup promptGroup)
    {
        return toAjax(promptGroupService.insertPromptGroup(promptGroup));
    }

    /**
     * 修改promptGroup
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:edit')")
    @Log(title = "promptGroup", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody PromptGroup promptGroup)
    {
        return toAjax(promptGroupService.updatePromptGroup(promptGroup));
    }

    /**
     * 删除promptGroup
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:remove')")
    @Log(title = "promptGroup", businessType = BusinessType.DELETE)
	@PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(promptGroupService.deletePromptGroupByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('prompt:group:upload')")
    @Log(title = "promptGroup", businessType = BusinessType.OTHER)
    @PostMapping("/uploadIcon")
    public AjaxResult uploadIcon(@RequestParam("file") MultipartFile file)
    {
        List<String> range = new ArrayList<>(Arrays.asList("jpg", "png", "jpeg"));
        if (!range.contains(FileNameUtils.getFileExtension(file.getOriginalFilename()))) {
            return AjaxResult.error("文件格式不支持");
        }
        return AjaxResult.success(promptGroupService.uploadIcon(file));
    }

    /**
     * 发布提示词分类
     */
    @PreAuthorize("@ss.hasPermi('prompt:group:publish')")
    @Log(title = "promptGroup", businessType = BusinessType.UPDATE)
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody PromptGroupPublishDto promptGroupPublishDto)
    {
        return toAjax(promptGroupService.publishPromptGroup(promptGroupPublishDto));
    }

}
