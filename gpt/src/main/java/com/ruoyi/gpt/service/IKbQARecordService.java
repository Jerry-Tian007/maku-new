package com.ruoyi.gpt.service;

import java.util.List;

import cn.hutool.json.JSONObject;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;

/**
 * 知识库问答记录Service接口
 *
 * @author Eric
 * @date 2023-07-11
 */
public interface IKbQARecordService {
    /**
     * 查询知识库问答记录
     *
     * @param id 知识库问答记录主键
     * @return 知识库问答记录
     */
    KbQARecordDto selectKbQARecordById(Long id);

    /**
     * 查询知识库问答记录
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 知识库问答记录
     */
    String countKbQARecordState(KbQARecordDto kbQARecordDto);

    /**
     * 查询知识库问答记录列表
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 知识库问答记录集合
     */
    List<KbQARecordDto> selectKbQARecordList(KbQARecordDto kbQARecordDto);

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
    List<Long> selectKbQARecordByRegionsAndRoles(long[] regionIds, long[] roleIds);


    /**
     * 新增知识库问答记录, 后管
     *
     * @return 结果
     */
    Integer insertKbQARecord(KbQARecord kbQARecord);


    /**
     * 新增知识库问答记录, 智能问答页面
     *
     * @return 结果
     */
    Long insertKbQARecordKbqa(KbQARecordDto kbQARecordDto);

    /**
     * 修改知识库问答记录
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 结果
     */
    Integer updateKbQARecord(KbQARecordDto kbQARecordDto);


    /**
     * 点赞点踩
     */
    Integer updateAccuracyOrNot(long id, String isAccuracy);

    /**
     * 批量删除知识库问答记录
     *
     * @param ids 需要删除的知识库问答记录主键集合
     * @return 结果
     */
    Integer deleteKbQARecordByIds(Long[] ids);

    /**
     * 删除知识库问答记录信息
     *
     * @param id 知识库问答记录主键
     * @return 结果
     */
    int deleteKbQARecordById(Long id);


    /**
     * 知识库智能问答的 config
     */
    public JSONObject getKbQAConfig();

    /**
     * 更新知识库问答记录
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 结果
     */
    int updateKbQAAnswer(KbQARecordDto kbQARecordDto);

    List<KbQARecord> getHistoryBySessionId(String sessionId);

}
