package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 部门知识库配置对象 dept_kb_config
 * 
 * @author Eric
 * @date 2023-05-31
 */
@Data
public class DeptKbConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** OpenAI文档嵌入部署 */
    @Excel(name = "OpenAI文档嵌入部署")
    private String openaiDocumentEmbeddingDeployment;

    /** OpenAI查询嵌入部署 */
    @Excel(name = "OpenAI查询嵌入部署")
    private String openaiQueryEmbeddingDeployment;

    /** OpenAI摘要生成部署 */
    @Excel(name = "OpenAI摘要生成部署")
    private String openaiSummerizationDeployment;

    /** OpenAI */
    @Excel(name = "OpenAI")
    private String openaiApiKey;

    /** OpenAI */
    @Excel(name = "OpenAI")
    private String openaiApiBase;

    /** OpenAI */
    @Excel(name = "OpenAI")
    private String openaiApiType;

    /** OpenAI */
    @Excel(name = "OpenAI")
    private String openaiApiVersion;

    /** 模型部署名称 */
    @Excel(name = "模型部署名称")
    private String openaiChatgptDeployment;

    /** Azure */
    @Excel(name = "Azure")
    private String azureBlobStorageConnectionString;

    /** Azure */
    @Excel(name = "Azure")
    private String azureBlobStorageAccountName;

    /** Azure */
    @Excel(name = "Azure")
    private String azureBlobStorageContainer;

    /** Azure */
    @Excel(name = "Azure")
    private String azureVectorStorageTableName;

    /** Azure向量Blob存储密钥 */
    @Excel(name = "Azure向量Blob存储密钥")
    private String azureVectorBlobStorageKey;

    /** Azure */
    @Excel(name = "Azure")
    private String azureBlobStorageContainerDirectroy;

    /** Azure */
    @Excel(name = "Azure")
    private String azureBlobStorageAccountUrl;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchUrl;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchKey;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchDataDocsSource;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchService;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchDocsIndex;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchDocsSkillset;

    /** Azure */
    @Excel(name = "Azure")
    private String azureSearchDocsIndexer;

    /** Azure认知服务密钥 */
    @Excel(name = "Azure认知服务密钥")
    private String azureCognitiveServiceKey;

    /** Redis主机 */
    @Excel(name = "Redis主机")
    private String redisHost;

    /** Redis密码 */
    @Excel(name = "Redis密码")
    private String redisPassword;

    /** Redis端口号 */
    @Excel(name = "Redis端口号")
    private String redisPort;

    /** RedisDB */
    @Excel(name = "RedisDB")
    private String redisDb;

    /** 知识库字段内容 */
    @Excel(name = "知识库字段内容")
    private String kbFieldsContent;

    /** 知识库字段来源页 */
    @Excel(name = "知识库字段来源页")
    private String kbFieldsSourcepage;

    /** 知识库字段中文内容 */
    @Excel(name = "知识库字段中文内容")
    private String kbFieldsContentCn;

    /** 知识库字段中文来源页 */
    @Excel(name = "知识库字段中文来源页")
    private String kbFieldsSourcepageCn;

    /** 函数URL */
    @Excel(name = "函数URL")
    private String functionUrl;

    /** excel */
    @Excel(name = "excel")
    private String mergedTextUrl;

    /**  */
    @Excel(name = "")
    private String azureSearchDataSourceExcels;

    /**  */
    @Excel(name = "")
    private String azureSearchIndexExcels;

    /**  */
    @Excel(name = "")
    private String azureSearchSkillsetExcels;

    /**  */
    @Excel(name = "")
    private String azureSearchIndexerExcels;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String azureSearchIndex;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String azureSearchSkillset;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String azureSearchIndexer;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String azureSearchDataSource;

    private String blobStorageContainerDocsDirectroy;

    private String blobStorageContainerExcelsDirectroy;

    private String blobSourceContainerDirectoryExcels;

    private String formrecognizerFunctionUrl;




}
