package com.ruoyi.gpt.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

public interface IEmitService {

    public void send(@NotBlank String sessionId, String event, String data);

    void send(@NotBlank String sessionId, String event);

    public void closeConn(@NotBlank String sessionId);


    public SseEmitter getConn(@NotBlank String sessionId);
}
