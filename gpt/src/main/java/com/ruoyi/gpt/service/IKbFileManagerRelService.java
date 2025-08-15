package com.ruoyi.gpt.service;

import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelInsertDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateBatchDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateDto;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbIdVo;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbNameVo;

import java.util.List;

/**
 * 文件与知识库关联关系表Service
 *
 */
public interface IKbFileManagerRelService {

    /**
     * 新增文件与知识库关联关系
     *
     * @param kbFileManagerRelInsertDto
     * @return
     */
    Integer insertKbFileManagerRel(KbFileManagerRelInsertDto kbFileManagerRelInsertDto);

    /**
     * 更新文件与知识库关联关系
     *
     * @param kbFileManagerRelUpdateDto
     * @return
     */
    Integer updateKbFileManagerRel(KbFileManagerRelUpdateDto kbFileManagerRelUpdateDto);

    /**
     * 批量更新文件与知识库关联关系
     *
     * @param kbFileManagerRelUpdateBatchDto
     * @return
     */
    Integer updateBatchKbFileManagerRel(KbFileManagerRelUpdateBatchDto kbFileManagerRelUpdateBatchDto);

    /**
     * 根据文件id删除文件与知识库关联关系
     *
     * @param fileId
     * @return
     */
    Integer deleteKbFileManagerRelByFileId(Long fileId);

    /**
     * 根据知识库id删除文件与知识库关联关系
     *
     * @param kbId
     * @return
     */
    Integer deleteKbFileManagerRelByKbId(Long kbId);

    /**
     * 根据文件ids批量删除文件与知识库关联关系
     *
     * @param fileIds
     * @return
     */
    Integer deleteKbFileManagerRelByFileIds(Long[] fileIds);

    /**
     * 根据文件ids批量查询对应的知识库名称
     *
     * @param fileIds
     * @return
     */
    List<FileIdToKbNameVo> selectFileIdToKbNameListByFileIds(List<Long> fileIds);

    /**
     * 根据文件ids查询文件id对应的知识库id
     *
     * @param fileIds
     * @return
     */
    List<FileIdToKbIdVo> selectFileIdToKbIdByFileIds(List<Long> fileIds);

    /**
     * 查询被删除的知识库下的文件id对应的知识库id
     *
     * @param deletedKbId
     * @return
     */
    List<FileIdToKbIdVo> selectFileIdToKbIdByDeletedKbId(Long deletedKbId);
}
