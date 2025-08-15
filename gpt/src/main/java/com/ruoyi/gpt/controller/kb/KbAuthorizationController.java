package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationInsertDto;
import com.ruoyi.gpt.model.dto.kb_authorization.KbAuthorizationPageDto;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationPageVo;
import com.ruoyi.gpt.model.vo.kb_authorization.KbAuthorizationSelectVo;
import com.ruoyi.gpt.service.IKbAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 知识库授权管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/kb/authorization")
public class KbAuthorizationController extends BaseController {

    @Autowired
    private IKbAuthorizationService kbAuthorizationService;

    /**
     * 知识库授权
     *
     * @param kbAuthorizationInsertDto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:authorization:add')")
    @Log(title = "知识库授权表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Integer> add(@RequestBody KbAuthorizationInsertDto kbAuthorizationInsertDto) {
        return R.success(kbAuthorizationService.insertKbAuthorization(kbAuthorizationInsertDto));
    }

    /**
     * 已授权知识库列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:authorization:list')")
    @PostMapping("/list")
    public PageDataInfo<KbAuthorizationPageVo> list(@RequestBody KbAuthorizationPageDto kbAuthorizationPageDto) {
        startPage();
        List<KbAuthorizationPageVo> kbRoleManagerPageRel = kbAuthorizationService.list(kbAuthorizationPageDto);
        return getPageData(kbRoleManagerPageRel);
    }

    /**
     * 已授权知识库下拉框
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:authorization:query')")
    @PostMapping("/getSelect")
    public R<List<KbAuthorizationSelectVo>> getSelect() {
        return R.success(kbAuthorizationService.getSelect());
    }

    /**
     * 根据角色ID获取知识库集合
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('kb:authorization:query')")
    @GetMapping("/getKbListByRoleId/{roleId}")
    public R<Set<String>> getKbListByRoleId(@PathVariable("roleId") Long roleId) {
        return R.success(kbAuthorizationService.getKbListByRoleId(roleId));
    }
}
