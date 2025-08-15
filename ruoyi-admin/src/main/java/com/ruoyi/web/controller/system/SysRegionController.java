package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;


import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRegion;
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
import com.ruoyi.system.service.ISysRegionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * regionController
 * 
 * @author lijw
 * @date 2023-12-02
 */
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends BaseController
{
    @Autowired
    private ISysRegionService sysRegionService;

    /**
     * 查询region列表
     */
    @PreAuthorize("@ss.hasPermi('system:region:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRegion sysRegion)
    {
        startPage();
        List<SysRegion> list = sysRegionService.selectSysRegionList(sysRegion);
        return getDataTable(list);
    }

    /**
     * 导出region列表
     */
    @PreAuthorize("@ss.hasPermi('system:region:export')")
    @Log(title = "region", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRegion sysRegion)
    {
        List<SysRegion> list = sysRegionService.selectSysRegionList(sysRegion);
        ExcelUtil<SysRegion> util = new ExcelUtil<SysRegion>(SysRegion.class);
        util.exportExcel(response, list, "region数据");
    }

    /**
     * 通过regionId获取region详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:region:query')")
    @GetMapping(value = "/{regionId}")
    public AjaxResult getInfo(@PathVariable("regionId") Long regionId)
    {
        return success(sysRegionService.selectSysRegionByRegionId(regionId));
    }


    /**
     * 通过regionName 获取region详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:region:query')")
    @GetMapping(value = "/byRegionName")
    public AjaxResult getInfo(String regionName)
    {
        return success(sysRegionService.selectSysRegionByRegion(regionName));
    }

    @PreAuthorize("@ss.hasPermi('system:region:list')")
    @GetMapping("/regionTree")
    public AjaxResult regionTree(SysRegion region)
    {
        return success(sysRegionService.selectRegionTreeList(region));
    }

    /**
     * 新增region
     */
    @PreAuthorize("@ss.hasPermi('system:region:add')")
    @Log(title = "region", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysRegion sysRegion)
    {
        return toAjax(sysRegionService.insertSysRegion(sysRegion));
    }

    /**
     * 修改region
     */
    @PreAuthorize("@ss.hasPermi('system:region:edit')")
    @Log(title = "region", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysRegion sysRegion)
    {
        return toAjax(sysRegionService.updateSysRegion(sysRegion));
    }

    /**
     * 删除region
     */
    @PreAuthorize("@ss.hasPermi('system:region:remove')")
    @Log(title = "region", businessType = BusinessType.DELETE)
	@DeleteMapping("/{regionIds}")
    public AjaxResult remove(@PathVariable Long[] regionIds)
    {
        return toAjax(sysRegionService.deleteSysRegionByRegionIds(regionIds));
    }
}
