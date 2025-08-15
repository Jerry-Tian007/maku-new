package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 天翼云对象粗出属性配置类
 *
 * @author tiankk
 */
@Data
@Component
@ConfigurationProperties(prefix = "ctyun-storage")
public class CTYunStorageProperty {
    /**
     * 访问凭证
     */
    private String accessKey;
    /**
     * 凭证secret
     */
    private String secretKey;
    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 地域标识
     */
    private String region;
    /**
     * 桶endpoint
     */
    private String endPoint;
}

