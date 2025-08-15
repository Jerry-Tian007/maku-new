package com.ruoyi.common.utils;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地文件管理工具 文件上传到本地、从本地删除
 */
@Component
public class LocalFileManageUtils {
    private static final String FILE_DELIMETER = ",";

    @Value("${ruoyi.localhostUrl}")
    private String LOCALHOST_URL;

    private static String FRONT_IP;


    public static Map uploadFiles(MultipartFile file) {
        Map result = new HashMap<>();
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = FRONT_IP + fileName;

            result.put("url", url);
            result.put("fileName", fileName);
            result.put("newFileName", FileUtils.getName(fileName));
            result.put("originalFilename", file.getOriginalFilename());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    public static String deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "error";
        }

        String fullFilePath = RuoYiConfig.getUploadPath() + filePath;
        if (FileUtils.deleteFile(fullFilePath)) {
            return "success";
        }

        return "error";
    }

    private static String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    private static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    @PostConstruct
    public void init() {
        FRONT_IP = LOCALHOST_URL;
    }

}
