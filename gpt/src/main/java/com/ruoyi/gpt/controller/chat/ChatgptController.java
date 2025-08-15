package com.ruoyi.gpt.controller.chat;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;
import com.ruoyi.gpt.service.IChatgptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ruoyi.common.core.domain.AjaxResult.success;

@RestController
@RequestMapping("/chat/chatgpt")
public class ChatgptController {

    @Autowired
    private IChatgptService iChatgptService;

    @PreAuthorize("@ss.hasPermi('chat:chatgpt:chat')")
    @PostMapping("/chat")
    public R<JSONObject> chat(@RequestBody ModelChatApiDto modelChatApiDto) {
        return R.success(iChatgptService.chat(modelChatApiDto));
    }

}
