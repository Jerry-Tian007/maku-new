package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.service.IEmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EmitServiceImpl implements IEmitService {

    private static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();





    @Override
    public SseEmitter getConn(@NotBlank String sessionId) {
        final SseEmitter sseEmitter = SSE_CACHE.get(sessionId);

        if (sseEmitter != null) {
            return sseEmitter;
        } else {
            // 设置连接超时时间，需要配合配置项 spring.mvc.async.request-timeout: 600000 一起使用  
            final SseEmitter emitter = new SseEmitter(0L);
            // 注册超时回调，超时后触发
            emitter.onTimeout(() -> {
                log.info("连接已超时，正准备关闭，sessionId = {}", sessionId);
                SSE_CACHE.remove(sessionId);
            });
            // 注册完成回调，调用 emitter.complete() 触发
            emitter.onCompletion(() -> {
                log.info("连接已关闭，正准备释放，sessionId = {}", sessionId);
                SSE_CACHE.remove(sessionId);
                log.info("连接已释放，sessionId = {}", sessionId);
            });
            // 注册异常回调，调用 emitter.completeWithError() 触发
            emitter.onError(throwable -> {
                log.error("连接已异常，正准备关闭，sessionId = {}", sessionId, throwable);
                SSE_CACHE.remove(sessionId);
            });

            SSE_CACHE.put(sessionId, emitter);

            return emitter;
        }
    }


      /**  模拟类似于 GPT 的流式推送回答
    yield f'event:add\nid:8201455865276140671\ndata:{"{}".format(char)}\n\n'

     */



    @Override
    public void send(@NotBlank String sessionId, String event, String data) {
        final SseEmitter emitter = SSE_CACHE.get(sessionId);
        // 推流内容到客户端  
        try {
            emitter.send(data + "\nsessionId:" + sessionId + "\nevent:" + event  + "\n\n");
//            emitter.send(new MySseEventBuilder().sessionId(sessionId).event(event).data(data).build() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 结束推流
//        emitter.complete();
    }

    @Override
    public void send(@NotBlank String sessionId, String event) {
        final SseEmitter emitter = SSE_CACHE.get(sessionId);
        // 推流内容到客户端
        try {
            emitter.send("" + "\nsessionId:" + sessionId + "\nevent:" + event  + "\n\n");
//            emitter.send(new MySseEventBuilder().sessionId(sessionId).event(event).build() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 结束推流
//        emitter.complete();
    }

    @Override
    public void closeConn(@NotBlank String sessionId) {
        final SseEmitter sseEmitter = SSE_CACHE.get(sessionId);
        if (sseEmitter != null) {
            SSE_CACHE.remove(sessionId);
            sseEmitter.complete();
        }
    }

}
