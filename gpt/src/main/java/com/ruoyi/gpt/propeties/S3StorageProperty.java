package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * S3属性配置类
 *
 * @author tiankk
 */
@Data
@Component
@ConfigurationProperties(prefix = "s3-storage")
public class S3StorageProperty {
    /**
     * Url
     */
    private String url;
}
