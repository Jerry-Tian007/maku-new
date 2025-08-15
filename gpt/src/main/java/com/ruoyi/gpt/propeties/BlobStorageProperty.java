package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Blob属性配置类
 *
 * @author tiankk
 */
@Data
@Component
@ConfigurationProperties(prefix = "blob-storage")
public class BlobStorageProperty {
    /**
     * Azure 向量 Blob 储存账户名称
     */
    private String azureBlobStorageAccountName;
    /**
     * Azure向量Blob存储密钥
     */
    private String azureVectorBlobStorageKey;
    /**
     * Azure 向量 Blob 储存容器名称
     */
    private String azureBlobStorageContainer;
    /**
     * Azure Blob存储容器目录
     */
    private String azureBlobStorageContainerDirectory;
    /**
     * blob_source_container_excels_directory
     */
    private String blobStorageContainerExcelsDirectory;
    /**
     * Azure 向量储存表格连接字符串
     */
    private String azureBlobStorageConnectionString;
    /**
     * Azure 向量储存表格名称
     */
    private String azureVectorStorageTableName;
}
