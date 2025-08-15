package com.ruoyi.gpt.controller.chat;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.domain.QAToneStyle;
import com.ruoyi.gpt.service.IQAToneStyleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 语气风格Controller
 * 
 * @author Eric
 * @date 2023-08-21
 */
@RestController
@RequestMapping("/gpt/tonestyle")
public class QAToneStyleController extends BaseController
{
    @Autowired
    private IQAToneStyleService qAToneStyleService;

    /**
     * 查询语气风格列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:list')")
    @GetMapping("/list")
    public TableDataInfo list(QAToneStyle qAToneStyle)
    {
        startPage();
        List<QAToneStyle> list = qAToneStyleService.selectQAToneStyleList(qAToneStyle);
        return getDataTable(list);
    }

    /**
     * 查询语气风格列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:list')")
    @GetMapping("/detail")
    public AjaxResult getDetail(QAToneStyle qAToneStyle)
    {

        QAToneStyle qaToneStyle = qAToneStyleService.getQAToneStyleDetail(qAToneStyle);

        return AjaxResult.success(qaToneStyle);

    }

    /**
     * 导出语气风格列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:export')")
    @Log(title = "语气风格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QAToneStyle qAToneStyle)
    {
        List<QAToneStyle> list = qAToneStyleService.selectQAToneStyleList(qAToneStyle);
        ExcelUtil<QAToneStyle> util = new ExcelUtil<QAToneStyle>(QAToneStyle.class);
        util.exportExcel(response, list, "语气风格数据");
    }

    /**
     * 获取语气风格详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qAToneStyleService.selectQAToneStyleById(id));
    }

    /**
     * 新增语气风格
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:add')")
    @Log(title = "语气风格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QAToneStyle qAToneStyle)
    {
        return toAjax(qAToneStyleService.insertQAToneStyle(qAToneStyle));
    }

    /**
     * 修改语气风格
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:edit')")
    @Log(title = "语气风格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QAToneStyle qAToneStyle)
    {
        return toAjax(qAToneStyleService.updateQAToneStyle(qAToneStyle));
    }

    /**
     * 删除语气风格
     */
    @PreAuthorize("@ss.hasPermi('gpt:tonestyle:remove')")
    @Log(title = "语气风格", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qAToneStyleService.deleteQAToneStyleByIds(ids));
    }
}
