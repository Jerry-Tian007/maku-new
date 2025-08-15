package com.ruoyi.gpt.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.KbTrainFileMapper;
import com.ruoyi.gpt.model.domain.KbTrainFile;
import com.ruoyi.gpt.service.IKbTrainFileService;

/**
 * 知识库训练文件Service业务层处理
 * 
 * @author Eric
 * @date 2023-09-19
 */
@Service
public class KbTrainFileServiceImpl implements IKbTrainFileService 
{
    @Autowired
    private KbTrainFileMapper kbTrainFileMapper;

    /**
     * 查询知识库训练文件
     * 
     * @param fileId 知识库训练文件主键
     * @return 知识库训练文件
     */
    @Override
    public KbTrainFile selectKbTrainFileByFileId(String fileId)
    {
        return kbTrainFileMapper.selectKbTrainFileByFileId(fileId);
    }

    /**
     * 查询知识库训练文件列表
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 知识库训练文件
     */
    @Override
    public List<KbTrainFile> selectKbTrainFileList(KbTrainFile kbTrainFile)
    {
        return kbTrainFileMapper.selectKbTrainFileList(kbTrainFile);
    }

    /**
     * 新增知识库训练文件
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 结果
     */
    @Override
    public int insertKbTrainFile(KbTrainFile kbTrainFile)
    {
        kbTrainFile.setCreateTime(DateUtils.getNowDate());
        return kbTrainFileMapper.insertKbTrainFile(kbTrainFile);
    }

    /**
     * 修改知识库训练文件
     * 
     * @param kbTrainFile 知识库训练文件
     * @return 结果
     */
    @Override
    public int updateKbTrainFile(KbTrainFile kbTrainFile)
    {
        kbTrainFile.setUpdateTime(DateUtils.getNowDate());
        return kbTrainFileMapper.updateKbTrainFile(kbTrainFile);
    }

    /**
     * 批量删除知识库训练文件
     * 
     * @param fileIds 需要删除的知识库训练文件主键
     * @return 结果
     */
    @Override
    public int deleteKbTrainFileByFileIds(String[] fileIds)
    {
        return kbTrainFileMapper.deleteKbTrainFileByFileIds(fileIds);
    }

    /**
     * 删除知识库训练文件信息
     * 
     * @param fileId 知识库训练文件主键
     * @return 结果
     */
    @Override
    public int deleteKbTrainFileByFileId(String fileId)
    {
        return kbTrainFileMapper.deleteKbTrainFileByFileId(fileId);
    }

    @Override
    public List<KbTrainFile> selectUnTrainKbTrainFileList(KbTrainFile kbTrainFile) {
        return kbTrainFileMapper.selectUnTrainKbTrainFileList(kbTrainFile);
    }
}
