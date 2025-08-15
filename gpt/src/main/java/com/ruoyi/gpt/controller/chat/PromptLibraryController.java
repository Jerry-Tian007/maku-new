package com.ruoyi.gpt.controller.chat;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibraryListDto;
import com.ruoyi.gpt.model.dto.prompt_library.PromptLibrarySelectListDto;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibraryListVo;
import com.ruoyi.gpt.model.vo.prompt_library.PromptLibrarySelectListVo;
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
import com.ruoyi.gpt.model.domain.PromptLibrary;
import com.ruoyi.gpt.service.IPromptLibraryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * promptLibraryController
 * 
 * @author Eric
 * @date 2023-07-31
 */
@RestController
@RequestMapping("/prompt/prompt-library")
public class PromptLibraryController extends BaseController
{
    @Autowired
    private IPromptLibraryService promptLibraryService;

    /**
     * 查询promptLibrary列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:list')")
    @GetMapping("/list")
    public PageDataInfo<PromptLibraryListVo> list(PromptLibraryListDto promptLibraryListDto)
    {
        startPage();
        List<PromptLibraryListVo> list = promptLibraryService.selectPromptLibraryList(promptLibraryListDto);
        return getPageData(list);
    }

    /**
     * 查询promptLibrary列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:list')")
    @GetMapping("/select-list")
    public R<List<PromptLibrarySelectListVo>> selectList(PromptLibrarySelectListDto promptLibrarySelectListDto)
    {
        List<PromptLibrarySelectListVo> list = promptLibraryService.selectPromptLibrarySelectList(promptLibrarySelectListDto);
        return R.success(list);
    }

    /**
     * 导出promptLibrary列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:export')")
    @Log(title = "promptLibrary", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromptLibraryListDto promptLibraryListDto)
    {
        List<PromptLibraryListVo> list = promptLibraryService.selectPromptLibraryList(promptLibraryListDto);
        ExcelUtil<PromptLibraryListVo> util = new ExcelUtil<PromptLibraryListVo>(PromptLibraryListVo.class);
        util.exportExcel(response, list, "promptLibrary数据");
    }

    /**
     * 获取promptLibrary详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promptLibraryService.selectPromptLibraryById(id));
    }

    /**
     * 新增promptLibrary
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:add')")
    @Log(title = "promptLibrary", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromptLibrary promptLibrary)
    {
//        PromptLibrary query = new PromptLibrary();
//        query.setUserId(SecurityUtils.getUserId());
//        List<PromptLibrary> promptLibraries = promptLibraryService.selectPromptLibraryList(query);
//        if (promptLibraries.size() == 30){
//            return  AjaxResult.error("您的prompt数以达到上线请删除后重试");
//        }
        return success(promptLibraryService.insertPromptLibrary(promptLibrary));
    }

    /**
     * 修改promptLibrary
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:edit')")
    @Log(title = "promptLibrary", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody PromptLibrary promptLibrary)
    {
        return success(promptLibraryService.updatePromptLibrary(promptLibrary));
    }

    /**
     * 删除promptLibrary
     */
    @PreAuthorize("@ss.hasPermi('gpt:promptlibrary:remove')")
    @Log(title = "promptLibrary", businessType = BusinessType.DELETE)
	@PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return success(promptLibraryService.deletePromptLibraryByIds(ids));
    }
}
