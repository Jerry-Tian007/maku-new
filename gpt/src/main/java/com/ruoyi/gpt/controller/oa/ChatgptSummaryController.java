package com.ruoyi.gpt.controller.oa;

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
import com.ruoyi.gpt.model.domain.ChatgptSummary;
import com.ruoyi.gpt.service.IChatgptSummaryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * gpt总结Controller
 * 
 * @author Eric
 * @date 2023-09-26
 */
@RestController
@RequestMapping("/gpt/chatgptsummary")
public class ChatgptSummaryController extends BaseController
{
    @Autowired
    private IChatgptSummaryService chatgptSummaryService;

    /**
     * 查询gpt总结列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ChatgptSummary chatgptSummary)
    {
        startPage();
        List<ChatgptSummary> list = chatgptSummaryService.selectChatgptSummaryList(chatgptSummary);
        return getDataTable(list);
    }

    /**
     * 查询gpt24小时列表
     */
    @GetMapping("/list24")
    public TableDataInfo list24(ChatgptSummary chatgptSummary)
    {
        startPage();
        List<ChatgptSummary> list = chatgptSummaryService.selectUser24HourChatHistoryList(chatgptSummary);
        return getDataTable(list);
    }

    /**
     * 导出gpt总结列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptsummary:export')")
    @Log(title = "gpt总结", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatgptSummary chatgptSummary)
    {
        List<ChatgptSummary> list = chatgptSummaryService.selectChatgptSummaryList(chatgptSummary);
        ExcelUtil<ChatgptSummary> util = new ExcelUtil<ChatgptSummary>(ChatgptSummary.class);
        util.exportExcel(response, list, "gpt总结数据");
    }

    /**
     * 获取gpt总结详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptsummary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatgptSummaryService.selectChatgptSummaryById(id));
    }

    /**
     * 新增gpt总结
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptsummary:add')")
    @Log(title = "gpt总结", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatgptSummary chatgptSummary)
    {
        return toAjax(chatgptSummaryService.insertChatgptSummary(chatgptSummary));
    }

    /**
     * 修改gpt总结
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptsummary:edit')")
    @Log(title = "gpt总结", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatgptSummary chatgptSummary)
    {
        return toAjax(chatgptSummaryService.updateChatgptSummary(chatgptSummary));
    }

    /**
     * 删除gpt总结
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptsummary:remove')")
    @Log(title = "gpt总结", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatgptSummaryService.deleteChatgptSummaryByIds(ids));
    }

    @GetMapping(value = "/info/{identityId}")
    public AjaxResult getInfoByUserId(@PathVariable("identityId") Long identityId)
    {
        return AjaxResult.success(chatgptSummaryService.selectChatgptSummaryByIdentityId(identityId));
    }
}
