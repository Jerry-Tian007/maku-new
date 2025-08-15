package com.ruoyi.gpt.controller.parameter;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import com.ruoyi.gpt.service.IPublicPromptService;
import com.ruoyi.gpt.model.dto.public_prompt.*;
import com.ruoyi.gpt.model.vo.public_prompt.*;

/**
 * 社区提示词
 *
 * @author Eric
 */
@RestController
@RequestMapping("/public-prompt")
public class PublicPromptController  extends BaseController {
    @Autowired
    private IPublicPromptService iPublicPromptService;

    /**
     * 创建
     *
     * @param publicPromptAddDto
     */
    @PreAuthorize("@ss.hasPermi('public-prompt:insert')")
    @Log(title = "创建", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public R<Integer> add(@RequestBody @Validated PublicPromptAddDto publicPromptAddDto) {
        return R.success(iPublicPromptService.add(publicPromptAddDto));
    }

    @PreAuthorize("@ss.hasPermi('public-prompt:save')")
    @Log(title = "保存", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public R<Integer> save(@RequestBody @Validated PublicPromptSaveDto publicPromptSaveDto) {
        return R.success(iPublicPromptService.save(publicPromptSaveDto));
    }

    /**
     * 更新
     *
     * @param publicPromptUpdateDto
     */
    @PreAuthorize("@ss.hasPermi('public-prompt:update')")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public R<Integer> update(@RequestBody @Validated PublicPromptUpdateDto publicPromptUpdateDto) {
        return R.success(iPublicPromptService.update(publicPromptUpdateDto));
    }

    /**
     * 删除
     *
     * @param id
     */
    @PreAuthorize("@ss.hasPermi('public-prompt:delete')")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping(value = "/delete/{id}")
    public R<Integer> delete(@PathVariable("id") Long id) {
        return R.success(iPublicPromptService.delete(id));
    }

    /**
     * 查询分页信息
     *
     * @param publicPromptPageQryDto
     */
    @PreAuthorize("@ss.hasPermi('public-prompt:list')")
    @PostMapping(value = "/page-list")
    public PageDataInfo<PublicPromptPageVO> pageList(@RequestBody PublicPromptPageQryDto publicPromptPageQryDto) {
        startPage();
        return getPageData(iPublicPromptService.pageList(publicPromptPageQryDto));
    }

    /**
    * 查询详情
     *
     * @param id
    */
    @PreAuthorize("@ss.hasPermi('public-prompt:query')")
    @GetMapping(value = "/detail/{id}")
    public R<PublicPromptInfoVO> getById(@PathVariable("id") Long id) {
        return R.success(iPublicPromptService.getById(id));
    }
}
