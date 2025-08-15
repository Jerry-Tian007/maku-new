package com.ruoyi.gpt.controller;


import com.ruoyi.gpt.service.IEmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/emit/")
public class EmitController {
    
    @Resource
    private IEmitService iEmitService;

    @GetMapping(value = "test666/{sessionId}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter test(@PathVariable("sessionId") String sessionId) {
        final SseEmitter emitter = iEmitService.getConn(sessionId);
        CompletableFuture.runAsync(() -> {
            try {
                iEmitService.send(sessionId, "", "");
            } catch (Exception e) {
                // 这里你可以选择记录异常，或者根据需要进行其他处理。
                log.error("推送数据异常", e);
                throw new RuntimeException("推送数据异常", e);
            }
        });

        return emitter;
    }


    @GetMapping("closeConn/{sessionid}")
    public Result<String> closeConn(@PathVariable("sessionid") String sessionid) {
        iEmitService.closeConn(sessionid);
        return Result.response(Response.success("连接已关闭"));
    }
}
