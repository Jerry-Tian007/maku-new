package com.ruoyi.gpt.controller.graph;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.domain.GraphInfo;
import com.ruoyi.gpt.model.dto.graph.GraphAuthDto;
import com.ruoyi.gpt.model.dto.graph.GraphInfoListDto;
import com.ruoyi.gpt.model.vo.GraphRoleVo;
import com.ruoyi.gpt.service.IGraphInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 知识图谱信息Controller
 *
 * @author yecao
 * @date 2025-04-09
 */
@RestController
@RequestMapping("/gpt/graphInfo")
public class GraphInfoController extends BaseController {

    @Resource
    private IGraphInfoService graphInfoService;

    /**
     * 查询知识图谱信息列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:graphInfo:list')")
    @GetMapping("/list")
    public PageDataInfo<GraphInfo> list(GraphInfoListDto graphInfoDto) {
        startPage();
        List<GraphInfo> list = graphInfoService.selectGraphInfoListForPlatform(graphInfoDto);
        return getPageData(list);
    }

    /**
     * 同步知识图谱数据
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('gpt:graphInfo:sync')")
    @GetMapping("/sync")
    public AjaxResult sync() {
        int i = graphInfoService.syncGraphInfo();
        if (i == 0) {
            return AjaxResult.success();
        }

        return AjaxResult.error();
    }

    @PreAuthorize("@ss.hasPermi('gpt:graphInfo:auth')")
    @PostMapping("/auth")
    public AjaxResult auth(@RequestBody GraphAuthDto graphAuthDto) {
        int i = graphInfoService.auth(graphAuthDto);
        if (i == 1) {
            return AjaxResult.success();
        }

        return AjaxResult.error();
    }

    @PreAuthorize("@ss.hasPermi('gpt:graphInfo:graphRole')")
    @GetMapping("/graphRole")
    public AjaxResult graphRole(Long roleId) {
        List<GraphRoleVo> graphRoleVos = graphInfoService.graphRole(roleId);
        return AjaxResult.success(graphRoleVos);
    }

    @PreAuthorize("@ss.hasPermi('gpt:graphInfo:userGraphRole')")
    @GetMapping("/userGraphRole")
    public AjaxResult userGraphRole() {
        List<GraphInfo> graphInfos = graphInfoService.userGraphRole(SecurityUtils.getUserId());
        return AjaxResult.success(graphInfos);
    }

}
