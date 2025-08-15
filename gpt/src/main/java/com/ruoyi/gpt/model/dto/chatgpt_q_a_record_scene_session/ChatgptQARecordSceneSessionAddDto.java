package com.ruoyi.gpt.model.dto.chatgpt_q_a_record_scene_session;

import lombok.Data;

@Data
public class ChatgptQARecordSceneSessionAddDto {
    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String sessionTitle;

    private Long recordId;

}
