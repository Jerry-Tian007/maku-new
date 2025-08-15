package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.gpt.model.dto.file.DownloadDto;
import com.ruoyi.gpt.service.IFileCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.gpt.model.dto.file.UploadDto;

import java.util.List;

/**
 * 私域知识库
 *
 * @author tiankk
 */
@Slf4j
@RestController
@RequestMapping("/kb/file-common")
public class FileCommonController {

    @Autowired
    private IFileCommonService iFileCommonService;

    /**
     * 上传请求
     */
    @PostMapping("/upload-file")
    public R<String> uploadFile(@RequestParam("file") MultipartFile file, UploadDto uploadDto) {
        return R.success(ReturnMessageEnum.upload_success.toString(), iFileCommonService.uploadFile(file, uploadDto));
    }

    /**
     * 下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/file-download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format(ReturnMessageEnum.file_name_invalid.toString(), fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error(ReturnMessageEnum.download_failed.toString(), e);
        }
    }

    /**
     * 获取下载链接
     */
    @PostMapping("/get-download-url")
    public R<String> getDownloadUrl(@RequestBody DownloadDto downloadDto) {
        return R.success(ReturnMessageEnum.upload_success.toString(), iFileCommonService.getDownloadUrl(downloadDto));
    }

}
