package com.ruoyi.gpt.core.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosClientConfig {

    @Value("${tencent.cos.region}")
    private String region;

    @Value("${tencent.cos.secretId}")
    private String secretId;

    @Value("${tencent.cos.secretKey}")
    private String secretKey;
    @Bean
    public COSClient cosClient() {
//        COSCredentials cred = new AnonymousCOSCredentials();
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 2 设置 bucket 的区域
        Region regions = new Region(region);

        // 3 clientConfig 包含了设置 region, https(默认), 超时, 代理等 set 方法
        ClientConfig clientConfig = new ClientConfig(regions);

        // 4 生成 cos 客户端
        return new COSClient(cred, clientConfig);
    }

}
