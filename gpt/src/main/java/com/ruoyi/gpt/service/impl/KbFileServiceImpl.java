package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringContextUtil;
import com.ruoyi.gpt.configuration.RabbitmqProducer;
import com.ruoyi.gpt.model.dto.kb_file.*;
import com.ruoyi.gpt.model.vo.kb_file.KbFileExportVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFileTrainVo;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbIdVo;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbNameVo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbFileMapper;
import com.ruoyi.gpt.model.domain.KbFile;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateBatchDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateDto;
import com.ruoyi.gpt.model.vo.kb_file.KbFileInfoVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFilePageVo;
import com.ruoyi.gpt.propeties.FileStorageClassPathProperty;
import com.ruoyi.gpt.propeties.FileStorageTypeProperty;
import com.ruoyi.gpt.service.IFileSaveService;
import com.ruoyi.gpt.service.IKbFileManagerRelService;
import com.ruoyi.gpt.service.IKbManagerService;
import com.ruoyi.gpt.service.IKbFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库文件Service业务层处理
 *
 * @author Eric
 * @date 2023-05-16
 */
@Service
public class KbFileServiceImpl implements IKbFileService {
    private static final Logger log = LoggerFactory.getLogger(KbFileServiceImpl.class);

    @Resource
    private KbFileMapper kbFileMapper;

    @Resource
    private DeptKbConfigServiceImpl deptKbConfigService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IKbFileManagerRelService kbFileManagerRelService;

    @Autowired
    private RabbitmqProducer rabbitmqProducer;

    @Autowired
    private FileStorageTypeProperty fileStorageTypeProperty;
    @Autowired
    private FileStorageClassPathProperty fileStorageClassPathProperty;

    @Autowired
    private CosImageServiceImpl cosImageService;

    /**
     * 查询知识库文件详情
     *
     * @param id 知识库文件主键
     * @return 知识库文件
     */
    @Override
    public KbFileInfoVo selectKbFileById(Long id) {

        // 根据id查询知识库文件实体
        KbFile kbFile = kbFileMapper.selectKbFileById(id);

        // 如果查询结果为空，不需要拼接kb_name
        if (kbFile == null) {
            return null;
        }

        // 根据file_id查kb_name 1 -> a, 1 -> b, 1 -> c
        List<Long> fileIds = Arrays.asList(id);
        List<FileIdToKbNameVo> fileIdToKbNameVoList = kbFileManagerRelService.selectFileIdToKbNameListByFileIds(fileIds);

        // 拼接kb_name 1 -> a,b,c
        Map<Long, String> fileIdToKbNamesMap = fileIdToKbNameVoList.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbNameVo::getFileId,
                        Collectors.mapping(FileIdToKbNameVo::getKbName, Collectors.joining(","))));
        // 拼接kb_name 1 -> a,b,c
        Map<Long, String> fileIdToKbIdsMap = fileIdToKbNameVoList.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbNameVo::getFileId,
                        Collectors.mapping(FileIdToKbNameVo::getKbId, Collectors.joining(","))));

        // 封装kbFileInfoVo
        KbFileInfoVo kbFileInfoVo = BeanConvertUtil.conver(kbFile, KbFileInfoVo.class);
        kbFileInfoVo.setKbName(fileIdToKbNamesMap.get(id));
        kbFileInfoVo.setKbId(fileIdToKbIdsMap.get(id));
        Map<String, List<String>> result = cosImageService.listAndSignImageUrls(kbFileInfoVo.getDownloadUrlMd());
        kbFileInfoVo.setDownloadUrlImages(result.get("IMAGE"));
        kbFileInfoVo.setDownloadUrlYamls(result.get("YAML"));

        return kbFileInfoVo;
    }

