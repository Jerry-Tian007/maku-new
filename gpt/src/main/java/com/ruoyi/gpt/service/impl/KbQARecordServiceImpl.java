package com.ruoyi.gpt.service.impl;

import java.util.Collections;
import java.util.List;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordStateDto;
import com.ruoyi.gpt.service.impl.util.RoleRegionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gpt.mapper.KbQARecordMapper;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.service.IKbQARecordService;

import javax.annotation.Resource;

/**
 * 知识库问答记录Service业务层处理
 *
 * @author Eric
 * @date 2023-07-11
 */
@Service
public class KbQARecordServiceImpl implements IKbQARecordService {
    @Resource
    private KbQARecordMapper kbQARecordMapper;

    @Autowired
    private RoleRegionServiceImpl roleRegionService;

    @Resource
    private DeptKbConfigServiceImpl deptKbConfigService;


    /**
     * 查询知识库问答记录
     *
     * @param id 知识库问答记录主键
     * @return 知识库问答记录
     */
    @Override
    public KbQARecordDto selectKbQARecordById(Long id) {
        return kbQARecordMapper.selectKbQARecordById(id);
    }

    @Override
    public String countKbQARecordState(KbQARecordDto kbQARecordDto) {

        KbQARecordStateDto state = kbQARecordMapper.countKbQARecordState(kbQARecordDto);
        return ReturnMessageEnum.solved + state.getSolved_count()
                + "_" + ReturnMessageEnum.unsolved + state.getUnsolved_count()
                + "_" + ReturnMessageEnum.accurate + state.getYes_count()
                + "_" + ReturnMessageEnum.inaccurate + state.getNo_count()
                + "_" + ReturnMessageEnum.unrated + state.getNull_count();

    }

    /**
     * 查询知识库问答记录列表
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 知识库问答记录
     */
    @Override
    public List<KbQARecordDto> selectKbQARecordList(KbQARecordDto kbQARecordDto) {
        kbQARecordDto.setCreateByAccount(SecurityUtils.getUsername());
        return kbQARecordMapper.selectKbQARecordList(kbQARecordDto);
    }

    /**
     * 查询知识库问答记录列表
     *
     * @param kbQARecord 知识库问答记录
     * @return 知识库问答记录
     */
    @Override
    public List<KbQARecord> selectKbQASessionRecordList(KbQARecord kbQARecord) {
        kbQARecord.setUserId(SecurityUtils.getUserId());
        return kbQARecordMapper.selectKbQASessionRecordList(kbQARecord);
    }

    /**
     * 通过sessionId查询一个session下的多条qa
     *
     * @param sessionId 知识库会话唯一标识
     * @return 知识库指定会话问答记录集合
     */
    @Override
    public List<KbQARecordDto> selectKbQARecordBySessionId(String sessionId) {
        return kbQARecordMapper.selectKbQARecordBySessionId(sessionId);
    }

    /**
     * 通过userId查询多个session下的多条qa
     *
     * @param userId 知识库会话唯一标识
     * @return 知识库指定会话问答记录集合
     */
    @Override
    public List<KbQARecordDto> selectKbQARecordByUserId(Long userId) {
        return kbQARecordMapper.selectKbQARecordByUserId(userId);
    }

    @Override
    public List<KbQARecord> selectSessionIdsByUserId(Long userId) {
        return kbQARecordMapper.selectSessionIdsByUserId(userId);
    }


    /**
     * 给通过userId获取history使用， 作为total数量
     *
     * @param userId 知识库会话唯一标识
     * @return kbQARecordDto集合
     */
    public List<KbQARecordDto> selectSessionIdsByUserIdWithOut24(Long userId) {
        return kbQARecordMapper.selectSessionIdsByUserIdWithOut24(userId);
    }

    @Override
    public List<KbQARecord> selectChatTitlesByUserId(Long userId) {
        return kbQARecordMapper.selectChatTitlesByUserId(userId);
    }

    /**
     * 通过regionId数组和roleId数组查询kbqa，
     */
    @Override
    public List<Long> selectKbQARecordByRegionsAndRoles(long[] regionId, long[] roleId) {

        return kbQARecordMapper.selectKbQARecordByRegionsAndRoles(regionId, roleId);
    }

