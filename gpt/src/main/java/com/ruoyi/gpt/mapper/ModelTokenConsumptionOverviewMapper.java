package com.ruoyi.gpt.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.ModelTokenConsumptionOverviewDto;
import com.ruoyi.gpt.model.vo.ModelTokenConsumptionOverviewVo;

/**
 * GPT消费概览Mapper接口
 *
 * @author yecao
 * @date 2023-04-28
 */
public interface ModelTokenConsumptionOverviewMapper
{
    /**
     * 查询GPT消费概览
     *
     * @param id GPT消费概览主键
     * @return GPT消费概览
     */
    public ModelTokenConsumptionOverview selectModelTokenConsumptionOverviewById(Long id);

    public ModelTokenConsumptionOverview selectModelTokenConsumptionOverviewByDeptId(Long deptId);

    public ModelTokenConsumptionOverview selectDeptOverviewByDeptId(Long deptId);

    /**
     * 查询GPT消费概览列表
     *
     * @param modelTokenConsumptionOverviewDto GPT消费概览
     * @return GPT消费概览集合
     */
    public List<ModelTokenConsumptionOverviewVo> selectModelTokenConsumptionOverviewList(ModelTokenConsumptionOverviewDto modelTokenConsumptionOverviewDto);

    List<SysUser> selectGptSysUserList();

    public ModelTokenConsumptionOverview selectModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 新增GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    @GenerateUniqueId
    public int insertModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 修改GPT消费概览
     *
     * @param modelTokenConsumptionOverview GPT消费概览
     * @return 结果
     */
    public int updateModelTokenConsumptionOverview(ModelTokenConsumptionOverview modelTokenConsumptionOverview);

    /**
     * 删除GPT消费概览
     *
     * @param id GPT消费概览主键
     * @return 结果
     */
    public int deleteModelTokenConsumptionOverviewById(Long id);

    /**
     * 批量删除GPT消费概览
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelTokenConsumptionOverviewByIds(Long[] ids);
}
