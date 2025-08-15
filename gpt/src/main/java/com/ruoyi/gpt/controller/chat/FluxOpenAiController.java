package com.ruoyi.gpt.controller.chat;

import cn.hutool.core.lang.UUID;

import cn.hutool.json.JSONObject;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.*;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;


@RestController
public class FluxOpenAiController {

    @Resource
    private RedisCache redisCache;
    @Resource
    private OpenAIAsyncClient openAIAsyncClient;

    @GetMapping("/gpt3.5")
    public Flux gpt35(String uuid){
        JSONObject params = redisCache.getCacheObject(uuid);
        System.out.println(params.toString());
        if (params == null ){
            return null;
        }
//        redisCache.deleteObject(uuid);
//        OpenAIAsyncClient  client = new OpenAIClientBuilder()
//                .credential(new AzureKeyCredential("9244fb28ce7e4a9f89eddebd6e9bac49"))
//                .endpoint("https://fuhongopenai.openai.azure.com/")
//                .buildAsyncClient();
//        List<ChatMessage> chatMessages = new ArrayList<>();
//
//        JSONArray question = params.getJSONArray("question");
//
//        question.stream().forEach(item -> {
//            JSONObject inner = new JSONObject(item);
//            if ("system".equals(inner.getStr("role"))) {
//                chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent(inner.getStr("content")));
//            }
//            if ("user".equals(inner.getStr("role"))) {
//                chatMessages.add(new ChatMessage(ChatRole.USER).setContent(inner.getStr("content")));
//            }
//            if ("assistant".equals(inner.getStr("role"))) {
//                chatMessages.add(new ChatMessage(ChatRole.ASSISTANT).setContent(inner.getStr("content")));
//            }
//        });
//        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
////        chatCompletionsOptions.setFrequencyPenalty(params.getDouble("frequency_penalty"));
////        chatCompletionsOptions.setTemperature(params.getDouble("temperature"));
////        chatCompletionsOptions.setMaxTokens(params.getInt("max_tokens"));
////        chatCompletionsOptions.setPresencePenalty(params.getDouble("presence_penalty"));
////        chatCompletionsOptions.setTopP(params.getDouble("top_p"));
////        chatCompletionsOptions.setStream(true);
//        Flux<ChatCompletions> chatCompletionsStream = client.getChatCompletionsStream("gpt-35-turbo",
//                chatCompletionsOptions);
//        return chatCompletionsStream;


        return openAIAsyncClient.getChatCompletionsStream("gpt-35-turbo", new ChatCompletionsOptions(
                Flux.fromIterable(new JSONObject(redisCache.getCacheObject(uuid).toString())
                                .getJSONArray("question")
                                .toList(JSONObject.class))
                        .map(obj -> {
                            JSONObject inner = obj;
                            return new ChatMessage(ChatRole.fromString(inner.getStr("role")))
                                    .setContent(inner.getStr("content"));
                        }).collectList().block()));
    }


    @PostMapping("/uploadParams")
    public String gpt35(@RequestBody JSONObject params){
        String uuid = UUID.fastUUID().toString();
        redisCache.setCacheObject(uuid,params);
        return uuid;
    }
}
