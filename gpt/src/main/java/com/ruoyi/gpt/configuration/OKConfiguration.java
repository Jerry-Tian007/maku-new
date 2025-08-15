package com.ruoyi.gpt.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

    @Slf4j
    @NoArgsConstructor
    @AllArgsConstructor
    public class OKConfiguration {


        @Getter
        @Setter
        private String apiHost = "http://127.0.0.1:5000/";

        // Api 服务
        @Setter
        @Getter
        private IOpenAiApi openAiApi;

        @Getter
        @Setter
        private OkHttpClient okHttpClient;

        public EventSource.Factory createRequestFactory() {
            return EventSources.createFactory(okHttpClient);
        }

        // OkHttp 配置信息
//        @Setter
//        @Getter
//        private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.HEADERS;
        @Setter
        @Getter
        private long connectTimeout = 20000;
        @Setter
        @Getter
        private long writeTimeout = 200000;
        @Setter
        @Getter
        private long readTimeout = 20000;

        // http keywords
        public static final String SSE_CONTENT_TYPE = "text/event-stream";
        public static final String APPLICATION_JSON = "application/json";
        public static final String JSON_CONTENT_TYPE = APPLICATION_JSON + "; charset=utf-8";

}
