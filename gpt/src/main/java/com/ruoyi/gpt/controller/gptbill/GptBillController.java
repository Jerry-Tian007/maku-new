package com.ruoyi.gpt.controller.gptbill;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.GptBill;
import com.ruoyi.gpt.model.domain.ModelTokenConsumptionOverview;
import com.ruoyi.gpt.model.dto.GPTBillDto;
import com.ruoyi.gpt.model.vo.GptBillVo;
import com.ruoyi.gpt.service.IGptBillService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * GPT账单Controller
 *
 * @author yecao
 * @date 2023-04-28
 */
@RestController
@RequestMapping("/bill")
public class GptBillController extends BaseController {
    @Resource
    private IGptBillService gptBillService;

    /**
     * 查询GPT账单列表
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(GPTBillDto gptBillDto) {
        startPage();
        List<GptBillVo> list = gptBillService.selectGptBillList(gptBillDto);
        return getDataTable(list);
    }

    /**
     * 导出GPT账单列表
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:export')")
    @Log(title = "GPT账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GPTBillDto gptBillDto) {
        List<GptBillVo> list = gptBillService.selectGptBillList(gptBillDto);
        ExcelUtil<GptBillVo> util = new ExcelUtil<GptBillVo>(GptBillVo.class);
        util.exportExcel(response, list, "GPT账单数据");
    }

    /**
     * 获取GPT账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(gptBillService.selectGptBillById(id));
    }

    /**
     * 新增GPT账单
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:add')")
    @Log(title = "GPT账单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelTokenConsumptionOverview modelTokenConsumptionOverview) {
        return toAjax(gptBillService.insertGptBill(modelTokenConsumptionOverview));
    }

    /**
     * 修改GPT账单
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:edit')")
    @Log(title = "GPT账单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GptBill gptBill) {
        return toAjax(gptBillService.updateGptBill(gptBill));
    }

    /**
     * 删除GPT账单
     */
    @PreAuthorize("@ss.hasPermi('bill:bill:remove')")
    @Log(title = "GPT账单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(gptBillService.deleteGptBillByIds(ids));
    }
}
