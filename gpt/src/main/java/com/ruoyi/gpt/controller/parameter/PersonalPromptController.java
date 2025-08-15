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

import com.ruoyi.gpt.service.IPersonalPromptService;
import com.ruoyi.gpt.model.dto.personal_prompt.*;
import com.ruoyi.gpt.model.vo.personal_prompt.*;

/**
 * 个人提示词
 *
 * @author Eric
 */
@RestController
@RequestMapping("/personal-prompt")
public class PersonalPromptController  extends BaseController {
    @Autowired
    private IPersonalPromptService iPersonalPromptService;

    /**
     * 创建
     *
     * @param personalPromptAddDto
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:insert')")
    @Log(title = "创建", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public R<Integer> add(@RequestBody @Validated PersonalPromptAddDto personalPromptAddDto) {
        return R.success(iPersonalPromptService.add(personalPromptAddDto));
    }

    @PreAuthorize("@ss.hasPermi('personal-prompt:release')")
    @Log(title = "发布状态修改", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/release")
    public R<Integer> release(@RequestBody @Validated PersonalPromptReleaseDto personalPromptReleaseDto) {
        return R.success(iPersonalPromptService.release(personalPromptReleaseDto));
    }

    /**
     * 更新
     *
     * @param personalPromptUpdateDto
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:update')")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public R<Integer> update(@RequestBody @Validated PersonalPromptUpdateDto personalPromptUpdateDto) {
        return R.success(iPersonalPromptService.update(personalPromptUpdateDto));
    }

    /**
     * 删除
     *
     * @param id
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:delete')")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping(value = "/delete/{id}")
    public R<Integer> delete(@PathVariable("id") Long id) {
        return R.success(iPersonalPromptService.delete(id));
    }

    /**
     * 查询分页信息
     *
     * @param personalPromptPageQryDto
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:list')")
    @PostMapping(value = "/page-list")
    public PageDataInfo<PersonalPromptPageVO> pageList(@RequestBody PersonalPromptPageQryDto personalPromptPageQryDto) {
        startPage();
        return getPageData(iPersonalPromptService.pageList(personalPromptPageQryDto));
    }

    /**
    * 查询详情
     *
     * @param id
    */
    @PreAuthorize("@ss.hasPermi('personal-prompt:query')")
    @GetMapping(value = "/detail/{id}")
    public R<PersonalPromptInfoVO> getById(@PathVariable("id") Long id) {
        return R.success(iPersonalPromptService.getById(id));
    }
}
