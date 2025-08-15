package com.ruoyi.gpt.controller.chat;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.UserChatHistory;
import com.ruoyi.gpt.service.IUserChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * chatController
 *
 * @author yecao
 * @date 2023-05-19
 */
@RestController
@RequestMapping("/chat/history")
public class UserChatHistoryController extends BaseController {
    @Autowired
    private IUserChatHistoryService userChatHistoryService;

    /**
     * 查询chat列表
     */
    @PreAuthorize("@ss.hasPermi('chat:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserChatHistory userChatHistory) {
        startPage();
        List<UserChatHistory> list = userChatHistoryService.selectUserChatHistoryList(userChatHistory);
        return getDataTable(list);
    }

    /**
     * 导出chat列表
     */
    @PreAuthorize("@ss.hasPermi('chat:history:export')")
    @Log(title = "chat", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserChatHistory userChatHistory) {
        List<UserChatHistory> list = userChatHistoryService.selectUserChatHistoryList(userChatHistory);
        ExcelUtil<UserChatHistory> util = new ExcelUtil<UserChatHistory>(UserChatHistory.class);
        util.exportExcel(response, list, "chat数据");
    }

    /**
     * 获取chat详细信息
     */
    @PreAuthorize("@ss.hasPermi('chat:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userChatHistoryService.selectUserChatHistoryById(id));
    }

    /**
     * 新增chat
     */
    @PreAuthorize("@ss.hasPermi('chat:history:add')")
    @Log(title = "chat", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserChatHistory userChatHistory) {
        return AjaxResult.success(userChatHistoryService.insertUserChatHistory(userChatHistory));
    }

    /**
     * 修改chat
     */
    @PreAuthorize("@ss.hasPermi('chat:history:edit')")
    @Log(title = "chat", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserChatHistory userChatHistory) {
        return toAjax(userChatHistoryService.updateUserChatHistory(userChatHistory));
    }

    /**
     * 删除chat
     */
    @PreAuthorize("@ss.hasPermi('chat:history:remove')")
    @Log(title = "chat", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userChatHistoryService.deleteUserChatHistoryByIds(ids));
    }
}
