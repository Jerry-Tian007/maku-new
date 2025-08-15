package com.ruoyi.gpt.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.DeptAdInfo;
import com.ruoyi.gpt.service.IDeptAdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部门ad域信息Controller
 * 
 * @author Eric
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/gpt/deptAdInfo")
public class DeptAdInfoController extends BaseController
{
    @Autowired
    private IDeptAdInfoService deptAdInfoService;

    /**
     * 查询部门ad域信息列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeptAdInfo deptAdInfo)
    {
        startPage();
        List<DeptAdInfo> list = deptAdInfoService.selectDeptAdInfoList(deptAdInfo);
        return getDataTable(list);
    }

    /**
     * 导出部门ad域信息列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:export')")
    @Log(title = "部门ad域信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeptAdInfo deptAdInfo)
    {
        List<DeptAdInfo> list = deptAdInfoService.selectDeptAdInfoList(deptAdInfo);
        ExcelUtil<DeptAdInfo> util = new ExcelUtil<DeptAdInfo>(DeptAdInfo.class);
        util.exportExcel(response, list, "部门ad域信息数据");
    }

    /**
     * 获取部门ad域信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(deptAdInfoService.selectDeptAdInfoById(id));
    }

    /**
     * 新增部门ad域信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:add')")
    @Log(title = "部门ad域信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeptAdInfo deptAdInfo)
    {
        return toAjax(deptAdInfoService.insertDeptAdInfo(deptAdInfo));
    }

    /**
     * 修改部门ad域信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:edit')")
    @Log(title = "部门ad域信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeptAdInfo deptAdInfo)
    {
        return toAjax(deptAdInfoService.updateDeptAdInfo(deptAdInfo));
    }

    /**
     * 删除部门ad域信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptAdInfo:remove')")
    @Log(title = "部门ad域信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deptAdInfoService.deleteDeptAdInfoByIds(ids));
    }
}
