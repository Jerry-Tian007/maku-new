package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储类路径
 *
 * @author tiankk
 */
@Data
@Component
@ConfigurationProperties(prefix = "file-storage-class-path")
public class FileStorageClassPathProperty {
    /**
     * blob存储类路径
     */
    private String blob;
    /**
     * S3存储类路径
     */
    private String s3;
    /**
     * 本地存储类路径
     */
    private String local;
    /**
     * 天翼云Zos对象存储类路径
     */
    private String ctYun;
    /**
     * 腾讯云存储
     */
    private String cos;
}
