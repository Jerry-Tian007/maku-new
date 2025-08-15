package com.ruoyi.gpt.configuration;

import lombok.Data;

@Data
public class ChatCompletionResponse {

    private String data;
    private String meta;

    @Data
    public static class Meta {
        private String task_status;
        private String task_id;
        private String request_id;
    }

}
