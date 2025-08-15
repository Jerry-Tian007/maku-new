package com.ruoyi.gpt.service;


import com.ruoyi.gpt.model.dto.file.DownloadDto;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import java.io.InputStream;


public interface IFileSaveService {
    /**
     * 文件保存
     *
     * @param inputStream 文件流
     * @param fileInfoVo           文件信息
     * @return
     */
    FileInfoVo saveFile(InputStream inputStream, FileInfoVo fileInfoVo);

    String getDownloadUrl(String key);

    /**
     * 删除远程文件
     * @param key
     */
    void deleteRemoteFile(String key);
}
