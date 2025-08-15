package com.ruoyi.common.core.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置，分布式锁
 *
 * @author yecao
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password:#{null}}")
    private String password;

    @Value("${spring.redis.ssl:#{null}}")
    private Boolean ssl;

    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        if (ssl != null && ssl) {
            singleServerConfig.setAddress("rediss://" + redisHost + ":" + port);
            singleServerConfig.setSslEnableEndpointIdentification(true);
        } else {
            singleServerConfig.setAddress("redis://" + redisHost + ":" + port);
        }
        singleServerConfig.setPassword(password);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
