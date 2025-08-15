package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.ModelTokenConsumptionOverviewDto;
import com.ruoyi.gpt.model.dto.UpdateMaxConsumptionAmountDto;
import com.ruoyi.gpt.model.vo.ModelTokenConsumptionOverviewVo;
import com.ruoyi.system.domain.event.CreateUserEvent;

/**
 * GPT消费概览Service接口
 *
 * @author yecao
 * @date 2023-04-28
 */
public interface IModelTokenConsumptionOverviewService
{
    /**
     * 查询GPT消费概览
     *
     * @param id GPT消费概览主键
     * @return GPT消费概览
     */
    public ModelTokenConsumptionOverview selectModelTokenConsumptionOverviewById(Long id);

    /**
     * 查询GPT消费概览列表
     *
     * @param modelTokenConsumptionOverviewDto GPT消费概览
     * @return GPT消费概览集合
     */
    public List<ModelTokenConsumptionOverviewVo> selectModelTokenConsumptionOverviewList(ModelTokenConsumptionOverviewDto modelTokenConsumptionOverviewDto);

    /**
     * 核算GPT消费概览列表
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return GPT消费概览集合
     */
    public int totalCost(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 个人概览核算
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    public int userTotalCost(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 修改最大消费金额 用于个人消费概览
     * @param updateMaxConsumptionAmountDto GPT消费概览
     * @return 结果
     */
    public int updateMaxConsumptionAmount(UpdateMaxConsumptionAmountDto updateMaxConsumptionAmountDto);

    /**
     * 生成账单
     * @param modelTokenConsumptionOverview
     * @return 结果
     */
    public int generateBill(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 初始化GPT消费概览
     * @return 结果
     */
   public int modelTokenConsumptionOverviewInit(CreateUserEvent createUserEvent);

    /**
     * 重置最大消费金额 每月月底执行
     */
   public void resetMaximumLimitJob();

    /**
     * 新增GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    public int insertModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 修改GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    public int updateModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 批量删除GPT消费概览
     *
     * @param ids 需要删除的GPT消费概览主键集合
     * @return 结果
     */
    public int deleteModelTokenConsumptionOverviewByIds(Long[] ids);

    /**
     * 删除GPT消费概览信息
     *
     * @param id GPT消费概览主键
     * @return 结果
     */
    public int deleteModelTokenConsumptionOverviewById(Long id);
}
