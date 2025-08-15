package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.KbTrainFile;

/**
 * 知识库训练文件Service接口
 * 
 * @author Eric
 * @date 2023-09-19
 */
public interface IKbTrainFileService 
{
    /**
     * 查询知识库训练文件
     * 
     * @param fileId 知识库训练文件主键
     * @return 知识库训练文件
     */
    public KbTrainFile selectKbTrainFileByFileId(String fileId);

    /**
     * 查询知识库训练文件列表
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 知识库训练文件集合
     */
    public List<KbTrainFile> selectKbTrainFileList(KbTrainFile kbTrainFile);

    /**
     * 新增知识库训练文件
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 结果
     */
    public int insertKbTrainFile(KbTrainFile kbTrainFile);

    /**
     * 修改知识库训练文件
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 结果
     */
    public int updateKbTrainFile(KbTrainFile kbTrainFile);

    /**
     * 批量删除知识库训练文件
     * 
     * @param fileIds 需要删除的知识库训练文件主键集合
     * @return 结果
     */
    public int deleteKbTrainFileByFileIds(String[] fileIds);

    /**
     * 删除知识库训练文件信息
     * 
     * @param fileId 知识库训练文件主键
     * @return 结果
     */
    public int deleteKbTrainFileByFileId(String fileId);

    List<KbTrainFile> selectUnTrainKbTrainFileList(KbTrainFile kbTrainFile);
}
