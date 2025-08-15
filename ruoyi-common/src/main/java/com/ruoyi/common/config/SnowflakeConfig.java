package com.ruoyi.common.config;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 */
@Configuration
public class SnowflakeConfig {

    @Bean
    SnowflakeGenerator snowflakeIdWorker() {
        return new SnowflakeGenerator(1,0);
    }
}
