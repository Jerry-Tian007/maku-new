package com.ruoyi.gpt.controller;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gpt.configuration.ChatCompletionRequest;
import com.ruoyi.gpt.configuration.EventType;
import com.ruoyi.gpt.service.IEmitService;
import com.ruoyi.gpt.service.impl.KbQARecordServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 事件源聊天监听器
 * 用于处理从OpenAI API返回的服务器发送事件(SSE)
 * @author ruoyi
 */
@Slf4j
@Data
@RestController("/bb")
public class EventSourceChatListener extends EventSourceListener {

    /**
     * 发射服务接口
     */
    private IEmitService iEmitService;

    /**
     * Redis缓存服务
     */
    private RedisCache redisCache;

    /**
     * 倒计时锁存器，用于等待事件完成
     */
    @Getter
    @Setter
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 聊天完成请求对象
     */
    private ChatCompletionRequest request;

    /**
     * 会话ID映射表
     */
    ConcurrentHashMap<String, String> sessionIdA = new ConcurrentHashMap<>();

    /**
     * SSE发射器映射表
     */
    ConcurrentHashMap<String, SseEmitter> emit = new ConcurrentHashMap<>();

    /**
     * 知识库问答记录服务实现
     */
    private KbQARecordServiceImpl kbQARecordService;

    /**
     * HTTP响应对象
     */
    private HttpServletResponse rp;

    /**
     * 输出缓冲区
     */
    private StringBuffer output = new StringBuffer();

    /**
     * 构造函数
     * @param response HTTP响应对象
     */
    public EventSourceChatListener(HttpServletResponse response) {
        this.rp = response;
    }

    /**
     * 设置请求和响应对象
     * @param request 聊天完成请求对象
     * @param response HTTP响应对象
     */
    public void setRequest(ChatCompletionRequest request, HttpServletResponse response) {
        this.request = request;
        this.rp = response;
    }

    /**
     * 构造函数
     * @param redisCache Redis缓存服务
     * @param kbQARecordService 知识库问答记录服务
     * @param iEmitService 发射服务
     */
    @Autowired
    public EventSourceChatListener(RedisCache redisCache, KbQARecordServiceImpl kbQARecordService, IEmitService iEmitService) {
        this.redisCache = redisCache;
        this.kbQARecordService = kbQARecordService;
        this.iEmitService = iEmitService;
    }

    /**
     * 处理接收到的事件
     * @param eventSource 事件源
     * @param sessionId 会话ID
     * @param type 事件类型
     * @param data 事件数据
     * 
     * 处理流程:
     * 1. 将数据添加到输出缓冲区
     * 2. 如果是结束事件(finish)，处理会话完成逻辑
     * 3. 向客户端发送事件通知
     * 4. 记录日志并释放倒计时锁存器
     */
    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String sessionId, @Nullable String type, String data) {
        try {
            // 将数据添加到输出缓冲区
            output.append(data);
            
            // 如果是结束事件
            if (EventType.finish.getCode().equals(type)) {
                // 标记会话为已完成
                sessionIdA.put(sessionId, "false");

                // 向客户端发送事件通知
                rp.getWriter().write("event:" + type + "\n");
                rp.getWriter().write("sessionId:" + sessionId + "\n");
                log.info("on time: " + DateUtils.dateTimeNow());
                rp.getWriter().flush();
                
                // 此处注释了保存问答记录的代码
//                KbQARecordDto kbQARecordDto = new KbQARecordDto();
//                kbQARecordDto.setSessionId(sessionId);
//                kbQARecordDto.setUserId(request.getUserId());
//                kbQARecordDto.setCreateTime(DateUtils.getNowDate());
//                kbQARecordDto.setQuestion(sessionIdQ.get(sessionId));
//                kbQARecordDto.setAnswer(sessionIdA.get(sessionId));
//                kbQARecordService.insertKbQARecord(kbQARecordDto);

                log.info("请求结束");
                // 释放倒计时锁存器，允许等待线程继续执行
                countDownLatch.countDown();
            }

            if (rp != null && sessionIdA.get(sessionId) != "false" && EventType.add.getCode().equals(type)) {
                log.info("请求开始");
                if ("\n".equals(data)) {
                    rp.getWriter().write("event:" + type + "\n");
                    rp.getWriter().write("sessionId:" + sessionId + "\n");
                    rp.getWriter().write("data:\n\n");
                    rp.getWriter().flush();
                    log.info("on time: " + DateUtils.dateTimeNow() + "data is " + "换行换行");
                } else {
                    String[] dataArr = data.split("\\n");
                    for (int i = 0; i < dataArr.length; i++) {
                        if (i == 0) {
                            rp.getWriter().write("event:" + type + "\n");
                            rp.getWriter().write("sessionId:" + sessionId + "\n");
                        }
                        if (i == dataArr.length - 1) {
                            rp.getWriter().write("data:" + dataArr[i] + "\n\n");
                            rp.getWriter().flush();
                        } else {
                            rp.getWriter().write("data:" + dataArr[i] + "\n");
                            rp.getWriter().flush();
                        }
//                        assert sessionId != null;
//                        if (redisCache.getCacheObject(sessionId) == null) {
//                            redisCache.setCacheObject(sessionId, dataArr[i]);
//                        } else {
//                            StringBuffer temp = redisCache.getCacheObject(sessionId);
//                            temp.append(dataArr[i]);
//                            redisCache.deleteObject(sessionId);
//                            redisCache.setCacheObject(sessionId, temp);
//                        }
                        log.info("on time: " + DateUtils.dateTimeNow() + "data is " + dataArr[i]);
                    }


                }
            } else {

                countDownLatch.countDown();
            }
        } catch (IOException e) {
            countDownLatch.countDown();
            throw new RuntimeException(e);
        }
    }

