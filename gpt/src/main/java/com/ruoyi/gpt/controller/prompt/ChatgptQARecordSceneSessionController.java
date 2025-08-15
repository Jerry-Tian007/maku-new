package com.ruoyi.gpt.controller.prompt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.ChatgptQARecordSceneSession;
import com.ruoyi.gpt.model.dto.chatgpt_q_a_record_scene_session.ChatgptQARecordSceneSessionAddDto;
import com.ruoyi.gpt.service.IChatgptQARecordSceneSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 场景问答会话Controller
 * 
 * @author Eric
 * @date 2024-10-08
 */
@RestController
@RequestMapping("/prompt/chatgptrecordscene/session")
public class ChatgptQARecordSceneSessionController extends BaseController
{
    @Autowired
    private IChatgptQARecordSceneSessionService chatgptQARecordSceneSessionService;

    /**
     * 查询场景问答会话列表
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:list')")
    @GetMapping("/list")
    public R<List<ChatgptQARecordSceneSession>> list(ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        List<ChatgptQARecordSceneSession> list = chatgptQARecordSceneSessionService.selectChatgptQARecordSceneSessionList(chatgptQARecordSceneSession);
        return R.success(list);
    }

    /**
     * 查询场景会话下的问答历史记录
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:list')")
    @GetMapping("/history")
    public R<List<ChatgptQARecord>> history(ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        List<ChatgptQARecord> list = chatgptQARecordSceneSessionService.selectChatgptQARecordSceneSessionHistpry(chatgptQARecordSceneSession);
        return R.success(list);
    }


    /**
     * 导出场景问答会话列表
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:export')")
    @Log(title = "场景问答会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        List<ChatgptQARecordSceneSession> list = chatgptQARecordSceneSessionService.selectChatgptQARecordSceneSessionList(chatgptQARecordSceneSession);
        ExcelUtil<ChatgptQARecordSceneSession> util = new ExcelUtil<ChatgptQARecordSceneSession>(ChatgptQARecordSceneSession.class);
        util.exportExcel(response, list, "场景问答会话数据");
    }

    /**
     * 获取场景问答会话详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatgptQARecordSceneSessionService.selectChatgptQARecordSceneSessionById(id));
    }

    /**
     * 新增场景问答会话
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:add')")
    @Log(title = "场景问答会话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatgptQARecordSceneSessionAddDto chatgptQARecordSceneSessionAddDto)
    {
        return toAjax(chatgptQARecordSceneSessionService.insertChatgptQARecordSceneSession(chatgptQARecordSceneSessionAddDto));
    }

    /**
     * 修改场景问答会话
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:edit')")
    @Log(title = "场景问答会话", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody ChatgptQARecordSceneSession chatgptQARecordSceneSession)
    {
        return toAjax(chatgptQARecordSceneSessionService.updateChatgptQARecordSceneSession(chatgptQARecordSceneSession));
    }

    /**
     * 删除场景问答会话
     */
    @PreAuthorize("@ss.hasPermi('chatgptrecordscene:session:remove')")
    @Log(title = "场景问答会话", businessType = BusinessType.DELETE)
	@PostMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatgptQARecordSceneSessionService.deleteChatgptQARecordSceneSessionByIds(ids));
    }
}
