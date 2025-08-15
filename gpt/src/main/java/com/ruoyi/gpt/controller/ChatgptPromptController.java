package com.ruoyi.gpt.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.gpt.model.domain.ChatgptPrompt;
import com.ruoyi.gpt.service.IChatgptPromptService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * gpt提示语Controller
 * 
 * @author Eric
 * @date 2023-05-23
 */
@RestController
@RequestMapping("/gpt/prompt")
public class ChatgptPromptController extends BaseController
{
    @Autowired
    private IChatgptPromptService chatgptPromptService;

    /**
     * 查询gpt提示语列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatgptPrompt chatgptPrompt)
    {
        startPage();
        List<ChatgptPrompt> list = chatgptPromptService.selectChatgptPromptList(chatgptPrompt);
        return getDataTable(list);
    }

    /**
     * 查询gpt提示语列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:list')")
    @GetMapping("/allList")
    public AjaxResult allList(ChatgptPrompt chatgptPrompt)
    {
        List<ChatgptPrompt> list = chatgptPromptService.selectChatgptPromptList(chatgptPrompt);
        return AjaxResult.success(list);
    }

    /**
     * 导出gpt提示语列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:export')")
    @Log(title = "gpt提示语", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatgptPrompt chatgptPrompt)
    {
        List<ChatgptPrompt> list = chatgptPromptService.selectChatgptPromptList(chatgptPrompt);
        ExcelUtil<ChatgptPrompt> util = new ExcelUtil<ChatgptPrompt>(ChatgptPrompt.class);
        util.exportExcel(response, list, "gpt提示语数据");
    }

    /**
     * 获取gpt提示语详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatgptPromptService.selectChatgptPromptById(id));
    }

    /**
     * 新增gpt提示语
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:add')")
    @Log(title = "gpt提示语", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatgptPrompt chatgptPrompt)
    {
        return toAjax(chatgptPromptService.insertChatgptPrompt(chatgptPrompt));
    }

    /**
     * 修改gpt提示语
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:edit')")
    @Log(title = "gpt提示语", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatgptPrompt chatgptPrompt)
    {
        return toAjax(chatgptPromptService.updateChatgptPrompt(chatgptPrompt));
    }

    /**
     * 删除gpt提示语
     */
    @PreAuthorize("@ss.hasPermi('gpt:prompt:remove')")
    @Log(title = "gpt提示语", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatgptPromptService.deleteChatgptPromptByIds(ids));
    }
}
