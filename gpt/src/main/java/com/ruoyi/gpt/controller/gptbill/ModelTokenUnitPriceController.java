package com.ruoyi.gpt.controller.gptbill;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.ModelTokenUnitPrice;
import com.ruoyi.gpt.service.IModelTokenUnitPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 模型单位token价格Controller
 *
 * @author yecao
 * @date 2023-04-28
 */
@RestController
@Slf4j
@RequestMapping("/gptbill/modeltokenunitprice")
public class ModelTokenUnitPriceController extends BaseController {

    @Resource
    private IModelTokenUnitPriceService modelTokenUnitPriceService;

    /**
     * 查询模型单位token价格列表
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:list')")
    @GetMapping("/list")
    public TableDataInfo list(ModelTokenUnitPrice modelTokenUnitPrice) {
        startPage();
        List<ModelTokenUnitPrice> list = modelTokenUnitPriceService.selectModelTokenUnitPriceList(modelTokenUnitPrice);
        return getDataTable(list);
    }

    /**
     * 导出模型单位token价格列表
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:export')")
    @Log(title = "模型单位token价格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ModelTokenUnitPrice modelTokenUnitPrice) {
        List<ModelTokenUnitPrice> list = modelTokenUnitPriceService.selectModelTokenUnitPriceList(modelTokenUnitPrice);
        ExcelUtil<ModelTokenUnitPrice> util = new ExcelUtil<ModelTokenUnitPrice>(ModelTokenUnitPrice.class);
        util.exportExcel(response, list, "模型单位token价格数据");
    }

    /**
     * 获取模型单位token价格详细信息
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(modelTokenUnitPriceService.selectModelTokenUnitPriceById(id));
    }

    /**
     * 新增模型单位token价格
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:add')")
    @Log(title = "模型单位token价格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelTokenUnitPrice modelTokenUnitPrice) {
        return toAjax(modelTokenUnitPriceService.insertModelTokenUnitPrice(modelTokenUnitPrice));
    }

    /**
     * 修改模型单位token价格
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:edit')")
    @Log(title = "模型单位token价格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ModelTokenUnitPrice modelTokenUnitPrice) {
        return toAjax(modelTokenUnitPriceService.updateModelTokenUnitPrice(modelTokenUnitPrice));
    }

    /**
     * 删除模型单位token价格
     */
    @PreAuthorize("@ss.hasPermi('gptbill:modeltokenunitprice:remove')")
    @Log(title = "模型单位token价格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(modelTokenUnitPriceService.deleteModelTokenUnitPriceByIds(ids));
    }
}
