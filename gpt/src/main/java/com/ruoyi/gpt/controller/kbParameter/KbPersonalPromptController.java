package com.ruoyi.gpt.controller.kbParameter;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptAddDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptPageQryDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptReleaseDto;
import com.ruoyi.gpt.model.dto.personal_prompt.PersonalPromptUpdateDto;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptInfoVO;
import com.ruoyi.gpt.model.vo.personal_prompt.PersonalPromptPageVO;
import com.ruoyi.gpt.model.vo.personal_prompt.QueryPromptPageVO;
import com.ruoyi.gpt.service.IKbPersonalPromptService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 私域知识库个人提示词
 *
 * @author Eric
 */
@RestController
@RequestMapping("/kb/personal-prompt")
public class KbPersonalPromptController extends BaseController {

    @Resource
    private IKbPersonalPromptService iKbPersonalPromptService;

    /**
     * 创建
     *
     * @param personalPromptAddDto
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:insert')")
    @Log(title = "创建", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public R<Integer> add(@RequestBody @Validated PersonalPromptAddDto personalPromptAddDto) {
        return R.success(iKbPersonalPromptService.add(personalPromptAddDto));
    }

    @PreAuthorize("@ss.hasPermi('personal-prompt:release')")
    @Log(title = "发布状态修改", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/release")
    public R<Integer> release(@RequestBody @Validated PersonalPromptReleaseDto personalPromptReleaseDto) {
        return R.success(iKbPersonalPromptService.release(personalPromptReleaseDto));
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
        return R.success(iKbPersonalPromptService.update(personalPromptUpdateDto));
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
        return R.success(iKbPersonalPromptService.delete(id));
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
        return getPageData(iKbPersonalPromptService.pageList(personalPromptPageQryDto));
    }

    /**
     * 查询详情
     *
     * @param id
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:query')")
    @GetMapping(value = "/detail/{id}")
    public R<PersonalPromptInfoVO> getById(@PathVariable("id") Long id) {
        return R.success(iKbPersonalPromptService.getById(id));
    }

    /**
     * 查询知识库可选的提示词
     */
    @PreAuthorize("@ss.hasPermi('personal-prompt:kbManagerSelectPromptList')")
    @GetMapping(value = "/kbManagerSelectPromptList")
    public R<List<PersonalPromptInfoVO>> kbManagerSelectPromptList(@RequestParam(value = "kbManagerId", required = false) Long kbManagerId) {
        return R.success(iKbPersonalPromptService.kbManagerSelectPromptList(kbManagerId));
    }

    /**
     * 查询所有query_prompt_type不为空的记录
     *
     * @param personalPromptPageQryDto
     */
    @PreAuthorize("@ss.hasPermi('kb:promptManager:list')")
    @PostMapping(value = "/query-prompt-list")
    public PageDataInfo<QueryPromptPageVO> queryPromptList(@RequestBody PersonalPromptPageQryDto personalPromptPageQryDto) {
        startPage();
        return getPageData(iKbPersonalPromptService.queryPromptList(personalPromptPageQryDto));
    }
}
