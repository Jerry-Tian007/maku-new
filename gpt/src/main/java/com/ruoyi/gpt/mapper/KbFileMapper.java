package com.ruoyi.gpt.mapper;

import java.util.List;

import com.ruoyi.gpt.model.domain.KbFile;
import com.ruoyi.gpt.model.dto.kb_file.KbFileBookLevelUpdateDto;
import com.ruoyi.gpt.model.dto.kb_file.KbFilePageDto;
import com.ruoyi.gpt.model.dto.kb_file.KbFileUpdateTrainStatusDto;
import com.ruoyi.gpt.model.vo.kb_file.KbFileExportVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFilePageVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFileTrainVo;
import org.apache.ibatis.annotations.Param;

/**
 * 知识库文件Mapper接口
 *
 * @author Eric
 * @date 2023-05-16
 */
public interface KbFileMapper {
    /**
     * 知识库文件
     *
     * @param id 知识库文件主键
     * @return 知识库文件
     */
    KbFile selectKbFileById(@Param("id") Long id);

    /**
     * 查询知识库文件列表
     *
     * @param pageDto 知识库文件
     * @return 知识库文件集合
     */
    List<KbFilePageVo> selectKbFileList(@Param("pageDto") KbFilePageDto pageDto,
                                        @Param("size") Integer size,
                                        @Param("userName") String userName,
                                        @Param("roleIds") Long[] roleIds);

    /**
     * 新增知识库文件
     *
     * @param kbFile 知识库文件
     * @return 结果
     */
    Integer insertKbFile(@Param("kbFile") KbFile kbFile);

    /**
     * 更新文件启用禁用状态
     * @param kbFile
     * @return
     */
    Integer updateKbFileStatus(KbFile kbFile);
    
    /**
     * 更新知识库文件的书籍级别
     *
     * @param KbFileBookLevelUpdateDto 文件实体
     * @return 影响行数
     */
    Integer updateBookLevel(KbFileBookLevelUpdateDto KbFileBookLevelUpdateDto);
    
    /**
     * 删除知识库文件
     *
     * @param id
     * @return 结果
     */
    Integer deleteKbFileById(@Param("id") Long id);

    KbFile selectKbFileByFileNameAndSuffix(@Param("fileName") String fileName, @Param("suffix") String suffix);

    List<KbFile> selectKbFileListByFileNameAndSuffix(@Param("fileName") String fileName, @Param("suffix") String suffix);

    List<KbFileTrainVo> selectKbFileTrainVoByFileIds(@Param("fileIds") List<Long> fileIds);

    List<KbFile> selectKbFileByIds(@Param("fileIds") List<Long> fileIds);

    Integer deleteKbFileByIds(@Param("fileIds") List<Long> fileIds);

    Integer updateKbFileTrainStatus(@Param("kbFileUpdateTrainStatusDto") KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto);

    /**
     * 文件状态从未训练变成训练等待中
     *
     * @param kbFileUpdateTrainStatusDto
     * @return
     */
    Integer updateKbFileTrainStatusToWaiting(KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto);

    Integer updateKbFileTrainStatusToDeleting(KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto);

    Integer updateKbFileTrainStatusToUntrained(KbFileUpdateTrainStatusDto kbFileUpdateTrainStatusDto);

    List<KbFileExportVo> selectExportKbFileList(@Param("pageDto") KbFilePageDto pageDto,
                                                @Param("size") Integer size,
                                                @Param("userName") String userName,
                                                @Param("roleIds") Long[] roleIds);
}
