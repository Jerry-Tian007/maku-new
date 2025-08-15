package com.ruoyi.gpt.controller.gptbill;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.ModelTokenConsumptionOverviewDto;
import com.ruoyi.gpt.model.dto.UpdateMaxConsumptionAmountDto;
import com.ruoyi.gpt.model.vo.ModelTokenConsumptionOverviewVo;
import com.ruoyi.gpt.service.IModelTokenConsumptionOverviewService;
import com.ruoyi.system.domain.event.CreateUserEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * GPT消费概览Controller
 *
 * @author yecao
 * @date 2023-04-28
 */
@RestController
@RequestMapping("/gpt/gptconsumptionoverview")
public class ModelTokenConsumptionOverviewController extends BaseController {

    @Resource
    private IModelTokenConsumptionOverviewService modelTokenConsumptionOverviewService;

    /**
     * 查询GPT消费概览列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:list')")
    @GetMapping("/list")
    public TableDataInfo list(ModelTokenConsumptionOverviewDto modelTokenConsumptionOverviewDto) {
        startPage();
        List<ModelTokenConsumptionOverviewVo> list = modelTokenConsumptionOverviewService.selectModelTokenConsumptionOverviewList(modelTokenConsumptionOverviewDto);
        return getDataTable(list);
    }

    /**
     * 核算GPT消费概览列表
     *
     * @param modelTokenConsumptionOverview 消费概览
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:query')")
    @GetMapping("/total-cost")
    public AjaxResult totalCost(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        int i = modelTokenConsumptionOverviewService.totalCost(modelTokenConsumptionOverview);
        if (i == 1 || i == 2) {
            return AjaxResult.success("核算成功");
        }
        return AjaxResult.error("核算失败");
    }

    /**
     * 核算GPT消费概览列表 个人
     *
     * @param modelTokenConsumptionOverview 消费概览
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:query')")
    @GetMapping("/user/total-cost")
    public AjaxResult totalCostByUser(ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        int i = modelTokenConsumptionOverviewService.userTotalCost(modelTokenConsumptionOverview);
        if (i == 1 || i == 2) {
            return AjaxResult.success("核算成功");
        }
        return AjaxResult.error("核算失败");
    }

    /**
     * 修改GPT最高消费
     *
     * @param updateMaxConsumptionAmountDto 消费概览
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:edit')")
    @PostMapping("/update/maxConsumptionAmount")
    public AjaxResult updateMaxConsumptionAmount(@RequestBody UpdateMaxConsumptionAmountDto updateMaxConsumptionAmountDto) {
        int i = modelTokenConsumptionOverviewService.updateMaxConsumptionAmount(updateMaxConsumptionAmountDto);
        if (i == -1) {
            return AjaxResult.error("修改失败，最高消费不能小于当前消费");
        }
        return AjaxResult.success();
    }

    /**
     * 生成账单
     *
     * @param modelTokenConsumptionOverview
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:generateBill')")
    @PostMapping("/generate-bill")
    public AjaxResult generateBill(@RequestBody ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        return AjaxResult.success(modelTokenConsumptionOverviewService.generateBill(modelTokenConsumptionOverview));
    }

    /**
     * 导出GPT消费概览列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:export')")
    @Log(title = "GPT消费概览", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ModelTokenConsumptionOverviewDto modelTokenConsumptionOverviewDto) {
        List<ModelTokenConsumptionOverviewVo> list = modelTokenConsumptionOverviewService.selectModelTokenConsumptionOverviewList(modelTokenConsumptionOverviewDto);
        ExcelUtil<ModelTokenConsumptionOverviewVo> util = new ExcelUtil<ModelTokenConsumptionOverviewVo>(ModelTokenConsumptionOverviewVo.class);
        util.exportExcel(response, list, "GPT消费概览数据");
    }

    /**
     * 获取GPT消费概览详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(modelTokenConsumptionOverviewService.selectModelTokenConsumptionOverviewById(id));
    }

    /**
     * GPT消费概览初始化
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:add')")
    @Log(title = "GPT消费概览初始化", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    public AjaxResult init(CreateUserEvent createUserEvent) {
        return AjaxResult.success(modelTokenConsumptionOverviewService.modelTokenConsumptionOverviewInit(createUserEvent));
    }

    /**
     * 新增GPT消费概览
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:add')")
    @Log(title = "GPT消费概览", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        return toAjax(modelTokenConsumptionOverviewService.insertModelTokenConsumptionOverview(modelTokenConsumptionOverview));
    }

    /**
     * 修改GPT消费概览
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:edit')")
    @Log(title = "GPT消费概览", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        return toAjax(modelTokenConsumptionOverviewService.updateModelTokenConsumptionOverview(modelTokenConsumptionOverview));
    }

    /**
     * 删除GPT消费概览
     */
    @PreAuthorize("@ss.hasPermi('gpt:gptconsumptionoverview:remove')")
    @Log(title = "GPT消费概览", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(modelTokenConsumptionOverviewService.deleteModelTokenConsumptionOverviewByIds(ids));
    }
}
