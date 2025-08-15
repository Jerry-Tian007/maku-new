package com.ruoyi.gpt.controller.chat;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.service.IChatgptQARecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * chatgptController
 *
 * @author Eric
 * @date 2023-04-18
 */
@RestController
@RequestMapping("/gpt/chatgptrecord")
public class ChatgptQARecordController extends BaseController
{
    @Resource
    private IChatgptQARecordService chatgptQARecordService;

    /**
     * 查询chatgpt列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatgptQARecord chatgptQARecord)
    {
        startPage();
        List<ChatgptQARecord> list = chatgptQARecordService.selectChatgptQARecordList(chatgptQARecord);
        return getDataTable(list);
    }

    /**
     * 导出chatgpt列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:export')")
    @Log(title = "chatgpt", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatgptQARecord chatgptQARecord)
    {
        List<ChatgptQARecord> list = chatgptQARecordService.selectChatgptQARecordList(chatgptQARecord);
        ExcelUtil<ChatgptQARecord> util = new ExcelUtil<ChatgptQARecord>(ChatgptQARecord.class);
        util.exportExcel(response, list, "chatgpt数据");
    }

    /**
     * 获取chatgpt详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatgptQARecordService.selectChatgptQARecordById(id));
    }

    /**
     * 新增chatgpt
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:add')")
    @Log(title = "chatgpt", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatgptQARecord chatgptQARecord)
    {
        int i = chatgptQARecordService.insertChatgptQARecord(chatgptQARecord);
        return AjaxResult.success(chatgptQARecord.getId());
    }

    /**
     * 修改chatgpt
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:edit')")
    @Log(title = "chatgpt", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatgptQARecord chatgptQARecord)
    {
        return toAjax(chatgptQARecordService.updateChatgptQARecord(chatgptQARecord));
    }

    /**
     * 删除chatgpt
     */
    @PreAuthorize("@ss.hasPermi('gpt:chatgptrecord:remove')")
    @Log(title = "chatgpt", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatgptQARecordService.deleteChatgptQARecordByIds(ids));
    }
}
