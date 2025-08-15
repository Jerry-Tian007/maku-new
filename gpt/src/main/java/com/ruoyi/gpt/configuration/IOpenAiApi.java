package com.ruoyi.gpt.configuration;

import io.reactivex.Single;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IOpenAiApi {

        String v3_completions = "chat";

        @POST(v3_completions)
        Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);


}
