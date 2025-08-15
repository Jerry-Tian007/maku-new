package com.ruoyi.gpt.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.gpt.model.domain.HotQuestion;
import com.ruoyi.gpt.model.vo.*;
import com.ruoyi.gpt.service.IHotQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 热点问题Controller
 *
 * @author lijw
 * @date 2023-11-30
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/gpt/hotQuestion")
public class HotQuestionController extends BaseController {
    @Resource
    private IHotQuestionService hotQuestionService;

    /**
     * 获取hotQuestion
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:list')")
    @GetMapping("/list")
    public TableDataInfo list(HotQuestionVo hotQuestionVo) {
        startPage();
        List<HotQuestionVo> list = hotQuestionService.selectHotQuestionList(hotQuestionVo);
        list.forEach(hotQuestionVo1 -> {
            String stringId = hotQuestionVo1.getId().toString();
            hotQuestionVo1.setStringId(stringId);
        });
        return getDataTable(list);
    }

    /**
     * 随机获取6个热点问题
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:list')")
    @GetMapping("/randomQuestionList")
    public R<List<String>> randomQuestionList() {
        List<String> list = hotQuestionService.randomQuestionList();
        return R.success(list);
    }

    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:list')")
    @GetMapping("/monitor")
    public AjaxResult list() {
        HotQuestionVo hotQuestionVo = new HotQuestionVo();
        hotQuestionVo.setRecordId(1000000000000L);
        return success(hotQuestionService.selectHotQuestionList(hotQuestionVo).get(0));
    }

    /**
     * 获取hotQuestion  智能问答前端 沃太
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:list')")
    @GetMapping("/getHotQuestion")
    public AjaxResult list(String role, String region, int questionNum, String language) {
        List<HotQuestionVo> list = hotQuestionService.selectHotQuestionListWoTai(role, region, questionNum, language);
        JSONArray hotQuestion = new JSONArray();

        list.forEach(hotQuestionVo -> {
            hotQuestion.put(hotQuestionVo.getQuestion());
        });

        AjaxResult success = success();
        success.put("hotquestion", hotQuestion);
        return success;
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<HotQuestion> util = new ExcelUtil<HotQuestion>(HotQuestion.class);
        util.importTemplateExcel(response, "热点问题数据");
    }


    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<HotQuestion> util = new ExcelUtil<HotQuestion>(HotQuestion.class);
        List<HotQuestion> hotQuestionList = util.importExcel(file.getInputStream());

        String message = hotQuestionService.importData(hotQuestionList, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 导出热点问题列表
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:export')")
    @Log(title = "热点问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HotQuestionVo hotQuestion) {
        List<HotQuestionVo> list = hotQuestionService.selectHotQuestionList(hotQuestion);
        ExcelUtil<HotQuestionVo> util = new ExcelUtil<HotQuestionVo>(HotQuestionVo.class);
        util.exportExcel(response, list, "热点问题数据");
    }

    /**
     * 获取热点问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hotQuestionService.selectHotQuestionById(id));
    }

    /**
     * 新增热点问题
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:add')")
    @Log(title = "热点问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HotQuestionVo hotQuestion) {
        return toAjax(hotQuestionService.insertHotQuestion(hotQuestion));
    }


    /**
     * 新增热点问题, python插入
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:add')")
    @Log(title = "热点问题", businessType = BusinessType.INSERT)
    @PostMapping("/add/batch")
    public JSONObject addBatch(@Valid @RequestBody List<HotQuestionInsertBackgroundVo> HotQuestionInsertBackgroundVoList) {
        return hotQuestionService.insertHotQuestionBatch(HotQuestionInsertBackgroundVoList);
    }

    /**
     * 修改热点问题
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:edit')")
    @Log(title = "热点问题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody HotQuestionVo hotQuestionVo) {
        return toAjax(hotQuestionService.updateHotQuestion(hotQuestionVo));
    }


    /**
     * 修改热点问题 -- 后管平台
     * 先判断有没有训练，如果训练过了就要同步给psql，如果没有训练过就直接修改, sync all properties
     * 如果没有修改就置NULL
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:edit')")
    @Log(title = "热点问题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit/background/batch")
    public JSONObject editBackgroundBatch(@Valid @RequestBody List<HotQuestionEditBackgroundVo> hotQuestionEditBackgroundVoList) {
        return hotQuestionService.updateHotQuestionBackgroundBatch(hotQuestionEditBackgroundVoList);
    }


    /**
     * 更新 recordId 对应热点问题的tranStatus和 tranFlow
     */
//    @Log(title = "热点问题", businessType = BusinessType.UPDATE)
    @PostMapping("/editByRecordId")
    public AjaxResult edit(@RequestBody HotQuestion hotQuestion) {

        return toAjax(hotQuestionService.updateBatchHotQuestionTranStatusByRecordId(hotQuestion));
    }


    /** todo: 热点问题批量修改国家角色然后传给python， 批量修改只能修改国家和角色，不能修改其他，同步所有语言版本
     * */

    /** todo: 热点问题单个修改国家角色，把其他语言版本的也同步修改国家角色, 如果修改了问题啥的，调用python接口，获取其他语言版本并更新*/


    /** todo: psql删除文件和热点问题*/

    /**
     * 删除热点问题
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:remove')")
    @Log(title = "热点问题", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{ids}")
    public JSONObject remove(@PathVariable Long[] ids) {
        return hotQuestionService.deleteMultiHotQuestionByIds(ids);
    }


    /**
     * 调用python热点问题分析，存库  后管使用
     * param 里面有  regionId  roleId
     * <p>
     */
    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:add')")
    @Log(title = "热点问题", businessType = BusinessType.DELETE)
    @PostMapping("/analyze")
    public JSONObject hotQuestionAnalysis(@Valid @RequestBody HotQuestionAnalyzeVo hotQuestionAnalyzeVo) {
        //热点问题分析，传region和role
        return hotQuestionService.hotQuestionAnalysis(hotQuestionAnalyzeVo);
    }

    @PreAuthorize("@ss.hasPermi('gpt:hotQuestion:add')")
    @Log(title = "热点问题", businessType = BusinessType.DELETE)
    @PostMapping("/train")
    public JSONObject hotQuestionTrain(@Valid @RequestBody HotQuestionTrainVo hotQuestionTrainVo) {
        return hotQuestionService.hotQuestionTrain(hotQuestionTrainVo);
    }


}
