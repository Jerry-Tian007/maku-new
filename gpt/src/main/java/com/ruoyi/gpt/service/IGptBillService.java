package com.ruoyi.gpt.service;

import java.util.List;
import com.ruoyi.gpt.model.domain.GptBill;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.GPTBillDto;
import com.ruoyi.gpt.model.vo.GptBillVo;

/**
 * GPT账单Service接口
 *
 * @author yecao
 * @date 2023-04-28
 */
public interface IGptBillService
{
    /**
     * 查询GPT账单
     *
     * @param id GPT账单主键
     * @return GPT账单
     */
    public GptBill selectGptBillById(Long id);

    /**
     * 查询GPT账单列表
     *
     * @param gptBillDto GPT账单
     * @return GPT账单集合
     */
    public List<GptBillVo> selectGptBillList(GPTBillDto gptBillDto);

    /**
     * 新增GPT账单
     *
     * @param modelTokenConsumptionOverview GPT账单
     * @return 结果
     */
    public int insertGptBill(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 修改GPT账单
     *
     * @param gptBill GPT账单
     * @return 结果
     */
    public int updateGptBill(GptBill gptBill);

    /**
     * 批量删除GPT账单
     *
     * @param ids 需要删除的GPT账单主键集合
     * @return 结果
     */
    public int deleteGptBillByIds(Long[] ids);

    /**
     * 删除GPT账单信息
     *
     * @param id GPT账单主键
     * @return 结果
     */
    public int deleteGptBillById(Long id);
}
