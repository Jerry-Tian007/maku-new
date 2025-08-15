package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.file.DownloadDto;
import com.ruoyi.gpt.model.dto.file.UploadDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileCommonService {
    /**
     * 上传文件
     *
     * @param file
     * @param uploadDto
     * @return
     */
    String uploadFile(MultipartFile file, UploadDto uploadDto);

    String promptUpload(MultipartFile file);

    String chatBiUpload(MultipartFile file);

    String allInOneUpload(MultipartFile file);

    String getDownloadUrl(DownloadDto downloadDto);
}