    /**
     * 新增知识库问答记录 后管
     *
     * @param kbQARecord 知识库问答记录
     * @return 结果
     */
    @Override
    public Integer insertKbQARecord(KbQARecord kbQARecord) {
        kbQARecord.setUserId(SecurityUtils.getUserId());
        kbQARecord.setDeptId(SecurityUtils.getDeptId());
        kbQARecord.setCreateBy(SecurityUtils.getUsername());
        kbQARecord.setCreateTime(DateUtils.getNowDate());
        return kbQARecordMapper.insertKbQARecord(kbQARecord);
    }

    /**
     * 新增知识库问答记录， 智能问答页面
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 结果
     */
    @Override
    public Long insertKbQARecordKbqa(KbQARecordDto kbQARecordDto) {
        kbQARecordMapper.insertKbQARecord(kbQARecordDto);
        return kbQARecordDto.getId();
    }

    /**
     * 修改知识库问答记录
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 结果
     */
    @Override
    public Integer updateKbQARecord(KbQARecordDto kbQARecordDto) {
        return kbQARecordMapper.updateKbQARecord(kbQARecordDto);
    }

    @Override
    public Integer updateAccuracyOrNot(long id, String isAccuracy) {
        return kbQARecordMapper.updateAccuracyOrNot(id, isAccuracy);
    }

    /**
     * 批量删除知识库问答记录
     *
     * @param ids 需要删除的知识库问答记录主键
     * @return 结果
     */
    @Override
    public Integer deleteKbQARecordByIds(Long[] ids) {
        return kbQARecordMapper.deleteKbQARecordByIds(ids);
    }

    /**
     * 删除知识库问答记录信息
     *
     * @param id 知识库问答记录主键
     * @return 结果
     */
    @Override
    public int deleteKbQARecordById(Long id) {
        return kbQARecordMapper.deleteKbQARecordById(id);
    }

    /**
     * 获取知识库智能问答config
     */
    @Override
    public JSONObject getKbQAConfig() {
        DeptKbConfig deptKbConfig = deptKbConfigService.selectDeptKbConfigById(2L);
        JSONObject kbConfigJson = new JSONObject();
        kbConfigJson.putOpt("openaiApiBase", deptKbConfig.getOpenaiApiBase());
        kbConfigJson.putOpt("openaiApiVersion", deptKbConfig.getOpenaiApiVersion());
        kbConfigJson.putOpt("openaiApiKey", deptKbConfig.getOpenaiApiKey());
        kbConfigJson.putOpt("openaiChatModel", deptKbConfig.getOpenaiChatgptDeployment());
        kbConfigJson.putOpt("openaiEmbeddingModel", deptKbConfig.getOpenaiQueryEmbeddingDeployment());
        kbConfigJson.putOpt("azureBlobStorageConnectionString", deptKbConfig.getAzureBlobStorageConnectionString());
        kbConfigJson.putOpt("azureVectorStorageTableName", "");
        kbConfigJson.putOpt("kbFieldsContent", deptKbConfig.getKbFieldsContent());
        kbConfigJson.putOpt("kbFieldsSourcepage", deptKbConfig.getKbFieldsSourcepage());
        kbConfigJson.putOpt("redisHost", deptKbConfig.getRedisHost());
        kbConfigJson.putOpt("redisHost", deptKbConfig.getRedisHost());
        kbConfigJson.putOpt("redisPassword", deptKbConfig.getRedisPassword());
        kbConfigJson.putOpt("redisPort", deptKbConfig.getRedisPort());
        return kbConfigJson;
    }

    /**
     * 更新知识库问答记录
     *
     * @param kbQARecordDto 知识库问答记录
     * @return 结果
     */
    @Override
    public int updateKbQAAnswer(KbQARecordDto kbQARecordDto) {
        return kbQARecordMapper.updateKbQAAnswer(kbQARecordDto);
    }

    @Override
    public List<KbQARecord> getHistoryBySessionId(String sessionId) {
        return kbQARecordMapper.getHistoryBySessionId(sessionId);
    }

}
