package com.ruoyi.gpt.mapper;


import com.ruoyi.gpt.model.domain.KbFileManagerRel;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbIdVo;
import com.ruoyi.gpt.model.vo.kb_file_manager_rel.FileIdToKbNameVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件知识库关联关系表Mapper接口
 *
 */
public interface KbFileManagerRelMapper {

    Integer insertKbFileManagerRel(@Param("kbFileManagerRelList") List<KbFileManagerRel> kbFileManagerRelList);

    Integer deleteKbFileManagerRelByFileId(@Param("fileId") Long fileId);

    Integer deleteKbFileManagerRelByKbId(@Param("kbId") Long kbId);

    Integer deleteKbFileManagerRelByFileIds(@Param("fileIds") Long[] fileIds);

    List<FileIdToKbNameVo> selectFileIdToKbNameListByFileIds(@Param("fileIds") List<Long> fileIds);

    List<FileIdToKbIdVo> selectFileIdToKbIdByFileIds(@Param("fileIds") List<Long> fileIds);

    List<FileIdToKbIdVo> selectFileIdToKbIdByDeletedKbId(@Param("deletedKbId") Long deletedKbId);
}
