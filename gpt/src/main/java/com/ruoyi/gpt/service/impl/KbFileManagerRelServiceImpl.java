package com.ruoyi.gpt.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanConvertUtil;
import com.ruoyi.gpt.mapper.KbFileManagerRelMapper;
import com.ruoyi.gpt.model.domain.KbFileManagerRel;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelInsertBatchDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelInsertDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateBatchDto;
import com.ruoyi.gpt.model.dto.kb_file_manager_rel.KbFileManagerRelUpdateDto;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbIdVo;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbNameVo;
import com.ruoyi.gpt.service.IKbFileManagerRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件与知识库关联关系表Service业务处理层
 */
@Service
public class KbFileManagerRelServiceImpl implements IKbFileManagerRelService {

    @Autowired
    private KbFileManagerRelMapper kbFileManagerRelMapper;

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    /**
     * 新增文件与知识库关联关系
     *
     * @param insertDto
     * @return
     */
    @Override
    public Integer insertKbFileManagerRel(KbFileManagerRelInsertDto insertDto) {
        // 建立file_id与kb_id关联关系
        Long[] kbIds = insertDto.getKbIds();
        List<KbFileManagerRel> kbFileManagerRelList = Arrays.stream(kbIds).map(kbId -> {
            KbFileManagerRel kbFileManagerRel = new KbFileManagerRel();
            kbFileManagerRel.setId(snowflakeGenerator.next());
            kbFileManagerRel.setFileId(insertDto.getFileId());
            kbFileManagerRel.setCreateBy(SecurityUtils.getUsername());
            kbFileManagerRel.setCreateTime(new Date());
            kbFileManagerRel.setKbId(kbId);
            return kbFileManagerRel;
        }).collect(Collectors.toList());
        return kbFileManagerRelMapper.insertKbFileManagerRel(kbFileManagerRelList);
    }

    /**
     * 批量新增文件与知识库关联关系
     *
     * @param insertBatchDto
     * @return
     */
    public Integer insertBatchKbFileManagerRel(KbFileManagerRelInsertBatchDto insertBatchDto) {
        // 建立file_id与kb_id关联关系
        Long[] fileIds = insertBatchDto.getFileIds();
        Long[] kbIds = insertBatchDto.getKbIds();

        List<KbFileManagerRel> kbFileManagerRelList = Arrays.stream(fileIds).flatMap(
                fileId -> Arrays.stream(kbIds).map(kbId -> {
                    KbFileManagerRel kbFileManagerRel = new KbFileManagerRel();
                    kbFileManagerRel.setId(snowflakeGenerator.next());
                    kbFileManagerRel.setFileId(fileId);
                    kbFileManagerRel.setCreateBy(SecurityUtils.getUsername());
                    kbFileManagerRel.setCreateTime(new Date());
                    kbFileManagerRel.setKbId(kbId);
                    return kbFileManagerRel;
                })).collect(Collectors.toList());

        return kbFileManagerRelMapper.insertKbFileManagerRel(kbFileManagerRelList);
    }

    /**
     * 更新文件与知识库关联关系
     *
     * @param kbFileManagerRelUpdateDto
     * @return
     */
    @Override
    @Transactional
    public Integer updateKbFileManagerRel(KbFileManagerRelUpdateDto kbFileManagerRelUpdateDto) {
        // 先删除
        deleteKbFileManagerRelByFileId(kbFileManagerRelUpdateDto.getFileId());
        // 再新增
        KbFileManagerRelInsertDto kbFileManagerRelInsertDto = BeanConvertUtil.conver(kbFileManagerRelUpdateDto, KbFileManagerRelInsertDto.class);
        return insertKbFileManagerRel(kbFileManagerRelInsertDto);
    }

    /**
     * 批量更新文件与知识库关联关系
     *
     * @param kbFileManagerRelUpdateBatchDto
     * @return
     */
    @Override
    @Transactional
    public Integer updateBatchKbFileManagerRel(KbFileManagerRelUpdateBatchDto kbFileManagerRelUpdateBatchDto) {
        deleteKbFileManagerRelByFileIds(kbFileManagerRelUpdateBatchDto.getFileIds());
        KbFileManagerRelInsertBatchDto kbFileManagerRelInsertBatchDto = BeanConvertUtil.conver(kbFileManagerRelUpdateBatchDto, KbFileManagerRelInsertBatchDto.class);
        return insertBatchKbFileManagerRel(kbFileManagerRelInsertBatchDto);
    }

    /**
     * 根据文件id删除文件与知识库关联关系
     *
     * @param fileId
     * @return
     */
    @Override
    public Integer deleteKbFileManagerRelByFileId(Long fileId) {
        return kbFileManagerRelMapper.deleteKbFileManagerRelByFileId(fileId);
    }

    /**
     * 根据知识库id删除文件与知识库关联关系
     *
     * @param kbId
     * @return
     */
    @Override
    public Integer deleteKbFileManagerRelByKbId(Long kbId) {
        return kbFileManagerRelMapper.deleteKbFileManagerRelByKbId(kbId);
    }

    /**
     * 根据文件ids批量删除文件与知识库关联关系
     *
     * @param fileIds
     * @return
     */
    @Override
    public Integer deleteKbFileManagerRelByFileIds(Long[] fileIds) {
        return kbFileManagerRelMapper.deleteKbFileManagerRelByFileIds(fileIds);
    }

    /**
     * 根据文件ids批量查询对应的知识库名称
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<FileIdToKbNameVo> selectFileIdToKbNameListByFileIds(List<Long> fileIds) {
        return kbFileManagerRelMapper.selectFileIdToKbNameListByFileIds(fileIds);
    }

    /**
     * 根据文件ids查询文件id对应的知识库id
     *
     * @param fileIds
     * @return
     */
    @Override
    public List<FileIdToKbIdVo> selectFileIdToKbIdByFileIds(List<Long> fileIds) {
        return kbFileManagerRelMapper.selectFileIdToKbIdByFileIds(fileIds);
    }

    /**
     * 查询被删除的知识库下的文件id对应的知识库id
     *
     * @param deletedKbId
     * @return
     */
    @Override
    public List<FileIdToKbIdVo> selectFileIdToKbIdByDeletedKbId(Long deletedKbId) {
        return kbFileManagerRelMapper.selectFileIdToKbIdByDeletedKbId(deletedKbId);
    }
}
