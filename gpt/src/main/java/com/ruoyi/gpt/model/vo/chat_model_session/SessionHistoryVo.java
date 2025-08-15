package com.ruoyi.gpt.model.vo.chat_model_session;

import lombok.Data;
import org.springframework.security.core.parameters.P;

@Data
public class SessionHistoryVo {
    private Long id;

    private String question;

    private String answer;

    private String isAccuracy;

    private String model;

    private String reasoningContent;

    private String linkAndTitle;

    private Integer completionTokens;

}
