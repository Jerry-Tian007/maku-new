package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.dto.kb_file.*;
import com.ruoyi.gpt.model.vo.kb_file.KbFileExportVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFileInfoVo;
import com.ruoyi.gpt.model.vo.kb_file.KbFilePageVo;
import com.ruoyi.gpt.service.IKbFileService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 知识库文件Controller
 *
 * @author Eric
 * @date 2023-05-16
 */
@RestController
@RequestMapping("/kb/file")
@Slf4j
public class KbFileController extends BaseController {
    @Autowired
    private IKbFileService kbFileService;

    /**
     * 文件详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:query')")
    @GetMapping("/{id}")
    public R<KbFileInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.success(kbFileService.selectKbFileById(id));
    }

    /**
     * 文件列表
     *
     * @param pageDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:list')")
    @PostMapping("/list")
    public PageDataInfo<KbFilePageVo> list(@RequestBody KbFilePageDto pageDto) {
        startPage();
        List<KbFilePageVo> list = kbFileService.selectKbFileList(pageDto);
        return getPageData(list);
    }

    /**
     * 导出
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:export')")
    @Log(title = "知识库文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody KbFilePageDto pageDto, HttpServletResponse response) {
        List<KbFileExportVo> list = kbFileService.selectExportKbFileList(pageDto);
        ExcelUtil<KbFileExportVo> util = new ExcelUtil<>(KbFileExportVo.class);
        util.exportExcel(response, list, "知识库文件");
    }
    /**
     * 修改文件
     *0
     * @param updateDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:edit')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<Integer> edit(@RequestBody KbFileUpdateDto updateDto) {
        return R.success(kbFileService.updateKbFile(updateDto));
    }

    /**
     * 修改文件书籍级别
     * @param kbFileBookLevelUpdateDto
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:edit')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/editBookLevel")
    public R<Integer> editBookLevel(@RequestBody KbFileBookLevelUpdateDto kbFileBookLevelUpdateDto) {
        return R.success(kbFileService.updateBookLevel(kbFileBookLevelUpdateDto));
    }

    /**
     * 批量修改文件
     *
     * @param updateBatchDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:edit')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit-batch")
    public R<Integer> editBatch(@RequestBody KbFileUpdateBatchDto updateBatchDto) {
        return R.success(kbFileService.updateBatchKbFile(updateBatchDto));
    }

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:remove')")
    @Log(title = "知识库文件", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{id}")
    public R<Integer> remove(@PathVariable("id") Long id) {
        return R.success(kbFileService.sendDeleteMessage(id));
    }

    /**
     * 取消训练
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:cancel')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel-training/{id}")
    public R<Integer> cancelTraining(@PathVariable("id") Long id) {
        return R.success(kbFileService.cancelTrainingById(id));
    }

    /**
     * 文件训练
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:train')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/train")
    public R<Integer> train(@RequestBody List<Long> ids) {
        return R.success(kbFileService.train(ids));
    }

    /**
     * 文件更新训练
     *
     * @param
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:train')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/updateTrain")
    public R<Integer> updateTrain(@RequestBody KbFileUpdateBatchDto updateBatchDto) {
        return R.success(kbFileService.updateTrain(updateBatchDto));
    }

    /**
     * 批量修改文件启用禁用状态
     *
     * @param batchEditKbFileStatus
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbfile:edit')")
    @Log(title = "知识库文件", businessType = BusinessType.UPDATE)
    @PostMapping("/batchEditStatus")
    public R<Integer> batchEditStatus(@RequestBody BatchEditKbFileStatus batchEditKbFileStatus) {
        return R.success(kbFileService.batchEditStatus(batchEditKbFileStatus));
    }
}
