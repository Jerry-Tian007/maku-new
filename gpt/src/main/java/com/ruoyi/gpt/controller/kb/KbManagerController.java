package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerInsertDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerPageDto;
import com.ruoyi.gpt.model.dto.kb_manager.KbManagerUpdateDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationSelectVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerInfoVo;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerPageVo;
import com.ruoyi.gpt.service.IKbManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库管理
 */
@Slf4j
@RestController
@RequestMapping("/kb/manager")
public class KbManagerController extends BaseController {

    @Autowired
    private IKbManagerService iKbManagerService;

    /**
     * 新增知识库数据
     *
     * @param kbManagerInsertDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:add')")
    @Log(title = "知识库管理表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Integer> add(@RequestBody KbManagerInsertDto kbManagerInsertDto) {
        return R.success(iKbManagerService.insertKbManager(kbManagerInsertDto));
    }

    /**
     * 修改知识库数据
     *
     * @param kbManagerUpdateDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:edit')")
    @Log(title = "知识库管理表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<Integer> edit(@RequestBody KbManagerUpdateDto kbManagerUpdateDto) {
        return R.success(iKbManagerService.updateKbManager(kbManagerUpdateDto));
    }

    /**
     * 根据id删除知识库数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:remove')")
    @Log(title = "知识库管理表", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{id}")
    public R<Integer> remove(@PathVariable("id") Long id) {
        return R.success(iKbManagerService.deleteById(id));
    }


    /**
     * 根据id批量删除知识库数据
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:remove')")
    @Log(title = "知识库管理表", businessType = BusinessType.DELETE)
    @PostMapping("/removeBath")
    public R<Integer> removeBath(@RequestBody List<Long> ids) {
        return R.success(iKbManagerService.removeBath(ids));
    }

    /**
     * 根据id查询知识库数据详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:query')")
    @GetMapping("/{id}")
    public R<KbManagerInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.success(iKbManagerService.selectInfoById(id));
    }

    /**
     * 知识库数据列表查询
     *
     * @param kbManagerPageDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:kbmanager:list')")
    @PostMapping("/list")
    public PageDataInfo<KbManagerPageVo> list(@RequestBody KbManagerPageDto kbManagerPageDto) {
        startPage();
        List<KbManagerPageVo> list = iKbManagerService.list(kbManagerPageDto);
        return getPageData(list);
    }
}
