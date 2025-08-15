package com.ruoyi.gpt.configuration;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * OpenAI会话工厂的默认实现类
 * 负责创建DefaultOpenAiSession实例
 * @author ruoyi
 */
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory {

    /**
     * OpenAI配置信息
     */
    private final OKConfiguration configuration;

    /**
     * 构造函数
     * @param configuration OpenAI配置对象
     */
    public DefaultOpenAiSessionFactory(OKConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建一个新的OpenAI会话
     * @return DefaultOpenAiSession实例
     * 
     * 调用流程:
     * 1. 配置OkHttpClient
     * 2. 添加OpenAiHTTPInterceptor拦截器
     * 3. 设置超时时间
     * 4. 创建Retrofit实例
     * 5. 构建IOpenAiApi接口
     * 6. 创建并返回DefaultOpenAiSession实例
     */
    @Override
    public OpenAiSession openSession() {
        // 1. 日志配置 (已注释)
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(configuration.getLevel());

        // 2. 开启 Http 客户端
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .addInterceptor(httpLoggingInterceptor)  // 已注释的日志拦截器
                .addInterceptor(new OpenAiHTTPInterceptor(configuration))  // 添加OpenAI拦截器
                .connectTimeout(configuration.getConnectTimeout(), TimeUnit.SECONDS)  // 连接超时
                .writeTimeout(configuration.getWriteTimeout(), TimeUnit.SECONDS)  // 写入超时
                .readTimeout(configuration.getReadTimeout(), TimeUnit.SECONDS)  // 读取超时
                .build();

        configuration.setOkHttpClient(okHttpClient);

        // 3. 创建 API 服务
        IOpenAiApi openAiApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())  // 设置基础URL
                .client(okHttpClient)  // 设置OkHttpClient
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // 添加RxJava适配器
                .addConverterFactory(JacksonConverterFactory.create())  // 添加Jackson转换器
                .build().create(IOpenAiApi.class);  // 创建API接口实例

        configuration.setOpenAiApi(openAiApi);

        // 4. 创建并返回DefaultOpenAiSession实例
        return new DefaultOpenAiSession(configuration);
    }

}
