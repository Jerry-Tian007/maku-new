package com.ruoyi.gpt.controller.kb;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.gpt.model.vo.kb_error_info.KbErrorInfoVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.domain.KbErrorInfo;
import com.ruoyi.gpt.service.IKbErrorInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 私域错误日志Controller
 * 
 * @author Eric
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/kb/error-info")
public class KbErrorInfoController extends BaseController
{
    @Autowired
    private IKbErrorInfoService kbErrorInfoService;

    /**
     * 查询私域错误日志列表
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:list')")
    @PostMapping("/list")
    public PageDataInfo<KbErrorInfo> list(@RequestBody KbErrorInfo kbErrorInfo)
    {
        startPage();
        List<KbErrorInfo> list = kbErrorInfoService.selectKbErrorInfoList(kbErrorInfo);
        return getPageData(list);
    }

    /**
     * 导出私域错误日志列表
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:export')")
    @Log(title = "私域错误日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody HttpServletResponse response, @RequestBody KbErrorInfo kbErrorInfo)
    {
        List<KbErrorInfo> list = kbErrorInfoService.selectKbErrorInfoList(kbErrorInfo);
        ExcelUtil<KbErrorInfo> util = new ExcelUtil<KbErrorInfo>(KbErrorInfo.class);
        util.exportExcel(response, list, ReturnMessageEnum.private_domain_error_log_data.toString());
    }

    /**
     * 获取私域错误日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:query')")
    @GetMapping(value = "/{id}")
    public R<KbErrorInfoVo> getInfo(@PathVariable("id") Long id)
    {
        return R.success(kbErrorInfoService.selectKbErrorInfoById(id));
    }

    /**
     * 新增私域错误日志
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:add')")
    @Log(title = "私域错误日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Integer> add(@RequestBody KbErrorInfo kbErrorInfo)
    {
        return R.success(kbErrorInfoService.insertKbErrorInfo(kbErrorInfo));
    }

    /**
     * 修改私域错误日志
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:edit')")
    @Log(title = "私域错误日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<Integer> edit(@RequestBody KbErrorInfo kbErrorInfo)
    {
        return R.success(kbErrorInfoService.updateKbErrorInfo(kbErrorInfo));
    }

    /**
     * 批量删除私域错误日志
     */
    @PreAuthorize("@ss.hasPermi('kb:errorinfo:remove')")
    @Log(title = "私域错误日志", businessType = BusinessType.DELETE)
	@PostMapping("/remove-batch/{ids}")
    public R<Integer> removeBatch(@PathVariable("ids") Long[] ids)
    {
        return R.success(kbErrorInfoService.deleteKbErrorInfoByIds(ids));
    }
}
