package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.utils.file.CTYunStorageUtil;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.propeties.CTYunStorageProperty;
import com.ruoyi.gpt.propeties.FileStoragePurposeProperty;
import com.ruoyi.gpt.service.IFileSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

/**
 * 天翼云存储
 *
 * @author tiankk
 */
@Service
public class FileSaveCtYunServiceImpl implements IFileSaveService {

    @Autowired
    private CTYunStorageProperty ctYunStorageProperty;
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
        String purpose = fileStoragePurposeProperty.getCtYun().get(fileInfoVo.getPurpose());
        CTYunStorageUtil ctYunStorageUtil = new CTYunStorageUtil(ctYunStorageProperty.getAccessKey(),
                ctYunStorageProperty.getSecretKey(), ctYunStorageProperty.getRegion(), ctYunStorageProperty.getEndPoint());
        String key = purpose + "/" + fileInfoVo.getOriginalFileFullName();
        boolean uploadFile = ctYunStorageUtil.uploadFile(inputStream, key, fileInfoVo.getOriginalFileFullName(), ctYunStorageProperty.getBucketName());
        System.out.println("uploadFile = " + uploadFile);
        if (uploadFile) {
            fileInfoVo.setDownloadUrl(getCtYunFileUrl(key));
            fileInfoVo.setServerFileName(fileInfoVo.getOriginalFileName());
            fileInfoVo.setServerFileNameUrl(key);
        }
        return fileInfoVo;
    }

    @Override
    public String getDownloadUrl(String key) {
        return null;
    }

    @Override
    public void deleteRemoteFile(String key) {

    }

    /**
     * 获取天翼云文件下载链接
     *
     * @return
     */
    public String getCtYunFileUrl(String key) {
        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 24 * 7;
        expiration.setTime(expTimeMillis);
        CTYunStorageUtil ctYunStorageUtil = new CTYunStorageUtil(ctYunStorageProperty.getAccessKey(),
                ctYunStorageProperty.getSecretKey(), ctYunStorageProperty.getRegion(), ctYunStorageProperty.getEndPoint());
        URL url = ctYunStorageUtil.getDownLoadFileUrl(key, expiration, ctYunStorageProperty.getBucketName());
        return url.toString();
    }
}
