package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.LocalFileManageUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.propeties.FileStoragePurposeProperty;
import com.ruoyi.gpt.service.IFileSaveService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 本地存储
 *
 * @author tiankk
 */
@Service
public class FileSaveLocalServiceImpl implements IFileSaveService {

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
        String purpose = fileStoragePurposeProperty.getLocal().get(fileInfoVo.getPurpose());
        FileOutputStream fos = null;
        try {
            byte[] byteData = IOUtils.toByteArray(inputStream);
            // 获取临时文件存储位置
            String fileTemplatePath = System.getProperty("user.dir") + "/" + purpose;
            // 实例化文件临时文件对象
            File fileTemplateSaveDir = new File(fileTemplatePath);
            // 验证临时文件存储位置是否存在
            if (!fileTemplateSaveDir.exists()) {
                // 不存在创建文件夹
                fileTemplateSaveDir.mkdirs();
            }
            File tempFile = new File(fileTemplatePath + File.separator + fileInfoVo.getOriginalFileName() +"." +fileInfoVo.getSuffix());
            System.out.println(tempFile.getCanonicalPath());
            fos = new FileOutputStream(tempFile);
            fos.write(byteData);
//            File file = new File(tempFile.getCanonicalPath());
            // file类型转MultipartFile
//            MultipartFile multipartFile = FileUtils.fileToMultipartFile(file);
            // 上传文件到本地
//            Map map = LocalFileManageUtils.uploadFiles(multipartFile);
//            fileInfoVo.setDownloadUrl(map.get("url").toString());
//            fileInfoVo.setServerFileName(map.get("fileName").toString());
            fileInfoVo.setServerFileNameUrl(tempFile.getCanonicalPath());
            return fileInfoVo;
        } catch (Exception ex) {
            throw new ServiceException("存储本地文件异常").setDetailMessage(ex.getMessage());
        }
    }

    @Override
    public String getDownloadUrl(String key) {
        return null;
    }

    @Override
    public void deleteRemoteFile(String key) {

    }

}
