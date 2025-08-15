package com.ruoyi.gpt.service;

import java.util.List;

import com.ruoyi.gpt.model.domain.KbFile;
import com.ruoyi.gpt.model.dto.kb_file.*;
import com.ruoyi.gpt.model.vo.kb_file.KbFileExportVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFileInfoVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFilePageVo;

/**
 * 知识库文件Service接口
 *
 * @author Eric
 * @date 2023-05-16
 */
public interface IKbFileService {
    /**
     * 查询知识库文件详情
     *
     * @param id 知识库文件主键
     * @return 知识库文件
     */
    KbFileInfoVo selectKbFileById(Long id);


//    String selectOriginalFileNameByBlobFileName(String blobFileName);

    /**
     * 查询知识库文件列表
     *
     * @param pageDto 知识库文件
     * @return 知识库文件集合
     */
    List<KbFilePageVo> selectKbFileList(KbFilePageDto pageDto);

    /**
     * 新增知识库文件
     *
     * @param kbFile 知识库文件
     * @return 结果
     */
    Integer insertKbFile(KbFile kbFile);

    /**
     * 修改知识库文件
     *
     * @param updateDto 知识库文件
     * @return 结果
     */
    Integer updateKbFile(KbFileUpdateDto updateDto);

    Integer updateKbFileStatus(KbFile kbFile);

    /**
     * 更新知识库文件的书籍级别
     *
     * @param bookLevelUpdateDto 书籍级别更新DTO
     * @return 是否成功
     */
    Integer updateBookLevel(KbFileBookLevelUpdateDto bookLevelUpdateDto);

    Integer updateBatchKbFile(KbFileUpdateBatchDto updateBatchDto);

    Integer batchEditStatus(BatchEditKbFileStatus batchEditKbFileStatus);

    Integer sendDeleteMessage(Long id);

    /**
     * 删除知识库文件信息
     *
     * @param id 知识库文件主键
     * @return 结果
     */
    Integer deleteKbFileById(Long id);


    /**
     * 根据文件名称和后缀查询文件信息
     *
     * @param fileName
     * @param suffix
     * @return
     */
    KbFile selectKbFileByFileNameAndSuffix(String fileName, String suffix);

    List<KbFile> selectKbFileListByFileNameAndSuffix(String fileName, String suffix);

    /**
     * 根据文件id取消训练
     *
     * @param id
     */
    Integer cancelTrainingById(Long id);

    /**
     * 文件训练
     *
     * @param fileIds
     */
    Integer train(List<Long> fileIds);

    Integer updateTrain(KbFileUpdateBatchDto updateBatchDto);

    /**
     * 批量删除知识库文件信息
     *
     * @param fileIds
     * @return
     */
    Integer deleteKbFileByIds(List<Long> fileIds);

    /**
     * 修改文件的训练状态
     *
     * @param kbFileUpdateTrainStatusDto
     * @return
     */
    Integer updateKbFileTrainStatus(KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto);

    List<KbFileExportVo> selectExportKbFileList(KbFilePageDto pageDto);
}