//        if (EventType.finish.getCode().equals(type)) {
//            // 结束对话
//            log.info("data is" + data);
//
//
//            KbQARecordDto kbQARecordDto = new KbQARecordDto();
//
////            CompletableFuture.runAsync(() -> {
////                try {
//                    if (sessionIdA.containsKey(sessionId) )
//                    //如果finish了就存，这时候还有key
//                    {
//
//                        kbQARecordDto.setSessionId(sessionId);
//                        kbQARecordDto.setAnswer(sessionIdA.get(sessionId).toString());
//                        System.out.println(sessionId);
//                        kbQARecordDto.setQuestion(SSEChat.sessionIdQ.get(sessionId));
//
//                        kbQARecordDto.setUserId(request.getUserId());
//                        kbQARecordDto.setCreateTime(DateUtils.getNowDate());
//
//                        System.out.println("***" + sessionId + "***");
//
//                        kbQARecordService.insertKbQARecordKbqa(kbQARecordDto);
//
//                        sessionIdA.removeBatch(sessionId);
//                        countDownLatch.countDown();
//                    } else {
//                        return ;
//                    }
////                } catch (Exception e) {
////                    log.error("插入数据异常", e);
////                }
//
//                log.info("[输出结束]");
//
////            });
//        } else if (EventType.add.getCode().equals(type)) {
//
//
////            final SseEmitter emitter = iEmitService.getConn(sessionId);
////            CompletableFuture.runAsync(() -> {
////                try {
////                    iEmitService.send(sessionId, type, data);
////                    assert sessionId != null;
//            if (sessionIdA.containsKey(sessionId)) {
//                // 如果存在这个sessionId，则追加
//                sessionIdA.get(sessionId).append(data);
//                final SseEmitter emitter = iEmitService.getConn(sessionId);
//                assert sessionId != null;
//                System.out.println("emitter is " + emitter);
//                SSEChat.emit.put(sessionId, emitter);
//                System.out.println("sseChat emit is" + emitter + sessionId);
//                iEmitService.send(sessionId, type, data);
//            } else {
//                // 否则新建一个
//                sessionIdA.put(sessionId, new StringBuffer().append(data));
//            }
//            log.info(data);
////                } catch (Exception e) {
////                    // 这里你可以选择记录异常，或者根据需要进行其他处理。
////                    log.error("推送数据异常", e);
////                    throw new RuntimeException("推送数据异常", e);
////                }
////            });
//
//        } else {
//            // 处理错误或空响应
//            log.info("错误");
//        }


    @Override
    public void onClosed(@NotNull EventSource eventSource) {
//        log.info("outPut is ******************" + outPut);
        log.info("对话完成");
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("已经连接");
        rp.setContentType("text/event-stream");
        rp.setCharacterEncoding("UTF-8");
//        rp.setStatus(200);
        super.onOpen(eventSource, response);
    }

    @Override
    public void onFailure(EventSource eventSource, @NotNull  Throwable t, Response response) {
        log.info("连接失败");
        log.info(String.valueOf(t.fillInStackTrace()));
        log.info(response.message());
    }

}