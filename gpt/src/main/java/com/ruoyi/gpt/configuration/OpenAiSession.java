package com.ruoyi.gpt.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.gpt.controller.EventSourceChatListener;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;


public interface OpenAiSession {

    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    void completions(ChatCompletionRequest chatCompletionRequest, EventSourceChatListener eventSourceListener) throws JsonProcessingException, InterruptedException;

    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

}
