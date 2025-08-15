package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.model.domain.HotQuestion;
import com.ruoyi.gpt.model.vo.HotQuestionVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 热点问题Mapper接口
 *
 * @author lijw
 * @date 2023-11-30
 */
public interface HotQuestionMapper
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


    /**
     * 查询热点问题列表 wotai
     *
     * @return 热点问题
     */
    public List<HotQuestionVo> selectHotQuestionListWoTai(@Param("role") String role, @Param("region") String region, @Param("questionNum") int questionNum, @Param("language") String language);

    /**
     * 新增热点问题
     *
     * @param hotQuestion 热点问题
     * @return 结果
     */
    public int insertHotQuestion(HotQuestion hotQuestion);

    /**
     * 修改热点问题
     *
     * @param hotQuestionVo 热点问题
     * @return 结果
     */
    public int updateHotQuestion(HotQuestionVo hotQuestionVo);

    /**
     * 删除热点问题
     *
     * @param id 热点问题主键
     * @return 结果
     */
    public int deleteHotQuestionById(Long id);

    /**
     * 批量删除热点问题
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHotQuestionByIds(Long[] ids);

    /**
     * 更新 recordId 对应热点问题的tranStatus和 tranFlow
     * */
    int updateBatchHotQuestionTranStatusByRecordId(@Param("status" ) String status, @Param("trainFlow") String trainFlow,
                                         @Param("trainTime") Date trainTime, @Param("recordId") Long recordId, @Param("language" ) String language);

    List<HotQuestionVo> selectDistinctLanguage();

    int updateBatchHotQuestionByRecordId(HotQuestion hotQuestion);
    int updateSingleHotQuestionByRecordId(@Param("role" ) String role, @Param("region") String region, @Param("recordId") Long recordId, @Param("language") String language);

    int deleteHotQuestionByRecordIds(Long[] recordIds);
}
