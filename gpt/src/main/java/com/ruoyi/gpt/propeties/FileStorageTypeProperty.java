package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储类型
 *
 * @author tiankk
 */
@Data
@Component
@ConfigurationProperties(prefix = "file-storage-type")
public class FileStorageTypeProperty {
    /**
     * blob存储
     */
    private Boolean blob;
    /**
     * S3存储
     */
    private Boolean s3;
    /**
     * 本地存储
     */
    private Boolean local;
    /**
     * 天翼云Zos对象存储
     */
    private Boolean ctYun;

    private Boolean cos;
}
