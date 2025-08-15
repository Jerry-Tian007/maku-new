package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQAChatDto;
import com.ruoyi.gpt.service.IKbQAChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * 知识库问答
 *
 * @author tiankk
 */
@RestController
@RequestMapping("/kb/qa")
public class KbQAChatController {

    @Autowired
    private IKbQAChatService iKbQAChatService;

    @PreAuthorize("@ss.hasPermi('kb:qa:chat')")
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody KbQAChatDto kbQAChatDto) {
        return success(iKbQAChatService.chat(kbQAChatDto));
    }

}
