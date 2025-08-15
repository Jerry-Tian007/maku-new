package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.dto.user_kb_history.UserKbHistoryInsertDto;
import com.ruoyi.gpt.model.vo.user_kb_history.UserKbHistoryPageVo;
import com.ruoyi.gpt.service.IUserKbHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户历史记录Controller
 */
@RestController
@RequestMapping("/kb/user-kb-history")
public class UserKbHistoryController extends BaseController {
    @Autowired
    private IUserKbHistoryService iUserKbHistoryService;

    /**
     * 分页查询用户历史记录列表
     */
    @PreAuthorize("@ss.hasPermi('kb:userkbhistory:list')")
    @GetMapping("/page-list")
    public PageDataInfo<UserKbHistoryPageVo> pageList() {
        startPage();
        List<UserKbHistoryPageVo> list = iUserKbHistoryService.selectUserKbHistoryPageList(SecurityUtils.getUsername());
        return getPageData(list);
    }

    /**
     * 查询用户历史记录列表
     */
    @PreAuthorize("@ss.hasPermi('kb:userkbhistory:list')")
    @GetMapping("/list")
    public R<List<UserKbHistoryPageVo>> list() {
        List<UserKbHistoryPageVo> list = iUserKbHistoryService.selectUserKbHistoryPageList(SecurityUtils.getUsername());
        return R.success(list);
    }

    /**
     * 根据id删除用户历史记录
     */
    @PreAuthorize("@ss.hasPermi('kb:userkbhistory:remove')")
    @GetMapping("/remove/{id}")
    public R<Integer> removeById(@PathVariable("id") Long id) {
        return R.success(iUserKbHistoryService.deleteUserKbHistoryById(id));
    }

    /**
     * 根据id批量删除用户历史记录
     */
    @PreAuthorize("@ss.hasPermi('kb:userkbhistory:remove')")
    @PostMapping("/batch-remove")
    public R<Integer> batchRemoveById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.error(ReturnMessageEnum.please_select_the_data_to_delete.toString());
        }
        return R.success(iUserKbHistoryService.batchDeleteUserKbHistoryById(ids));
    }

    /**
     * 新增用户历史记录信息
     */
    @PreAuthorize("@ss.hasPermi('kb:userkbhistory:add')")
    @PostMapping("/add")
    public R<Integer> add(@Validated @RequestBody UserKbHistoryInsertDto userKbHistoryInsertDto) {
        return R.success(iUserKbHistoryService.insertUserKbHistory(userKbHistoryInsertDto));
    }
}
