package com.ruoyi.gpt.service.impl;

import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.propeties.FileStoragePurposeProperty;
import com.ruoyi.gpt.service.IFileSaveService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * S3存储
 *
 * @author tiankk
 */
@Service
public class FileSaveS3ServiceImpl implements IFileSaveService {
    @Autowired
    private FileStoragePurposeProperty fileStoragePurposeProperty;
    /**
     * 文件保存
     *
     * @param inputStream 文件流
     * @param fileInfoVo           文件信息
     * @return
     */
    @Override
    public FileInfoVo saveFile(InputStream inputStream, FileInfoVo fileInfoVo) {
        String purpose = fileStoragePurposeProperty.getS3().get(fileInfoVo.getPurpose());
        return fileInfoVo;
    }

    @Override
    public String getDownloadUrl(String key) {
        return null;
    }

    @Override
    public void deleteRemoteFile(String key) {

    }

}
