package com.ruoyi.gpt.controller.chat;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import com.ruoyi.gpt.service.IChatModelSessionService;
import com.ruoyi.gpt.model.dto.chat_model_session.*;
import com.ruoyi.gpt.model.vo.chat_model_session.*;

import java.util.List;

/**
 * 
 *
 * @author Eric
 */
@RestController
@RequestMapping("/chat-model-session")
public class ChatModelSessionController  extends BaseController {
    @Autowired
    private IChatModelSessionService iChatModelSessionService;

    /**
     * 创建
     *
     * @param chatModelSessionAddDto
     */
    @PreAuthorize("@ss.hasPermi('chat-model-session:insert')")
    @Log(title = "创建", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public R<Integer> add(@RequestBody @Validated ChatModelSessionAddDto chatModelSessionAddDto) {
        return R.success(iChatModelSessionService.add(chatModelSessionAddDto));
    }




    /**
     * 更新
     *
     * @param chatModelSessionUpdateDto
     */
    @PreAuthorize("@ss.hasPermi('chat-model-session:update')")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public R<Integer> update(@RequestBody @Validated ChatModelSessionUpdateDto chatModelSessionUpdateDto) {
        return R.success(iChatModelSessionService.update(chatModelSessionUpdateDto));
    }

    /**
     * 删除
     *
     * @param id
     */
    @PreAuthorize("@ss.hasPermi('chat-model-session:delete')")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping(value = "/delete/{id}")
    public R<Integer> delete(@PathVariable("id") Long id) {
        return R.success(iChatModelSessionService.delete(id));
    }

    /**
     * 查询分页信息
     *
     * @param chatModelSessionPageQryDto
     */
    @PreAuthorize("@ss.hasPermi('chat-model-session:list')")
    @PostMapping(value = "/page-list")
    public PageDataInfo<ChatModelSessionPageVO> pageList(@RequestBody ChatModelSessionPageQryDto chatModelSessionPageQryDto) {
        startPage();
        return getPageData(iChatModelSessionService.pageList(chatModelSessionPageQryDto));
    }

    /**
    * 查询详情
     *
     * @param id
    */
    @PreAuthorize("@ss.hasPermi('chat-model-session:query')")
    @GetMapping(value = "/detail/{id}")
    public R<ChatModelSessionInfoVO> getById(@PathVariable("id") Long id) {
        return R.success(iChatModelSessionService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('chat-model-session:query')")
    @PostMapping(value = "/model-top-session")
    public PageDataInfo<ModelTopSessionVo> getModelTopSession(@RequestBody @Validated ModelTopSessionDto modelTopSessionDto) {
        startPage();
        return getPageData(iChatModelSessionService.getModelTopSession(modelTopSessionDto));
    }

    @PreAuthorize("@ss.hasPermi('chat-model-session:list')")
    @PostMapping(value = "/session/history")
    public R<List<SessionHistoryVo>> getSessionHistory(@RequestBody @Validated SessionHistoryDto sessionHistoryDto) {
        return R.success(iChatModelSessionService.getSessionHistory(sessionHistoryDto));
    }
}
