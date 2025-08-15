package com.ruoyi.gpt.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.azure.core.annotation.Get;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.vo.DeptKbConfigVo;
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
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.service.IDeptKbConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 部门知识库配置Controller
 *
 * @author Eric
 * @date 2023-05-18
 */
@RestController
@RequestMapping("/gpt/deptkbconfig")
public class DeptKbConfigController extends BaseController
{
    @Autowired
    private IDeptKbConfigService deptKbConfigService;
    @Autowired
    private RedisCache redisCache;
    /**
     * 查询部门知识库配置列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeptKbConfig deptKbConfig)
    {
        startPage();
        List<DeptKbConfig> list = deptKbConfigService.selectDeptKbConfigList(deptKbConfig);
        return getDataTable(list);
    }

    /**
     * 导出部门知识库配置列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:export')")
    @Log(title = "部门知识库配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeptKbConfig deptKbConfig)
    {
        List<DeptKbConfig> list = deptKbConfigService.selectDeptKbConfigList(deptKbConfig);
        ExcelUtil<DeptKbConfig> util = new ExcelUtil<DeptKbConfig>(DeptKbConfig.class);
        util.exportExcel(response, list, "部门知识库配置数据");
    }

    /**
     * 获取部门知识库配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(deptKbConfigService.selectDeptKbConfigById(id));
    }

    /**
     * 新增部门知识库配置
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:add')")
    @Log(title = "部门知识库配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeptKbConfig deptKbConfig)
    {
        return toAjax(deptKbConfigService.insertDeptKbConfig(deptKbConfig));
    }

    /**
     * 修改部门知识库配置
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:edit')")
    @Log(title = "部门知识库配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeptKbConfig deptKbConfig)
    {
        return toAjax(deptKbConfigService.updateDeptKbConfig(deptKbConfig));
    }

    /**
     * 删除部门知识库配置
     */
    @PreAuthorize("@ss.hasPermi('gpt:deptkbconfig:remove')")
    @Log(title = "部门知识库配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deptKbConfigService.deleteDeptKbConfigByIds(ids));
    }
    @PostMapping("/getKbConfig")
    public AjaxResult getDeptKbConfig(@RequestBody DeptKbConfigVo deptKbConfigVo){
        DeptKbConfig deptKbConfig = deptKbConfigService.getDeptKbConfigByDeptIdAndUserId(deptKbConfigVo);
        String key = deptKbConfigVo.getDeptId()+":"+deptKbConfigVo.getUserId()+":"+"kbconfig";
        redisCache.setCacheObject(key,deptKbConfig);
        return AjaxResult.success(deptKbConfig);
    }

    @GetMapping("/syncKbConfig")
    public AjaxResult syncKbConfig(DeptKbConfig deptKbConfig){
        List<DeptKbConfig> deptKbConfigs = deptKbConfigService.selectDeptKbConfigList(deptKbConfig);
        for (DeptKbConfig item : deptKbConfigs){
            DeptKbConfigVo deptKbConfigVo = new DeptKbConfigVo();
            deptKbConfigVo.setDeptId(item.getDeptId());
            deptKbConfigVo.setUserId(item.getUserId());
            this.getDeptKbConfig(deptKbConfigVo);
        }
        return AjaxResult.success();
    }
}