//    @Override
//    public String selectOriginalFileNameByBlobFileName(String blobFileName) {
//        return userBlobMapper.selectOriginalFileNameByBlobFileName(blobFileName);
//    }

    /**
     * 查询知识库文件列表
     *
     * @param pageDto 知识库文件
     * @return 知识库文件
     */
    @Override
    public List<KbFilePageVo> selectKbFileList(KbFilePageDto pageDto) {

        // 设置查询条件size
        Integer size = 0;
        if (!CollectionUtils.isEmpty(pageDto.getKbIds())) {
            size = pageDto.getKbIds().size();
        }
        String userName = "";
        Long[] roleIds = null;

        List<Long> userRoleIdList = Arrays.stream(SecurityUtils.getLoginUser().getUser().getRoleIds()).collect(Collectors.toList());
        // 如果不是admin，不是管理员 获取当前登录用户的用户名和角色id
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId()) && !userRoleIdList.contains(1003L)) {
            userName = SecurityUtils.getUsername();
            roleIds = SecurityUtils.getLoginUser().getUser().getRoleIds();
        }
        // 查询
        List<KbFilePageVo> kbFilePageVoList = kbFileMapper.selectKbFileList(pageDto, size, userName, roleIds);

        // 如果查询结果为空，不需要拼接kb_name
        if (kbFilePageVoList.isEmpty()) {
            return new ArrayList<>();
        }

        // 根据查询结果中的file_id，查询对应的kb_name -- 1 -> a, 1 -> b, 1 -> c
        List<Long> fileIds = kbFilePageVoList.stream().map(KbFilePageVo::getId).collect(Collectors.toList());
        List<FileIdToKbNameVo> fileIdToKbNameVoList = kbFileManagerRelService.selectFileIdToKbNameListByFileIds(fileIds);

        // 拼接kb_name -- 1 -> a,b,c
        Map<Long, String> fileIdToKbNamesMap = fileIdToKbNameVoList.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbNameVo::getFileId,
                        Collectors.mapping(FileIdToKbNameVo::getKbName, Collectors.joining(","))));

        for (int i = 0; i < kbFilePageVoList.size(); i++) {
            KbFilePageVo kbFilePageVo = kbFilePageVoList.get(i);
            kbFilePageVo.setKbName(fileIdToKbNamesMap.get(kbFilePageVo.getId()));
        }

        return kbFilePageVoList;
    }

    /**
     * 新增知识库文件
     *
     * @param kbFile 知识库文件
     * @return 结果
     */
    @Override
    public Integer insertKbFile(KbFile kbFile) {
        kbFile.setUserId(SecurityUtils.getUserId());
        if (kbFile.getDeptId() == null) {
            kbFile.setDeptId(SecurityUtils.getDeptId());
        }
        kbFile.setCreateBy(SecurityUtils.getUsername());
        kbFile.setCreateTime(DateUtils.getNowDate());
        return kbFileMapper.insertKbFile(kbFile);
    }

    /**
     * 修改知识库文件
     *
     * @param updateDto 知识库文件
     * @return 结果
     */
    @Override
    @Transactional
    public Integer updateKbFile(KbFileUpdateDto updateDto) {
        try {
            KbFileManagerRelUpdateDto kbFileManagerRelUpdateDto = new KbFileManagerRelUpdateDto();
            kbFileManagerRelUpdateDto.setFileId(updateDto.getFileId());
            kbFileManagerRelUpdateDto.setKbIds(updateDto.getKbIds());
            kbFileManagerRelService.updateKbFileManagerRel(kbFileManagerRelUpdateDto);
//            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
//            kbFileUpdateTrainStatusDto.setId(new Long[]{updateDto.getFileId()});
//            kbFileUpdateTrainStatusDto.setTrainTime(new Date());
//            kbFileMapper.updateKbFileTrainStatusToUntrained(kbFileUpdateTrainStatusDto);
            updateFileSendMessage(updateDto);
            return 1;
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_modify_the_file.toString()).setDetailMessage(ex.getMessage());
        }
    }

    @Override
    public Integer updateKbFileStatus(KbFile kbFile) {
        return kbFileMapper.updateKbFileStatus(kbFile);
    }

    @Override
    public Integer updateBookLevel(KbFileBookLevelUpdateDto bookLevelUpdateDto) {
        return kbFileMapper.updateBookLevel(bookLevelUpdateDto);
    }

    private void updateFileSendMessage(KbFileUpdateDto updateDto) {
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(updateDto.getFileId());
        List<KbFileTrainVo> kbFileTrainVos = kbFileMapper.selectKbFileTrainVoByFileIds(fileIds);
        for (KbFileTrainVo kbFileTrainVo : kbFileTrainVos) {
            try {
                // 跳过未训练或错误状态的文件
                if ("untrained".equals(kbFileTrainVo.getTrainStatus()) ||
                    "error".equals(kbFileTrainVo.getTrainStatus())) {
                    continue;
                }
                // 封装消息
                JSONObject message = new JSONObject();
                message.set("message_type", "update");
                message.set("file_id", kbFileTrainVo.getFileId().toString());
                message.set("file_name", kbFileTrainVo.getFileName());
                message.set("file_suffix", kbFileTrainVo.getSuffix());
                message.set("blob_url", kbFileTrainVo.getBlobUrl());
                message.set("kb_id", updateDto.getKbIds());
                message.set("place_holders", "pass");

                log.info("准备更新文件训练状态: {}", message.toString());

                // 设置redis缓存
                redisCache.setCacheObject(kbFileTrainVo.getFileId().toString(), 1);

                // 发送消息并保存记录
                rabbitmqProducer.saveAndSendMessage("messageExchange", "train", message.toString());

                log.info("文件 {} 训练消息发送成功", kbFileTrainVo.getFileName());
            } catch (Exception e) {
                log.error("处理文件 {} 训练消息时发生错误: {}",
                    kbFileTrainVo.getFileName(), e.getMessage(), e);
            }
        }
    }

    /**
     * 批量修改知识库文件所属知识库
     *
     * @param updateBatchDto
     * @return
     */
    @Override
    public Integer updateBatchKbFile(KbFileUpdateBatchDto updateBatchDto) {
        try {
            KbFileManagerRelUpdateBatchDto kbFileManagerRelUpdateBatchDto = BeanConvertUtil.conver(updateBatchDto, KbFileManagerRelUpdateBatchDto.class);
            return kbFileManagerRelService.updateBatchKbFileManagerRel(kbFileManagerRelUpdateBatchDto);
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.batch_edit_files_failed.toString()).setDetailMessage(ex.getMessage());
        }
    }

    @Override
    public Integer batchEditStatus(BatchEditKbFileStatus batchEditKbFileStatus) {
        batchEditKbFileStatus.getFileIds().forEach(fileId -> {
            JSONObject entries = new JSONObject();
            entries.set("file_id", fileId + "");

            String status = batchEditKbFileStatus.getStatus();
            if ("unable".equals(status)) {
                entries.set("message_type", "logical_delete");
                rabbitmqProducer.saveAndSendMessage("messageExchange", "delete", entries.toString());
                return;
            }
            if ("enable".equals(status)) {
                entries.set("message_type", "active");
                redisCache.setCacheObject(fileId.toString(), 1);
                rabbitmqProducer.saveAndSendMessage("messageExchange", "train", entries.toString());
            }
        });
        return 1;
    }

    /**
     * 删除知识库文件信息
     *
     * @param id 知识库文件主键
     * @return 结果
     */
    @Override
    public Integer sendDeleteMessage(Long id) {
        try {
            KbFile checkFile = kbFileMapper.selectKbFileById(id);
            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
            kbFileUpdateTrainStatusDto.setId(new Long[]{id});
            kbFileMapper.updateKbFileTrainStatusToDeleting(kbFileUpdateTrainStatusDto);
            JSONObject message = new JSONObject();
            message.set("file_id", id.toString());
            message.set("message_type", "delete");
            rabbitmqProducer.saveAndSendMessage("messageExchange", "delete", message.toString());
            if (!"success".equals(checkFile.getTrainStatus())) {
                String key = id.toString();
                if (redisCache.hasKey(key)) {
                    redisCache.setCacheObject(key, 2);
                }
            }

            //同时删除远程文件
            String classPath = getFileSaveClassPath();
            IFileSaveService iFileSaveService = SpringContextUtil.getBean(classPath, IFileSaveService.class);
            iFileSaveService.deleteRemoteFile(checkFile.getServerFileNameUrl());

            return 1;
        } catch (Exception ex) {
            throw new ServiceException(ReturnMessageEnum.failed_to_delete_file.toString()).setDetailMessage(ex.getMessage());
        }
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

    @Override
    @Transactional
    public Integer deleteKbFileById(Long id) {
        Integer row = kbFileMapper.deleteKbFileById(id);
        kbFileManagerRelService.deleteKbFileManagerRelByFileId(id);
        return row;
    }

    /**
     * 批量删除知识库文件信息
     *
     * @param fileIds
     * @return
     */
    @Override
    public Integer deleteKbFileByIds(List<Long> fileIds) {
        List<KbFile> checkKbFiles = kbFileMapper.selectKbFileByIds(fileIds);
        for (int i = 0; i < checkKbFiles.size(); i++) {
            KbFile checkkbFile = checkKbFiles.get(i);
            sendDeleteMessage(checkkbFile.getId());
        }
        return checkKbFiles.size();
    }

    /**
     * 修改文件的训练状态
     *
     * @param kbFileUpdateTrainStatusDto
     * @return
     */
    @Override
    public Integer updateKbFileTrainStatus(KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto) {
        return kbFileMapper.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
    }

    @Override
    public List<KbFileExportVo> selectExportKbFileList(KbFilePageDto pageDto) {
        // 设置查询条件size
        Integer size = 0;
        if (!CollectionUtils.isEmpty(pageDto.getKbIds())) {
            size = pageDto.getKbIds().size();
        }
        String userName = "";
        Long[] roleIds = null;
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            userName = SecurityUtils.getUsername();
            roleIds = SecurityUtils.getLoginUser().getUser().getRoleIds();
        }
        // 查询
        List<KbFileExportVo> kbFileExportVos = kbFileMapper.selectExportKbFileList(pageDto, size, userName, roleIds);

        // 如果查询结果为空，不需要拼接kb_name
        if (kbFileExportVos.isEmpty()) {
            return new ArrayList<>();
        }

        // 根据查询结果中的file_id，查询对应的kb_name -- 1 -> a, 1 -> b, 1 -> c
        List<Long> fileIds = kbFileExportVos.stream().map(KbFileExportVo::getId).collect(Collectors.toList());
        List<FileIdToKbNameVo> fileIdToKbNameVoList = kbFileManagerRelService.selectFileIdToKbNameListByFileIds(fileIds);

        // 拼接kb_name -- 1 -> a,b,c
        Map<Long, String> fileIdToKbNamesMap = fileIdToKbNameVoList.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbNameVo::getFileId,
                        Collectors.mapping(FileIdToKbNameVo::getKbName, Collectors.joining(","))));

        for (int i = 0; i < kbFileExportVos.size(); i++) {
            KbFileExportVo kbFileExportVo = kbFileExportVos.get(i);
            kbFileExportVo.setKbName(fileIdToKbNamesMap.get(kbFileExportVo.getId()));
        }

        return kbFileExportVos;
    }


    /**
     * 根据文件名称和后缀查询文件信息
     *
     * @param fileName
     * @param suffix
     * @return
     */
    @Override
    public KbFile selectKbFileByFileNameAndSuffix(String fileName, String suffix) {
        return kbFileMapper.selectKbFileByFileNameAndSuffix(fileName, suffix);
    }

    @Override
    public List<KbFile> selectKbFileListByFileNameAndSuffix(String fileName, String suffix) {
        return kbFileMapper.selectKbFileListByFileNameAndSuffix(fileName, suffix);
    }

    /**
     * 根据文件id取消训练
     *
     * @param id
     */
    @Override
    public Integer cancelTrainingById(Long id) {
        String key = id.toString();
        if (redisCache.hasKey(key)) {
            redisCache.setCacheObject(key, 0);
            JSONObject message = new JSONObject();
            message.set("file_id", id.toString());
            message.set("message_type", "cancel");
            rabbitmqProducer.saveAndSendMessage("messageExchange", "delete", message.toString());
            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
            kbFileUpdateTrainStatusDto.setId(new Long[]{id});
            kbFileUpdateTrainStatusDto.setTrainTime(DateUtils.getNowDate());
            kbFileUpdateTrainStatusDto.setTrainStatus("canceling");
            kbFileMapper.updateKbFileTrainStatus(kbFileUpdateTrainStatusDto);
        } else {
            throw new ServiceException(ReturnMessageEnum.the_document_is_being_trained_cancellation_failed.toString());
        }
        return 1;
    }

    /**
     * 文件训练
     *
     * @param fileIds
     */
    @Override
    @Transactional
    public Integer train(List<Long> fileIds) {
        List<Long> resultFileIds = new ArrayList<>();
        HashMap<Long, String> fileIdTrainStatus = new HashMap<>();
        for (int i = 0; i < fileIds.size(); i++) {
            KbFile kbFile = kbFileMapper.selectKbFileById(fileIds.get(i));
            if (StringUtils.isNull(kbFile)) {
                continue;
            }
            fileIdTrainStatus.put(kbFile.getId(), kbFile.getTrainStatus());
        }

        for (int i = 0; i < fileIds.size(); i++) {
            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
            kbFileUpdateTrainStatusDto.setTrainTime(DateUtils.getNowDate());
            kbFileUpdateTrainStatusDto.setId(new Long[]{fileIds.get(i)});
            Integer status = kbFileMapper.updateKbFileTrainStatusToWaiting(kbFileUpdateTrainStatusDto);
            if (status == 1) {
                resultFileIds.add(fileIds.get(i));
            }
        }
        if (CollectionUtils.isEmpty(resultFileIds)) {
            return 0;
        }
        // 查询除kbId列表以外的，消息需要的数据
        List<KbFileTrainVo> kbFileTrainVos = kbFileMapper.selectKbFileTrainVoByFileIds(resultFileIds);
        // 映射 fileId -> kbId列表
        List<FileIdToKbIdVo> fileIdToKbId = kbFileManagerRelService.selectFileIdToKbIdByFileIds(resultFileIds);
        Map<Long, List<Long>> fileIdToKbIdList = fileIdToKbId.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbIdVo::getFileId,
                        Collectors.mapping(FileIdToKbIdVo::getKbId, Collectors.toList())
                ));

        // 循环发送
        kbFileTrainVos.forEach(kbFileTrainVo -> {
            // 封装消息
            JSONObject message = new JSONObject();
            message.set("message_type", "train");
            message.set("original_status", fileIdTrainStatus.get(kbFileTrainVo.getFileId()));
            message.set("file_id", kbFileTrainVo.getFileId().toString());
            message.set("file_name", kbFileTrainVo.getFileName());
            message.set("file_suffix", kbFileTrainVo.getSuffix());
            message.set("blob_url", kbFileTrainVo.getBlobUrl());
            message.set("kb_id", fileIdToKbIdList.get(kbFileTrainVo.getFileId()));
            message.set("place_holders", "pass");
            System.out.println("Train: " + message.toString());
            // 设置redis
            redisCache.setCacheObject(kbFileTrainVo.getFileId().toString(), 1);
            // 发送消息并保存记录
            rabbitmqProducer.saveAndSendMessage("messageExchange", "train", message.toString());
        });
        return resultFileIds.size();
    }

    @Override
    public Integer updateTrain(KbFileUpdateBatchDto updateBatchDto) {
        updateBatchKbFile(updateBatchDto);

        List<Long> fileIds = Arrays.stream(updateBatchDto.getFileIds()).collect(Collectors.toList());
        List<Long> resultFileIds = new ArrayList<>();
        HashMap<Long, String> fileIdTrainStatus = new HashMap<>();
        for (int i = 0; i < fileIds.size(); i++) {
            KbFile kbFile = kbFileMapper.selectKbFileById(fileIds.get(i));
            if (StringUtils.isNull(kbFile)) {
                continue;
            }
            fileIdTrainStatus.put(kbFile.getId(), kbFile.getTrainStatus());
        }

        for (int i = 0; i < fileIds.size(); i++) {
            KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto = new KbFileUpdateTrainStatusDto();
            kbFileUpdateTrainStatusDto.setTrainTime(DateUtils.getNowDate());
            kbFileUpdateTrainStatusDto.setId(new Long[]{fileIds.get(i)});
            Integer status = kbFileMapper.updateKbFileTrainStatusToWaiting(kbFileUpdateTrainStatusDto);
            if (status == 1) {
                resultFileIds.add(fileIds.get(i));
            }
        }
        if (CollectionUtils.isEmpty(resultFileIds)) {
            return 0;
        }
        // 查询除kbId列表以外的，消息需要的数据
        List<KbFileTrainVo> kbFileTrainVos = kbFileMapper.selectKbFileTrainVoByFileIds(resultFileIds);
        // 映射 fileId -> kbId列表
        List<FileIdToKbIdVo> fileIdToKbId = kbFileManagerRelService.selectFileIdToKbIdByFileIds(resultFileIds);
        Map<Long, List<Long>> fileIdToKbIdList = fileIdToKbId.stream()
                .collect(Collectors.groupingBy(
                        FileIdToKbIdVo::getFileId,
                        Collectors.mapping(FileIdToKbIdVo::getKbId, Collectors.toList())
                ));

        // 循环发送
        kbFileTrainVos.forEach(kbFileTrainVo -> {
            // 封装消息
            JSONObject message = new JSONObject();
            message.set("message_type", "update");
            message.set("original_status", fileIdTrainStatus.get(kbFileTrainVo.getFileId()));
            message.set("file_id", kbFileTrainVo.getFileId().toString());
            message.set("file_name", kbFileTrainVo.getFileName());
            message.set("file_suffix", kbFileTrainVo.getSuffix());
            message.set("blob_url", kbFileTrainVo.getBlobUrl());
            message.set("kb_id", fileIdToKbIdList.get(kbFileTrainVo.getFileId()));
            message.set("place_holders", "pass");
            System.out.println("Train: " + message.toString());
            // 设置redis
            redisCache.setCacheObject(kbFileTrainVo.getFileId().toString(), 1);
            // 发送消息并保存记录
            rabbitmqProducer.saveAndSendMessage("messageExchange", "train", message.toString());
        });
        return resultFileIds.size();
    }
}
