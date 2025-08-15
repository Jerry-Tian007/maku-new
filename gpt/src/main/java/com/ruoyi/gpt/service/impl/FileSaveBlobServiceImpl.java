package com.ruoyi.gpt.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.propeties.BlobStorageProperty;
import com.ruoyi.gpt.propeties.FileStoragePurposeProperty;
import com.ruoyi.gpt.service.IFileSaveService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URLDecoder;
import java.time.OffsetDateTime;

/**
 * Blob存储
 *
 * @author tiankk
 */
@Service
public class FileSaveBlobServiceImpl implements IFileSaveService {

    @Autowired
    private BlobStorageProperty blobStorageProperty;
    @Autowired
    private FileStoragePurposeProperty fileStoragePurposeProperty;

    /**
     * 文件保存
     *
     * @param inputStream 文件流
     * @param fileInfoVo  文件信息
     * @return
     */
    @Override
    public FileInfoVo saveFile(InputStream inputStream, FileInfoVo fileInfoVo) {
        try {
            String purpose = fileStoragePurposeProperty.getBlob().get(fileInfoVo.getPurpose());
            // 获取Blob的SAS Token
            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(
                    OffsetDateTime.now().plusYears(10),
                    new BlobSasPermission().setReadPermission(true));
            // 上传文件至blob
            // 初始化blob客户端
            BlobContainerClient blobContainerClient = getBlobContainerClient();
            String blobFileName = SecurityUtils.getDeptId() + "-" + SecurityUtils.getUserId() + "-" + System.currentTimeMillis();
            String blobFileNameUrl = purpose + "/" + SecurityUtils.getDeptId() + "/" + SecurityUtils.getUserId() + "/" + blobFileName + "." + fileInfoVo.getSuffix();
            BlobClient blobClient = blobContainerClient.getBlobClient(blobFileNameUrl);
            blobClient.upload(inputStream, inputStream.available(), true);
            String url = URLDecoder.decode(blobClient.getBlobUrl(), "UTF-8") + "?" + blobClient.generateSas(values);
            fileInfoVo.setDownloadUrl(url);
            fileInfoVo.setServerFileName(blobFileName);
            fileInfoVo.setServerFileNameUrl(blobFileNameUrl);
            return fileInfoVo;
        } catch (Exception e) {
            throw new ServiceException("文件存储失败").setDetailMessage(e.getMessage());
        }
    }

    @Override
    public String getDownloadUrl(String key) {
        return null;
    }

    @Override
    public void deleteRemoteFile(String key) {

    }

    /**
     * 获取blob客户端
     *
     * @return
     */
    private BlobContainerClient getBlobContainerClient() {
        return new BlobServiceClientBuilder()
                .connectionString(String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;"
                        , blobStorageProperty.getAzureBlobStorageAccountName()
                        , blobStorageProperty.getAzureVectorBlobStorageKey())).
                buildClient().
                getBlobContainerClient(blobStorageProperty.getAzureBlobStorageContainer());
    }
}
