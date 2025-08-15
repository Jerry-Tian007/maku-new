package com.ruoyi.gpt.controller.kb;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordUpdateDto;
import com.ruoyi.gpt.model.vo.kb_manager.KbManagerInfoVo;
import com.ruoyi.gpt.service.IKbManagerService;
import com.ruoyi.gpt.service.IKbQARecordService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 知识库问答记录Controller
 *
 * @author Eric
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/kb/qa-record")

public class KbQARecordController extends BaseController {
    @Resource
    private IKbQARecordService kbQARecordService;

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private IKbManagerService kbManagerService;

    /**
     * 查询知识库问答记录列表
     */
    @PreAuthorize("@ss.hasPermi('kb:kbqarecord:list')")
    @PostMapping("/list")
    public PageDataInfo<KbQARecordDto> list(@RequestBody KbQARecordDto kbQARecordDto) {
        startPage();
        List<KbQARecordDto> list = kbQARecordService.selectKbQARecordList(kbQARecordDto);
        List<CompletableFuture<KbQARecordDto>> collect = list.stream().map(item -> {
            return CompletableFuture.supplyAsync(() -> {
                String kbName = "";
                String kbId = item.getKbId();
                if (StringUtils.isNotEmpty(kbId)) {
                    for (String id : kbId.split(";")) {
                        KbManagerInfoVo kbManagerInfoVo = kbManagerService.selectInfoById(Long.parseLong(id));
                        if (StringUtils.isNotNull(kbManagerInfoVo)) {
                            kbName = kbName + kbManagerInfoVo.getKbName() + ",";
                        }
                    }
                }
                item.setKbName(kbName);
                return item;
            }, threadPoolTaskExecutor);
        }).collect(Collectors.toList());

        List<KbQARecordDto> join = CompletableFuture.allOf(collect.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    return collect.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());
                }).join();
        return getPageData(join);
    }

    /**
     * 导出知识库问答记录列表
     */
    @PreAuthorize("@ss.hasPermi('kb:kbqarecord:export')")
    @Log(title = "知识库问答记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody HttpServletResponse response, @RequestBody KbQARecordDto kbQARecordDto) {
        List<KbQARecordDto> list = kbQARecordService.selectKbQARecordList(kbQARecordDto);
        ExcelUtil<KbQARecordDto> util = new ExcelUtil<>(KbQARecordDto.class);
        util.exportExcel(response, list, ReturnMessageEnum.knowledge_base_question_and_answer_records_data.toString());
    }

    /**
     * 获取知识库问答记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('kb:kbqarecord:query')")
    @GetMapping(value = "/{id}")
    public R<KbQARecordDto> getInfo(@PathVariable("id") Long id) {
        return R.success(kbQARecordService.selectKbQARecordById(id));
    }

    /**
     * 通过sessionId查询指定的问答记录
     */
    @PreAuthorize("@ss.hasPermi('kb:kbqarecord:query')")
    @GetMapping(value = "/get-history-by-session-id/{sessionId}")
    public R<List<KbQARecord>> getHistoryBySessionId(@PathVariable("sessionId") String sessionId) {
        return R.success(kbQARecordService.getHistoryBySessionId(sessionId));
    }

    /**
     * 用户点赞点踩
     */
    @PreAuthorize("@ss.hasPermi('kb:kbqarecord:edit')")
    @Log(title = "知识库问答记录", businessType = BusinessType.UPDATE)
    @PostMapping("/accuracy-or-not")
    public R<Integer> accuracyOrNot(@RequestBody KbQARecordUpdateDto kbQARecordUpdateDto) {
        return R.success(kbQARecordService.updateAccuracyOrNot(kbQARecordUpdateDto.getId(), kbQARecordUpdateDto.getAccuracy()));
    }

}
