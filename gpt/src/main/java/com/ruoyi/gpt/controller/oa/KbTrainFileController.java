package com.ruoyi.gpt.controller.oa;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.gpt.model.domain.KbTrainFile;
import com.ruoyi.gpt.service.IKbTrainFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 知识库训练文件Controller
 * 
 * @author Eric
 * @date 2023-09-19
 */
@RestController
@RequestMapping("/gpt/kbtrainfile")
public class KbTrainFileController extends BaseController
{
    @Autowired
    private IKbTrainFileService kbTrainFileService;

    /**
     * 查询知识库训练文件列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:list')")
    @GetMapping("/list")
    public TableDataInfo list(KbTrainFile kbTrainFile)
    {
        startPage();
        List<KbTrainFile> list = kbTrainFileService.selectKbTrainFileList(kbTrainFile);
        return getDataTable(list);
    }

    /**
     * 导出知识库训练文件列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:export')")
    @Log(title = "知识库训练文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KbTrainFile kbTrainFile)
    {
        List<KbTrainFile> list = kbTrainFileService.selectKbTrainFileList(kbTrainFile);
        ExcelUtil<KbTrainFile> util = new ExcelUtil<KbTrainFile>(KbTrainFile.class);
        util.exportExcel(response, list, "知识库训练文件数据");
    }

    /**
     * 获取知识库训练文件详细信息
     */
//    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") String fileId)
    {
        return success(kbTrainFileService.selectKbTrainFileByFileId(fileId));
    }

    /**
     * 新增知识库训练文件
     */
    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:add')")
    @Log(title = "知识库训练文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KbTrainFile kbTrainFile)
    {
        return toAjax(kbTrainFileService.insertKbTrainFile(kbTrainFile));
    }

    /**
     * 修改知识库训练文件
     */
//    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:edit')")
//    @Log(title = "知识库训练文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KbTrainFile kbTrainFile)
    {
        return toAjax(kbTrainFileService.updateKbTrainFile(kbTrainFile));
    }

    /**
     * 删除知识库训练文件
     */
    @PreAuthorize("@ss.hasPermi('gpt:kbtrainfile:remove')")
    @Log(title = "知识库训练文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable String[] fileIds)
    {
        return toAjax(kbTrainFileService.deleteKbTrainFileByFileIds(fileIds));
    }
}
