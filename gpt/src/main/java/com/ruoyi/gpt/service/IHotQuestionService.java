package com.ruoyi.gpt.service;


import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.gpt.model.domain.HotQuestion;
import com.ruoyi.gpt.model.vo.*;

import java.util.List;

/**
 * 热点问题Service接口
 *
 * @author lijw
 * @date 2023-11-30
 */
public interface IHotQuestionService
{
    /**
     * 查询热点问题 后管
     *
     * @param id 热点问题主键
     * @return 热点问题
     */
    public HotQuestion selectHotQuestionById(Long id);

    /**
     * 查询热点问题列表
     *
     * @param hotQuestionVo 热点问题
     * @return 热点问题集合
     */
    public List<HotQuestionVo> selectHotQuestionList(HotQuestionVo hotQuestionVo);



    public List<HotQuestionVo> selectHotQuestionListWoTai(String role, String region, int questionNum, String language);

    /**
     * 新增热点问题  后管 crud 热点问题插入， 传入的是region和role
     *
     * @param hotQuestion 热点问题
     * @return 结果
     */
    public int insertHotQuestion(HotQuestion hotQuestion);

    /**
     *
     * 增热点问题 热点问题分析插入 已设置好hotQuestionVo的 roleId和regionId
     *
     * @param hotQuestion 热点问题
     * @return 结果
     */
    public int insertHotQuestionKbqa(HotQuestion hotQuestion);

    /**
     * 修改热点问题
     *
     * @param hotQuestionVo 热点问题
     * @return 结果
     */
    public int updateHotQuestion(HotQuestionVo hotQuestionVo);

    JSONObject updateHotQuestionBackgroundBatch(List<HotQuestionEditBackgroundVo> hotQuestionEditBackgroundVoList);



    /**
     * 批量删除热点问题
     *
     * @param ids 需要删除的热点问题主键集合
     * @return 结果
     */
    public int deleteHotQuestionByIds(Long[] ids);
    public JSONObject deleteMultiHotQuestionByIds(Long[] ids);


    /**
     * 删除热点问题信息
     *
     * @param id 热点问题主键
     * @return 结果
     */
    public int deleteHotQuestionById(Long id);

    /**
     * 热点问题分析的config
     *
     * */
    public JSONObject getAnalysisConfig();

    /**
     * 热点问题训练的config
     *
     * */
    public JSONObject getTrainConfig();

    int updateBatchHotQuestionTranStatusByRecordId(HotQuestion hotQuestion);

    String importData(List<HotQuestion> hotQuestionList, boolean updateSupport);



    int deleteHotQuestionByRecordId(Long[] ids);

    JSONObject insertHotQuestionBatch(List<HotQuestionInsertBackgroundVo> HotQuestionInsertBackgroundVoList);

    JSONObject hotQuestionAnalysis(HotQuestionAnalyzeVo hotQuestionAnalyzeVo);

    JSONObject hotQuestionTrain(HotQuestionTrainVo hotQuestionTrainVo);

    List<String> randomQuestionList();
}
