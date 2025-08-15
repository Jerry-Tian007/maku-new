package com.ruoyi.gpt.configuration;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;


public class OpenAiHTTPInterceptor implements Interceptor {

    private final OKConfiguration configuration;

    public OpenAiHTTPInterceptor(OKConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        // 1. 获取原始 Request
        Request original = chain.request();

        // 2. 构建请求
        Request request = original.newBuilder()
                .url(original.url())
                .header("Content-Type", OKConfiguration.JSON_CONTENT_TYPE)
                .header("Accept", OKConfiguration.SSE_CONTENT_TYPE)
                .method(original.method(), original.body())
                .build();

        // 3. 返回执行结果
        return chain.proceed(request);
    }

}
