package com.ruoyi.gpt.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQAChatDto;

public interface IKbQAChatService {
    JSONObject chat(KbQAChatDto kbQAChatDto);
}
