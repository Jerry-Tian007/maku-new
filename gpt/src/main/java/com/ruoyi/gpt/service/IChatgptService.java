package com.ruoyi.gpt.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.gpt.model.dto.model_api.ModelChatApiDto;

public interface IChatgptService {
    JSONObject chat(ModelChatApiDto params);
}
