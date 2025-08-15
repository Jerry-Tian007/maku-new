package com.ruoyi.gpt.service.impl;


import com.alibaba.fastjson2.util.UUIDUtils;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.gpt.core.cos.CosClientConfig;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.service.IFileSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static com.qcloud.cos.demo.BucketRefererDemo.cosClient;

@Service
public class FileSaveTxCosServiceImpl  implements IFileSaveService {

    @Value("${tencent.cos.bucketName}")
    private String bucketName;

    @Value("${tencent.cos.baseUrl}")
    private String baseUrl;

    @Autowired
    private CosClientConfig cosClientConfig;


    @Override
    public FileInfoVo saveFile(InputStream inputStream, FileInfoVo fileInfoVo) {
        try {
            // 获取输入流
            String key = fileInfoVo.getPurpose() + "/" + SecurityUtils.getUserId()+ "/" + UUID.randomUUID() + "." + fileInfoVo.getSuffix();
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(getContentType(fileInfoVo.getSuffix()));

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            PutObjectResult putObjectResult = cosClientConfig.cosClient().putObject(putObjectRequest);
            System.out.println(putObjectResult);
            if (putObjectResult!=null) {
                fileInfoVo.setServerFileNameUrl(key);
                fileInfoVo.setServerFileName(fileInfoVo.getOriginalFileName()+ "." + fileInfoVo.getSuffix());
                fileInfoVo.setDownloadUrl(getDownloadUrl(key));
            }
            // 返回文件访问URL
            return fileInfoVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败");
        }
    }


    /**
     * 下载文件
     * @param key 文件路径(包含文件名)
     * @return 文件流
     */
    public InputStream downloadFile(String key) {
        try {
            // 获取文件
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            return cosObject.getObjectContent();
        } catch (Exception e) {
            throw new RuntimeException("下载文件失败");
        }
    }

    /**
     * 删除文件
     * @param key 文件路径(包含文件名)
     */
    public void deleteFile(String key) {
        try {
            cosClient.deleteObject(bucketName, key);
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败");
        }
    }

    /**
     * 获取文件类型
     * @param fileExt 文件扩展名
     * @return 文件类型
     */
    private String getContentType(String fileExt) {
        if (fileExt.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (fileExt.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (fileExt.equalsIgnoreCase(".jpeg") ||
                fileExt.equalsIgnoreCase(".jpg") ||
                fileExt.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (fileExt.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (fileExt.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (fileExt.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (fileExt.equalsIgnoreCase(".pptx") ||
                fileExt.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (fileExt.equalsIgnoreCase(".docx") ||
                fileExt.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (fileExt.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "application/octet-stream";
    }
    @Override
    public String getDownloadUrl(String key) {
        try {
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
            Date expirationDate = new Date(System.currentTimeMillis() + 36500L * 24 * 60 * 60 * 1000); // 链接有效期为1小时
            req.setExpiration(expirationDate);
            URL presignedUrl = cosClientConfig.cosClient().generatePresignedUrl(req);
            return presignedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("生成预签名URL失败", e);
        }
    }

    @Override
    public void deleteRemoteFile(String key) {
        try {
            // 检查文件是否存在（防止误删不存在的文件）
            if (!cosClientConfig.cosClient().doesObjectExist(bucketName, key)) {
                throw new RuntimeException("远程文件不存在: " + key);
            }
            // 执行删除操作
            cosClientConfig.cosClient().deleteObject(bucketName, key);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("远程文件删除失败", e);
        }
    }

}
