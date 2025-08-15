package com.ruoyi.gpt.configuration;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.gpt.controller.EventSourceChatListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * OpenAI会话的默认实现类
 * 负责与OpenAI API进行交互，处理聊天完成请求
 * @author ruoyi
 */
@Slf4j
public class DefaultOpenAiSession implements OpenAiSession {

    /**
     * OpenAI配置信息
     */
    private final OKConfiguration configuration;
    /**
     * EventSource工厂，用于创建事件源
     */
    private final EventSource.Factory factory;

    /**
     * 构造函数
     * @param configuration OpenAI配置对象
     */
    public DefaultOpenAiSession(OKConfiguration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }

    /**
     * 未实现的聊天完成方法
     * @param chatCompletionRequest 聊天完成请求
     * @param eventSourceListener 事件源监听器
     * @return EventSource对象
     * @throws JsonProcessingException JSON处理异常
     */
    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        return null;
    }

    /**
     * 处理聊天完成请求的同步方法
     * @param chatCompletionRequest 聊天完成请求对象
     * @param eventSourceChatListener 自定义事件源监听器
     * @throws JsonProcessingException JSON处理异常
     * @throws InterruptedException 线程中断异常
     * 
     * 调用流程:
     * 1. 构建HTTP请求
     * 2. 创建事件源并注册监听器
     * 3. 等待请求完成(通过CountDownLatch)
     */
    @Override
    public void completions(ChatCompletionRequest chatCompletionRequest, EventSourceChatListener eventSourceListener) throws JsonProcessingException, InterruptedException {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        System.out.println("request is " + request);

        // 创建事件源并注册监听器
        factory.newEventSource(request, eventSourceListener);
        // 等待请求完成
        eventSourceListener.getCountDownLatch().await();
    }

    /**
     * 处理聊天完成请求的异步方法
     * @param chatCompletionRequest 聊天完成请求对象
     * @return CompletableFuture<String> 包含响应结果的异步任务
     * @throws InterruptedException 线程中断异常
     * 
     * 调用流程:
     * 1. 创建CompletableFuture用于异步结果处理
     * 2. 构建HTTP请求
     * 3. 创建事件源并注册监听器处理响应
     * 4. 根据不同事件类型处理数据
     *    - add: 增量数据，追加到缓冲区
     *    - finish: 完成事件，将缓冲区数据作为结果返回
     *    - 异常情况: 完成异常
     */
    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        // 用于执行异步任务并获取结果
        CompletableFuture<String> future = new CompletableFuture<>();
        // 用于存储增量返回的数据
        StringBuffer dataBuffer = new StringBuffer();

        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        // 异步响应请求
        factory.newEventSource(request, new EventSourceListener() {
            /**
             * 处理接收到的事件
             * @param eventSource 事件源
             * @param id 事件ID
             * @param type 事件类型 (add, finish, error, interrupted)
             * @param data 事件数据
             */
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                // 解析JSON数据
                JSONObject entries = JSONUtil.parseObj(data);

                // 创建响应对象
                ChatCompletionResponse response = new ChatCompletionResponse();
                response.setData(String.valueOf(entries.getJSONObject("data")));
                if (entries.getJSONObject("meta") != null) {
                    response.setMeta(String.valueOf(entries.getJSONObject("meta")));
                }

                // 根据事件类型处理数据
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.add.getCode().equals(type)) {
                    // 增量数据，追加到缓冲区
                    dataBuffer.append(response.getData());
                } else if (EventType.finish.getCode().equals(type)) {
                    // 完成事件，将缓冲区数据作为结果返回
                    future.complete(dataBuffer.toString());
                }
            }

            /**
             * 连接关闭时调用
             * @param eventSource 事件源
             */
            @Override
            public void onClosed(EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }

            /**
             * 请求失败时调用
             * @param eventSource 事件源
             * @param t 异常
             * @param response 响应
             */
            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });

        return future;
    }

}
