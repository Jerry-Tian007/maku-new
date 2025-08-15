package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.file.FileTransformException;
import com.ruoyi.common.exception.file.FileUploadException;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.AsposeUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.spring.SpringContextUtil;
import com.ruoyi.gpt.model.domain.KbFile;
import com.ruoyi.gpt.model.dto.file.DownloadDto;
import com.ruoyi.gpt.model.dto.file.UploadDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelInsertDto;
import com.ruoyi.gpt.model.vo.file_common.FileInfoVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFileInfoVo;
import com.ruoyi.gpt.propeties.FileStorageClassPathProperty;
import com.ruoyi.gpt.propeties.FileStoragePurposeProperty;
import com.ruoyi.gpt.propeties.FileStorageTypeProperty;
import com.ruoyi.gpt.propeties.S3StorageProperty;
import com.ruoyi.gpt.service.IFileCommonService;
import com.ruoyi.gpt.service.IFileSaveService;
import com.ruoyi.gpt.service.IKbFileManagerRelService;
import com.ruoyi.gpt.service.IKbFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileCommonServiceImpl implements IFileCommonService {

    @Value("${allInOne.fileParsing}")
    private String fileParsingUrl;

    @Autowired
    private S3StorageProperty s3Property;
    @Autowired
    private FileStorageTypeProperty fileStorageTypeProperty;
    @Autowired
    private FileStorageClassPathProperty fileStorageClassPathProperty;
    @Autowired
    private FileStoragePurposeProperty fileStoragePurposeProperty;
    @Autowired
    private IKbFileService iKbFileService;
    @Autowired
    private IKbFileManagerRelService kbFileManagerRelService;
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;
    @Autowired
    private FileSaveLocalServiceImpl fileSaveLocalService;
    @Autowired
    private FileCommonServiceImpl fileCommonService;

    /**
     * 上传文件
     *
     * @param file
     * @param uploadDto
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, UploadDto uploadDto) {
        FileInfoVo fileInfoVo = checkFile(file);
        fileInfoVo.setPurpose("kb");
        // 查询是否有重复文件
        List<KbFile> kbFile = iKbFileService.selectKbFileListByFileNameAndSuffix(fileInfoVo.getOriginalFileName(), fileInfoVo.getSuffix());
        // 检查并删除文件
        checkAndDeleteUserBlob(kbFile, uploadDto.getUpdateStatus());

        try (InputStream inputStream = file.getInputStream()) {
            InputStream convertedInputStream = inputStream;
            MultipartFile convertedFile = file;

            // 文件转换
            String pdfPath = fileToPdf(file);
            if (!StringUtils.isEmpty(pdfPath)) {
                File pdfFile = new File(pdfPath);
                convertedInputStream = Files.newInputStream(pdfFile.toPath());
                convertedFile = FileUtils.fileToMultipartFile(pdfFile);
                fileInfoVo.setSuffix("pdf");
            }

            String classPath = getFileSaveClassPath();
            IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
            FileInfoVo fileInfoVoResult = iFileSaveService.saveFile(convertedInputStream, fileInfoVo);

            Long fileSize = convertedFile.getSize();
            double fileSizeInMB = Math.round(fileSize / (1024.0 * 1024.0) * 100.0) / 100.0;
            // 设置文件页数，pdf按实际的来，其他的按1算
            fileInfoVoResult.setFileSize(fileSizeInMB);
            if (fileInfoVo.getSuffix().equals("pdf")) {
                int pageCount = getFilePageCount(convertedFile, fileInfoVo.getSuffix());
                fileInfoVoResult.setFilePage(pageCount);
            } else {
                fileInfoVoResult.setFilePage(1);
            }
            // 保存文件数据
            fileCommonService.saveKbFile(fileInfoVoResult, uploadDto, kbFile);
            convertedInputStream.close();
            if (pdfPath != null && !fileStorageTypeProperty.getLocal()) {
                File pdfFile = new File(pdfPath);
                pdfFile.delete();
            }
            return ReturnMessageEnum.upload_success.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ReturnMessageEnum.file_upload_failed.toString(), e);
        }
    }

    @Override
    public String promptUpload(MultipartFile file) {
        FileInfoVo fileInfoVo = checkFile(file);
        fileInfoVo.setPurpose("prompt");
        try (InputStream inputStream = file.getInputStream()) {
            String classPath = getFileSaveClassPath();
            IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
            FileInfoVo fileInfoVoResult = iFileSaveService.saveFile(inputStream, fileInfoVo);
            inputStream.close();
            return fileInfoVoResult.getDownloadUrl();
        } catch (Exception e) {
            throw new RuntimeException(ReturnMessageEnum.file_upload_failed.toString(), e);
        }
    }

    @Override
    public String chatBiUpload(MultipartFile file) {
        FileInfoVo fileInfoVo = checkFile(file);
        fileInfoVo.setPurpose("chatBi");
        try (InputStream inputStream = file.getInputStream()) {
            String classPath = getFileSaveClassPath();
            IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
            FileInfoVo fileInfoVoResult = iFileSaveService.saveFile(inputStream, fileInfoVo);
            inputStream.close();
            return fileInfoVoResult.getDownloadUrl();
        } catch (Exception e) {
            throw new RuntimeException(ReturnMessageEnum.file_upload_failed.toString(), e);
        }
    }

    @Override
    public String allInOneUpload(MultipartFile file) {
        FileInfoVo fileInfoVo = checkFile(file);
        fileInfoVo.setPurpose("allInOne");
        try (InputStream inputStream = file.getInputStream()) {
//            String classPath = getFileSaveClassPath();
//            IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
//            FileInfoVo fileInfoVoResult = iFileSaveService.saveFile(inputStream, fileInfoVo);
            FileInfoVo fileInfoVoResult = fileSaveLocalService.saveFile(inputStream, fileInfoVo);
            inputStream.close();
            RestTemplate restTemplate = new RestTemplate();
            JSONObject body = new JSONObject();
            body.set("url", fileInfoVoResult.getServerFileNameUrl());
            body.set("suffix", FilenameUtils.getExtension(file.getOriginalFilename()));
            body.set("filename", FilenameUtils.getBaseName(file.getOriginalFilename()));
            body.set("userId", SecurityUtils.getUserId().toString());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json;charset=UTF-8");
            HttpEntity<JSONObject> entity = new HttpEntity<>(body, headers);

            System.out.println("body:" + body);
            ResponseEntity<JSONObject> res = restTemplate.postForEntity(fileParsingUrl, entity, JSONObject.class);
            System.out.println("res:" + res);
            if (res.getBody().getInt("code") != 200) {
                throw new RuntimeException(ReturnMessageEnum.file_parsing_failed.toString());
            }
            JSONObject data = new JSONObject();
            data.set("fileMark", res.getBody().get("fileMark"));
            data.set("fileTokens", res.getBody().get("fileTokens"));
            return data.toString();
        } catch (Exception e) {
            throw new RuntimeException(ReturnMessageEnum.file_upload_failed.toString(), e);
        }
    }

    @Override
    public String getDownloadUrl(DownloadDto downloadDto) {
        String classPath = getFileSaveClassPath();
        IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
        KbFileInfoVo kbFileInfoVo = iKbFileService.selectKbFileById(downloadDto.getFileId());
        String url = iFileSaveService.getDownloadUrl(kbFileInfoVo.getServerFileNameUrl());
        return url;
    }

    /**
     * 检查文件合法性
     *
     * @param file
     * @return
     */
    private FileInfoVo checkFile(MultipartFile file) {
        if (file.getSize() <= 0) {
            throw new FileUploadException(ReturnMessageEnum.file_upload_cannot_be_empty.toString());
        }
        // 文件原名称
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw new FileUploadException(ReturnMessageEnum.file_name_cannot_be_empty.toString());
        }
        // 原文件后缀
        String suffix = FilenameUtils.getExtension(originalFilename);
        if (StringUtils.isEmpty(suffix)) {
            throw new FileUploadException(ReturnMessageEnum.file_extension_cannot_be_empty.toString());
        }
        suffix = suffix.toLowerCase();
        // 文件名不带后缀
        String fileName = FilenameUtils.getBaseName(originalFilename);
        if (StringUtils.isEmpty(fileName)) {
            throw new FileUploadException(ReturnMessageEnum.file_name_cannot_be_empty.toString());
        }
        FileInfoVo fileInfoVo = new FileInfoVo();
        fileInfoVo.setOriginalFileName(fileName);
        fileInfoVo.setOriginalFileFullName(originalFilename);
        fileInfoVo.setSuffix(suffix);
        return fileInfoVo;
    }

    /**
     * 获取文件存储类路径
     *
     * @return
     */
    private String getFileSaveClassPath() {

        String classPath = "";
        if (fileStorageTypeProperty.getBlob()) {
            classPath = fileStorageClassPathProperty.getBlob();
        } else if (fileStorageTypeProperty.getS3()) {
            classPath = fileStorageClassPathProperty.getS3();
        } else if (fileStorageTypeProperty.getCtYun()) {
            classPath = fileStorageClassPathProperty.getCtYun();
        } else if (fileStorageTypeProperty.getCos()) {
            classPath = fileStorageClassPathProperty.getCos();
        } else if (fileStorageTypeProperty.getLocal()) {
            classPath = fileStorageClassPathProperty.getS3();
        } else {
            throw new ServiceException(ReturnMessageEnum.please_turn_on_the_storage_media.toString());
        }
        return classPath;
    }

    /**
     * 文件转PDF
     *
     * @param file
     * @return
     */
    private String fileToPdf(MultipartFile file) {
        String pdfPath = null;
        // 文件名
        String originalFilename = file.getOriginalFilename();
        // 文件后缀
        String suffix = FilenameUtils.getExtension(originalFilename);
        // 没有后缀
        String fileName = FilenameUtils.getBaseName(originalFilename);
        if ("doc".equals(suffix) || "docx".equals(suffix)) {
            try {
                File tempDir = new File(System.getProperty("user.dir") + "/zip/word2pdf");
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }
                File tempFile = File.createTempFile(fileName, suffix, tempDir);
                file.transferTo(tempFile);
                pdfPath = tempFile.getCanonicalPath().replace(suffix, ".pdf");
                AsposeUtils.wordToPdf(tempFile.getCanonicalPath(), pdfPath);
                tempFile.delete();
            } catch (Exception ex) {
                throw new FileTransformException(ex.getMessage());
            }
        } else if ("pptx".equals(suffix) || "ppt".equals(suffix)) {
            try {
                File tempDir = new File(System.getProperty("user.dir") + "/zip/word2pdf");
                File tempFile = File.createTempFile(fileName, suffix, tempDir);
                file.transferTo(tempFile);
                pdfPath = tempFile.getCanonicalPath().replace(suffix, ".pdf");
                AsposeUtils.pptToPdf(tempFile.getCanonicalPath(), pdfPath);
                tempFile.delete();
            } catch (Exception ex) {
                throw new FileTransformException(ex.getMessage());
            }
        }
        return pdfPath;
    }

    /**
     * 保存用户文件数据
     *
     * @param fileInfoVo 下载地址
     * @param uploadDto  上传其他信息
     */
    @Transactional
    public void saveKbFile(FileInfoVo fileInfoVo, UploadDto uploadDto, List<KbFile> kbFiles) {
        try {
            KbFile insert = new KbFile();
            Long kbFileId = snowflakeGenerator.next();
            insert.setId(kbFileId);
            insert.setDownloadUrl(fileInfoVo.getDownloadUrl());
            insert.setServerFileName(fileInfoVo.getServerFileName());
            insert.setServerFileNameUrl(fileInfoVo.getServerFileNameUrl());
            insert.setSuffix(fileInfoVo.getSuffix());
            insert.setOriginalFileFullName(fileInfoVo.getOriginalFileFullName());
            insert.setOriginalFileName(fileInfoVo.getOriginalFileName());
            insert.setTrainStatus("untrained");
            insert.setFileSize(fileInfoVo.getFileSize());
            insert.setFilePage(fileInfoVo.getFilePage());
            insert.setStatus("enable");

            iKbFileService.insertKbFile(insert);

            String[] strings = uploadDto.getKbIds().split(",");
            Long[] kbIds = new Long[strings.length];
            for (int i = 0; i < strings.length; i++) {
                kbIds[i] = Long.parseLong(strings[i]);
            }
            KbFileManagerRelInsertDto kbFileManagerRelInsertDto = new KbFileManagerRelInsertDto();
            kbFileManagerRelInsertDto.setFileId(kbFileId);
            kbFileManagerRelInsertDto.setKbIds(kbIds);
            kbFileManagerRelService.insertKbFileManagerRel(kbFileManagerRelInsertDto);

//            if (kbFile != null) {
//                if ("success".equals(kbFile.getTrainStatus())) {
//                    // TODO 发送一条消息？
//                }
//            }
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_save_the_database.toString()).setDetailMessage(ex.getMessage());
        }
    }

    /**
     * 检查并删除文件
     *
     * @param kbFiles
     * @param updateStatus
     */
    private void checkAndDeleteUserBlob(List<KbFile> kbFiles, Boolean updateStatus) {
        // 查询是否有重复文件
        if (!CollectionUtils.isEmpty(kbFiles) && !updateStatus) {
            throw new ServiceException(ReturnMessageEnum.duplicate_filename_please_rename_the_file_or_choose_to_overwrite_the_upload.toString());
        }

        // 覆盖删除原文件
        if (!CollectionUtils.isEmpty(kbFiles)  && updateStatus) {
            // 删除数据库数据
            kbFiles.forEach(v -> iKbFileService.deleteKbFileById(v.getId()));
        }
    }

    private int getFilePageCount(MultipartFile file, String suffix) {
        try (InputStream inputStream = file.getInputStream()) {
            if ("pdf".equalsIgnoreCase(suffix)) {
                PDDocument document = PDDocument.load(inputStream);
                int pageCount = document.getNumberOfPages();
                document.close();
                return pageCount;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    @PostConstruct
    public void init() {
        int enableCount = 0;
        if (fileStorageTypeProperty.getBlob()) {
            enableCount++;
        } else if (fileStorageTypeProperty.getS3()) {
            enableCount++;
        } else if (fileStorageTypeProperty.getCtYun()) {
            enableCount++;
        } else if (fileStorageTypeProperty.getLocal()) {
            enableCount++;
        }

        if (enableCount > 1) {

        }
    }

}
