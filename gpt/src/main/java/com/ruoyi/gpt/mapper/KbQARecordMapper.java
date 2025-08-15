package com.ruoyi.gpt.mapper;

import java.util.List;

import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.dto.chat_model_session.SessionHistoryDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordStateDto;
import com.ruoyi.gpt.model.vo.chat_model_session.SessionHistoryVo;
import org.apache.ibatis.annotations.Param;

/**
 * 知识库问答记录Mapper接口
 *
 * @author Eric
 * @date 2023-07-11
 */
public interface KbQARecordMapper {
    /**
     * 查询知识库问答记录
     *
     * @param id 知识库问答记录主键
     * @return 知识库问答记录
     */
    KbQARecordDto selectKbQARecordById(Long id);

    /**
     * 查询知识库问答记录列表
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 知识库问答记录集合
     */
    List<KbQARecordDto> selectKbQARecordList(KbQARecordDto kbQARecordDto);

    /**
     * 统计
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 知识库问答记录集合
     */
    KbQARecordStateDto countKbQARecordState(KbQARecordDto kbQARecordDto);

    /**
     * 查询知识库指定会话问答记录列表
     *
     * @param kbQARecord 知识库问答记录
     * @return 知识库指定会话问答记录集合
     */
    List<KbQARecord> selectKbQASessionRecordList(KbQARecord kbQARecord);

    /**
     * 通过sessionId查询一个session下的多条qa
     *
     * @param sessionId 知识库会话唯一标识
     * @return 知识库指定会话问答记录集合
     */
    List<KbQARecordDto> selectKbQARecordBySessionId(String sessionId);

    /**
     * 通过userId分页查询该userId的所有历史记录
     *
     * @param userId 知识库会话唯一标识
     * @return 知识库指定会话问答记录集合
     */
    List<KbQARecordDto> selectKbQARecordByUserId(Long userId);

    /**
     * 通过userId查找其所有的sessionids
     *
     * @param userId 知识库会话唯一标识
     * @return 多个sessionid形成的集合
     */
    List<KbQARecord> selectSessionIdsByUserId(Long userId);


    /**
     * 给通过userId获取history使用， 作为total数量
     *
     * @param userId 知识库会话唯一标识
     * @return kbQARecordDto集合
     */
    List<KbQARecordDto> selectSessionIdsByUserIdWithOut24(Long userId);

    /**
     * 通过userId查找其所有的chatTitles
     *
     * @param userId 知识库会话唯一标识
     * @return 多个chatTitles形成的集合
     */
    List<KbQARecord> selectChatTitlesByUserId(Long userId);


    /**
     * 通过regionId数组和roleId数组查询kbqa，
     */

    List<Long> selectKbQARecordByRegionsAndRoles(@Param("regionIds") long[] regionIds, @Param("roleIds") long[] roleIds);

    /**
     * 新增知识库问答记录, 后管&智能问答
     *
     * @param kbQARecord 知识库问答记录
     * @return 结果
     */
    @GenerateUniqueId
    int insertKbQARecord(KbQARecord kbQARecord);

    /**
     * 修改知识库问答记录
     *
     * @param kbQARecord 知识库问答记录
     * @return 结果
     */
    int updateKbQARecord(KbQARecord kbQARecord);


    /**
     * 点赞点踩
     *
     * @param id         主键ID
     * @param isAccuracy yes为准确 no 不准确
     * @return
     */
    int updateAccuracyOrNot(@Param("id") long id, @Param("isAccuracy") String isAccuracy);

    /**
     * 删除知识库问答记录
     *
     * @param id 知识库问答记录主键
     * @return 结果
     */
    int deleteKbQARecordById(Long id);

    /**
     * 批量删除知识库问答记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteKbQARecordByIds(Long[] ids);

    int updateKbQAAnswer(KbQARecordDto kbQARecordDto);

    List<KbQARecord> getHistoryBySessionId(String sessionId);

}
