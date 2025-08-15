package com.ruoyi.gpt.core;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiClient {
    @Bean("OpenAIAsyncClient")
    public OpenAIAsyncClient getOpenAIAsyncClient() {
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential("9244fb28ce7e4a9f89eddebd6e9bac49"))
                .endpoint("https://fuhongopenai.openai.azure.com/")
                .buildAsyncClient();
        return client;
    }
}
