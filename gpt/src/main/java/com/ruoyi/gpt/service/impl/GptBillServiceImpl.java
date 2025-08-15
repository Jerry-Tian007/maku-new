package com.ruoyi.gpt.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.mapper.ChatgptQARecordMapper;
import com.ruoyi.gpt.mapper.GptBillMapper;
import com.ruoyi.gpt.model.domain.GptBill;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.GPTBillDto;
import com.ruoyi.gpt.model.vo.GptBillVo;
import com.ruoyi.gpt.service.IGptBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * GPT账单Service业务层处理
 *
 * @author yecao
 * @Date 2021-04-28
 */
@Service
public class GptBillServiceImpl implements IGptBillService
{
    @Resource
    private GptBillMapper gptBillMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ChatgptQARecordMapper chatgptQARecordMapper;

    /**
     * 查询GPT账单
     *
     * @param id GPT账单主键
     * @return GPT账单
     */
    @Override
    public GptBill selectGptBillById(Long id)
    {
        return gptBillMapper.selectGptBillById(id);
    }

    /**
     * 查询GPT账单列表
     *
     * @param gptBillDto GPT账单
     * @return GPT账单
     */
    @Override
    @DataScope(deptAlias = "gb")
    public List<GptBillVo> selectGptBillList(GPTBillDto gptBillDto)
    {
        return gptBillMapper.selectGptBillList(gptBillDto);
    }

    /**
     * 新增GPT账单
     *
     * @param modelTokenConsumptionOverview GPT账单
     * @return 结果
     */
    @Override
    public int insertGptBill(ModelTokenConsumptionOverview modelTokenConsumptionOverview)
    {
        GptBill unpaid = GptBill.builder()
                .userId(modelTokenConsumptionOverview.getUserId())
                .deptId(modelTokenConsumptionOverview.getDeptId())
                .amountOfBill(modelTokenConsumptionOverview.getCurrentBillConsumptionAmount())
                .amountOfBillDetail(modelTokenConsumptionOverview.getOverviewDetail())
                .billingStatus("unpaid")
                .build();
        unpaid.setCreateTime(DateUtils.getNowDate());
        unpaid.setCreateBy(SecurityUtils.getUsername());
        return gptBillMapper.insertGptBill(unpaid);
    }

    /**
     * 修改GPT账单
     *
     * @param gptBill GPT账单
     * @return 结果
     */
    @Override
    public int updateGptBill(GptBill gptBill)
    {
        gptBill.setUpdateTime(DateUtils.getNowDate());
        return gptBillMapper.updateGptBill(gptBill);
    }

    /**
     * 批量删除GPT账单
     *
     * @param ids 需要删除的GPT账单主键
     * @return 结果
     */
    @Override
    public int deleteGptBillByIds(Long[] ids)
    {
        return gptBillMapper.deleteGptBillByIds(ids);
    }

    /**
     * 删除GPT账单信息
     *
     * @param id GPT账单主键
     * @return 结果
     */
    @Override
    public int deleteGptBillById(Long id)
    {
        return gptBillMapper.deleteGptBillById(id);
    }
}
