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

import com.ruoyi.gpt.service.IUserModelParameterService;
import com.ruoyi.gpt.model.dto.user_model_parameter.*;
import com.ruoyi.gpt.model.vo.user_model_parameter.*;

/**
 * 
 *
 * @author Eric
 */
@RestController
@RequestMapping("/user-model-parameter")
public class UserModelParameterController  extends BaseController {
    @Autowired
    private IUserModelParameterService iUserModelParameterService;

    /**
     * 创建
     *
     * @param userModelParameterAddDto
     */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:insert')")
    @Log(title = "创建", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public R<Integer> add(@RequestBody @Validated UserModelParameterAddDto userModelParameterAddDto) {
        return R.success(iUserModelParameterService.add(userModelParameterAddDto));
    }

    /**
     * 更新
     *
     * @param userModelParameterUpdateDto
     */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:update')")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public R<Integer> update(@RequestBody @Validated UserModelParameterUpdateDto userModelParameterUpdateDto) {
        return R.success(iUserModelParameterService.update(userModelParameterUpdateDto));
    }

    /**
     * 删除
     *
     * @param id
     */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:delete')")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @GetMapping(value = "/delete/{id}")
    public R<Integer> delete(@PathVariable("id") Long id) {
        return R.success(iUserModelParameterService.delete(id));
    }

    /**
     * 查询分页信息
     *
     * @param userModelParameterPageQryDto
     */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:list')")
    @PostMapping(value = "/page-list")
    public PageDataInfo<UserModelParameterPageVO> pageList(@RequestBody UserModelParameterPageQryDto userModelParameterPageQryDto) {
        startPage();
        return getPageData(iUserModelParameterService.pageList(userModelParameterPageQryDto));
    }

    /**
    * 查询详情
     *
     * @param id
    */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:query')")
    @GetMapping(value = "/detail/{id}")
    public R<UserModelParameterInfoVO> getById(@PathVariable("id") Long id) {
        return R.success(iUserModelParameterService.getById(id));
    }
    /**
     * 获取模型参数
     *
     * @param userModelParameterGetDto
     */
    @PreAuthorize("@ss.hasPermi('user-model-parameter:query')")
    @PostMapping(value = "/get/parameter")
    public R<UserModelParameterPageVO> getParameter(@RequestBody @Validated UserModelParameterGetDto userModelParameterGetDto) {
        return R.success(iUserModelParameterService.getParameter(userModelParameterGetDto));
    }

}
