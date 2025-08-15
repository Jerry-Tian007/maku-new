package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.i18n.ReturnMessageEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.http.RestTemplateUtils;
import com.ruoyi.gpt.mapper.HotQuestionMapper;
import com.ruoyi.gpt.model.domain.DeptKbConfig;
import com.ruoyi.gpt.model.domain.HotQuestion;
import com.ruoyi.gpt.model.vo.*;
import com.ruoyi.gpt.service.IHotQuestionService;
import com.ruoyi.gpt.service.IKbQARecordService;
import com.ruoyi.gpt.service.impl.util.RoleRegionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 热点问题Service业务层处理
 *
 * @author lijw
 * @date 2023-11-30
 */

@Service
@Slf4j
public class HotQuestionServiceImpl implements IHotQuestionService {
    @Autowired
    private HotQuestionMapper hotQuestionMapper;

    @Resource
    private DeptKbConfigServiceImpl deptKbConfigService;

    @Resource
    private IKbQARecordService kbQARecordService;

    @Resource
    private RoleRegionServiceImpl roleRegionService;

    @Resource
    protected Validator validator;

    @Value("${hotQA.hotQaModify}")
    private String PYTHON_HOT_QA_MODIFY;

    @Value("${hotQA.hotQaAdd}")
    private String PYTHON_HOT_QA_ADD;

    @Value("${hotQA.hotQaAnalysis}")
    private String PYTHON_HOT_QA_ANALYSIS;

    @Value("${hotQA.hotQaStore}")
    private String PYTHON_HOT_QA_STORE;

    @Value("${hotQA.hotQaDele}")
    private String PYTHON_HOT_QA_DELE;

    /**
     * 查询热点问题 后管
     *
     * @param id 热点问题主键
     * @return 热点问题
     */
    @Override
    public HotQuestion selectHotQuestionById(Long id) {
        return hotQuestionMapper.selectHotQuestionById(id);
    }


    /**
     * 查询热点问题列表
     *
     * @param hotQuestionVo 热点问题
     * @return 热点问题
     */
    @Override
    public List<HotQuestionVo> selectHotQuestionList(HotQuestionVo hotQuestionVo) {
        return hotQuestionMapper.selectHotQuestionList(hotQuestionVo);
    }


    /**
     * 查询热点问题列表 wotai
     *
     * @return 热点问题
     */
    @Override
    public List<HotQuestionVo> selectHotQuestionListWoTai(String role, String region, int questionNum, String language) {
        return hotQuestionMapper.selectHotQuestionListWoTai(role, region, questionNum, language);
    }

    /**
     * 新增热点问题 后管手动新增热点问题, 传入的是region和role
     *
     * @param hotQuestion 热点问题
     * @return 结果
     */
    @Override
    public int insertHotQuestion(HotQuestion hotQuestion) {
        hotQuestion.setCreateTime(DateUtils.getNowDate());
        hotQuestion.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        hotQuestion.setStatus("untrained");
        if (hotQuestion.getRecordId() == null) {
            hotQuestion.setRecordId(System.currentTimeMillis());
        }

        List<String> languageList = hotQuestionMapper.selectDistinctLanguage().stream().map(HotQuestion::getLanguage).collect(Collectors.toList());

        // 新增的时候对于每一种语言都要插入
        for (String language : languageList) {
            hotQuestion.setLanguage(language);
            return hotQuestionMapper.insertHotQuestion(hotQuestion);
        }
        return 0;
    }

    /**
     * 新增热点问题 热点问题分析插入
     *
     * @param hotQuestion 热点问题
     * @return 结果
     */
    @Override
    public int insertHotQuestionKbqa(HotQuestion hotQuestion) {
        hotQuestion.setCreateTime(DateUtils.getNowDate());
        hotQuestion.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        return hotQuestionMapper.insertHotQuestion(hotQuestion);
    }

    /**
     * 修改热点问题
     *
     * @param hotQuestionVo 热点问题
     * @return 结果
     */
    @Override
    public int updateHotQuestion(HotQuestionVo hotQuestionVo) {
        return hotQuestionMapper.updateHotQuestion(hotQuestionVo);
    }


    @Override
    public JSONObject updateHotQuestionBackgroundBatch(List<HotQuestionEditBackgroundVo> hotQuestionEditBackgroundVoList) {
        HotQuestionEditBackgroundVo hotPyJson = new HotQuestionEditBackgroundVo();

        JSONObject pythonParam = new JSONObject();
        JSONArray pythonJSONArray = new JSONArray();

        StringBuffer stringBuffer = new StringBuffer();

        for (HotQuestionEditBackgroundVo hotQuestionEditBackgroundVo : hotQuestionEditBackgroundVoList) {
            Long recordId = hotQuestionEditBackgroundVo.getRecordId();
            Optional<String> regionOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getRegion());
            Optional<String> roleOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getRole());
            Optional<String> questionOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getQuestion());
            Optional<String> answerOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getAnswer());
            Optional<String> statusOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getStatus());
            Optional<String> languageOptional = Optional.ofNullable(hotQuestionEditBackgroundVo.getLanguage());
            hotPyJson.setRecordId(recordId);

            // 初始化
            HotQuestionVo hotQuestionOldVo = new HotQuestionVo();
            JSONObject tmpQA = new JSONObject();

            //找到recordId对应的所有热点问题，相比较
            hotQuestionOldVo.setRecordId(recordId);
            hotQuestionOldVo.setLanguage(languageOptional.orElse(null));
            //用language查询出具体语言的， 下面需要对问答做比较
            List<HotQuestionVo> hotQuestionOldVos = selectHotQuestionList(hotQuestionOldVo);
            if (hotQuestionOldVos.isEmpty()) {
                log.error("该recordId：{}对应的热点问题为空", recordId);
                stringBuffer.append("recordId：{}对应的热点问题为空").append(System.lineSeparator());
                continue;
            }
            if (StringUtils.isNull(hotQuestionOldVos.get(0))) {
                log.error("该recordId：{}对应的热点问题不存在", recordId);
                stringBuffer.append("recordId：{}对应的热点问题为空").append(System.lineSeparator());
                continue;
            }

            //先判断有没有question answer， 如果有的话且和原来不同，就给python处理
            if (
                    !((questionOptional.isPresent() &&
                            (questionOptional.get()
                                    .equals(hotQuestionOldVos.get(0).getQuestion()
                                            .replaceAll("\\p{C}", "")
                                    )
                            )
                    )
                            && (answerOptional.isPresent() &&
                            (answerOptional.get()
                                    .equals(hotQuestionOldVos.get(0).getAnswer()
                                            .replaceAll("\\p{C}", "")
                                    )
                            )
                    ))
            ) {
                // 条件成立时的处理

                constructHotPyUpdateJson(hotPyJson, regionOptional, roleOptional, tmpQA, questionOptional, answerOptional, statusOptional, languageOptional);
                pythonJSONArray.add(hotPyJson);
            }

            //如果没有question answer 或者 question answer和原来一致，就判断有没有train
            // ， 如果train就给python处理， 否则java处理
            else {
                if (StringUtils.isEmpty(hotQuestionOldVos.get(0).getStatus()) || hotQuestionOldVos.get(0).getStatus().equals("untrained")) {
                    //未训练的，更新region role
                    HotQuestionServiceImpl hotServiceAop = (HotQuestionServiceImpl) AopContext.currentProxy();
                    hotServiceAop.untrainedUpdate(regionOptional, roleOptional, hotQuestionOldVos);
                } else {
                    //已经训练的，全部给python处理
                    constructHotPyUpdateJson(hotPyJson, regionOptional, roleOptional, tmpQA, Optional.empty(), Optional.empty(), statusOptional, languageOptional);
                    pythonJSONArray.add(hotPyJson);
                }
            }
        }

        pythonParam.putOpt("record_list", pythonJSONArray);
        log.info("pythonParam:{}", pythonParam);

        JSONObject pythonResponse = RestTemplateUtils.post(pythonParam, PYTHON_HOT_QA_MODIFY);
        pythonResponse.append("error_msg", stringBuffer.toString());
        return pythonResponse;
    }

    @Override
    public int deleteHotQuestionByIds(Long[] ids) {
        return hotQuestionMapper.deleteHotQuestionByIds(ids);
    }


    /**
     * 删除热点问题信息
     *
     * @param id 热点问题主键
     * @return 结果
     */
    @Override
    public int deleteHotQuestionById(Long id) {
        return hotQuestionMapper.deleteHotQuestionById(id);
    }

    /**
     * 热点问题分析的config
     */
    @Override
    public JSONObject getAnalysisConfig() {

        DeptKbConfig deptKbConfig = deptKbConfigService.selectDeptKbConfigById(2L);
        JSONObject kbConfigJson = new JSONObject();
        kbConfigJson.putOpt("openaiApiBase", deptKbConfig.getOpenaiApiBase());
        kbConfigJson.putOpt("openaiApiVersion", deptKbConfig.getOpenaiApiVersion());
        kbConfigJson.putOpt("openaiApiKey", deptKbConfig.getOpenaiApiKey());
        kbConfigJson.putOpt("openaiChatModel", deptKbConfig.getOpenaiChatgptDeployment());
        kbConfigJson.putOpt("openaiEmbeddingModel", deptKbConfig.getOpenaiQueryEmbeddingDeployment());
        kbConfigJson.putOpt("redisHost", deptKbConfig.getRedisHost());
        kbConfigJson.putOpt("redisPassword", deptKbConfig.getRedisPassword());
        kbConfigJson.putOpt("redisPort", deptKbConfig.getRedisPort());
        return kbConfigJson;
    }

    @Override
    public JSONObject getTrainConfig() {
        DeptKbConfig deptKbConfig = deptKbConfigService.selectDeptKbConfigById(2L);
        JSONObject kbConfigJson = new JSONObject();
        kbConfigJson.putOpt("openaiEmbeddingModel", deptKbConfig.getOpenaiQueryEmbeddingDeployment());
        kbConfigJson.putOpt("redisHost", deptKbConfig.getRedisHost());
        kbConfigJson.putOpt("redisPassword", deptKbConfig.getRedisPassword());
        kbConfigJson.putOpt("redisPort", deptKbConfig.getRedisPort());
        return kbConfigJson;
    }


    /**
     * 更新 同一recordId所有语言的 对应热点问题的tranStatus和 tranFlow
     */
    @Override
    public int updateBatchHotQuestionTranStatusByRecordId(HotQuestion hotQuestion) {
        List<String> languageList = hotQuestionMapper.selectDistinctLanguage().stream().map(HotQuestion::getLanguage).collect(Collectors.toList());

        int updateSum = 0;
        for (String language : languageList) {

            updateSum += hotQuestionMapper.updateBatchHotQuestionTranStatusByRecordId(hotQuestion.getStatus(), hotQuestion.getTrainFlow(), hotQuestion.getTrainTime(), hotQuestion.getRecordId(), language);
        }
        return updateSum;

    }

    /**
     * 导入热点问题数据
     *
     * @param hotQuestionList 热点问题数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importData(List<HotQuestion> hotQuestionList, boolean isUpdateSupport) {
        if (StringUtils.isNull(hotQuestionList) || hotQuestionList.isEmpty()) {
            throw new ServiceException("导入热点问题数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        HotQuestionVo hotQuestionVo = new HotQuestionVo();

        for (HotQuestion hotQuestion : hotQuestionList) {
            try {
                // 验证是否存在这个问题
                hotQuestionVo = (HotQuestionVo) hotQuestion;


                List<HotQuestionVo> hotQuestionList1 = hotQuestionMapper.selectHotQuestionList(hotQuestionVo);

                if (hotQuestionList1.isEmpty()) {
                    BeanValidators.validateWithException(validator, hotQuestion);
                    successNum += hotQuestionMapper.insertHotQuestion(hotQuestion);

                    successMsg.append("<br/>" + successNum + "、热点问题 " + hotQuestion.getQuestion() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, hotQuestion);
                    Long id = hotQuestionMapper.selectHotQuestionList(hotQuestionVo).get(0).getId();
                    hotQuestionVo.setId(id);
                    // setUpdateBy
                    successNum += hotQuestionMapper.updateHotQuestion(hotQuestionVo);

                    successMsg.append("<br/>" + successNum + "、热点问题 " + hotQuestion.getQuestion() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、热点问题 " + hotQuestion.getQuestion() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、热点问题 " + hotQuestion.getQuestion() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public int deleteHotQuestionByRecordId(Long[] ids) {
        return 0;
    }

    public void updateBatchHotQuestionByRecordId(HotQuestion hotQuestion) {

        HotQuestion hotQuestion1 = new HotQuestion();
        hotQuestion1.setRecordId(hotQuestion.getRecordId());
        hotQuestion1.setRegion(hotQuestion.getRegion());
        hotQuestion1.setRole(hotQuestion.getRole());

        //修改record_id这条记录状态的热点问题
        hotQuestionMapper.updateBatchHotQuestionByRecordId(hotQuestion1);
    }

    //    @Override
    public void updateSingleHotQuestionByRecordId(HotQuestion hotQuestion) {
        List<String> languageList = hotQuestionMapper.selectDistinctLanguage().stream().map(HotQuestion::getLanguage).collect(Collectors.toList());
        int updateSum = 0;
        for (String language : languageList) {
            updateSum += hotQuestionMapper.updateSingleHotQuestionByRecordId(hotQuestion.getRole(), hotQuestion.getRegion(), hotQuestion.getRecordId(), language);
        }
        //修改record_id这条记录状态的热点问题
        hotQuestionMapper.updateSingleHotQuestionByRecordId(hotQuestion.getRole(), hotQuestion.getRegion(), hotQuestion.getRecordId(), "record_id");
    }

    @Override
    public JSONObject deleteMultiHotQuestionByIds(Long[] ids) {

        int deleCnt = 0;
        List<HotQuestion> removeList = Arrays.stream(ids).map(this::selectHotQuestionById).collect(Collectors.toList());

        Long[] recordIds = removeList.stream()
                .map(HotQuestion::getRecordId)
                .toArray(Long[]::new);
        if (recordIds.length > 0) {
            deleCnt = hotQuestionMapper.deleteHotQuestionByRecordIds(recordIds);
        }

        List<Long> trainedRecordIds = removeList.stream()
                .filter(ele -> !ele.getStatus().equals("untrained")).map(HotQuestion::getRecordId).collect(Collectors.toList());
        if (!trainedRecordIds.isEmpty()) {
            JSONObject pythonParam = new JSONObject();
            pythonParam.putOpt("recordId", trainedRecordIds);
            log.info("pythonParam:{}", pythonParam);
            JSONObject res = RestTemplateUtils.post(pythonParam, PYTHON_HOT_QA_DELE);
            log.info("pythonResponse is {}", res);
            return res;
        }

        if (deleCnt != 0) {
            return JSONUtil.createObj().putOpt("code", 200).putOpt("msg", "操作成功").putOpt("data", "删除" + deleCnt + "条数据");
        }
        return JSONUtil.createObj().putOpt("code", 500).putOpt("msg", "操作失败").putOpt("data", "删除" + deleCnt + "条数据");
    }

    @Override
    public JSONObject insertHotQuestionBatch(List<HotQuestionInsertBackgroundVo> HotQuestionInsertBackgroundVoList) {
        JSONObject pythonParam = new JSONObject();
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        HotQuestionInsertBackgroundVoList.forEach(vo -> {
            vo.setCreate(nickName);
        });
        pythonParam.putOpt("record_list", HotQuestionInsertBackgroundVoList);
        log.info("pythonParam:{}", pythonParam);
        return RestTemplateUtils.post(pythonParam, PYTHON_HOT_QA_ADD);
    }

    @Override
    public JSONObject hotQuestionAnalysis(HotQuestionAnalyzeVo hotQuestionAnalyzeVo) {
        //python需要recordId，regionId，roleId
        String region = hotQuestionAnalyzeVo.getRegion();
        String role = hotQuestionAnalyzeVo.getRole();

        List<Long> regionId = Arrays.stream(region.split(";")).collect(Collectors.toList())
                .stream()
                .map(roleRegionService::getRegionId)
                .collect(Collectors.toList());

        List<Long> roleId = Arrays.stream(role.split(";")).collect(Collectors.toList())
                .stream()
                .map(roleRegionService::getRoleId)
                .collect(Collectors.toList());

        JSONArray pythonRegionIdArray = new JSONArray(regionId);
        JSONArray pythonRoleIdArray = new JSONArray(roleId);

        List<Long> ids = kbQARecordService.selectKbQARecordByRegionsAndRoles(regionId.stream().mapToLong(Long::longValue).toArray(),
                roleId.stream().mapToLong(Long::longValue).toArray());


        JSONArray idArray = new JSONArray();
        idArray.addAll(ids);

        hotQuestionAnalyzeVo.setRegionId(pythonRegionIdArray);
        hotQuestionAnalyzeVo.setRoleId(pythonRoleIdArray);
        hotQuestionAnalyzeVo.setConfig(getAnalysisConfig());
        hotQuestionAnalyzeVo.setId(idArray);
        JSONObject pythonParam = JSONUtil.parseObj(hotQuestionAnalyzeVo);

        log.info("pythonParam is " + pythonParam);

        JSONObject pythonResponse = RestTemplateUtils.post(pythonParam, PYTHON_HOT_QA_ANALYSIS);
        if (pythonResponse.getInt("code") == 200) {
            JSONArray data = pythonResponse.getJSONArray("data");

            log.info("*************************data是***************" + data.toString());


            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);

                Set<String> keys = jsonObject.keySet();
                for (String key : keys) {
                    if (key.equals("record_id")) {
                        continue;
                    }
                    String language = "";
                    language = key;
                    HotQuestion hotQuestion = new HotQuestionVo();
                    hotQuestion.setQuestion(jsonObject.getJSONObject(language).getStr("question"));
                    hotQuestion.setAnswer(jsonObject.getJSONObject(language).getStr("answer"));
                    hotQuestion.setRecordId(jsonObject.getLong("record_id"));
                    hotQuestion.setLanguage(language);
                    hotQuestion.setRegion(region);
                    hotQuestion.setRole(role);
                    hotQuestion.setStatus("untrained");
                    insertHotQuestionKbqa(hotQuestion);
                }

            }
        }

        return pythonResponse;
    }

    @Override
    public JSONObject hotQuestionTrain(HotQuestionTrainVo hotQuestionTrainVo) {

        List<HotQuestionTrainItemVo> list = hotQuestionTrainVo.getList();

        for (HotQuestionTrainItemVo hotQuestionTrainItemVo : list) {
            JSONArray regionArray = new JSONArray();
            JSONArray roleArray = new JSONArray();

            String[] regionSplited = hotQuestionTrainItemVo.getRegionList().split(";");
            List<String> regionList = Arrays.stream(regionSplited).collect(Collectors.toList());


            regionList.forEach(region -> {
                Long regionId = roleRegionService.getRegionId(region);
                regionArray.add(regionId);
            });

            String[] roleSplited = hotQuestionTrainItemVo.getRoleList().split(";");
            List<String> roleList = Arrays.stream(roleSplited).collect(Collectors.toList());

            roleList.forEach(role -> {
                Long roleId = roleRegionService.getRoleId(role);
                roleArray.add(roleId);
            });
            hotQuestionTrainItemVo.setRegionId(regionArray);
            hotQuestionTrainItemVo.setRoleId(roleArray);
        }

        JSONObject pythonParam = new JSONObject();

        pythonParam.putOpt("list", list);
        pythonParam.putOpt("config", getTrainConfig());
        pythonParam.putOpt("trainType", "Embedding");
        pythonParam.putOpt("userId", SecurityUtils.getUserId());
        pythonParam.putOpt("deptId", SecurityUtils.getDeptId());

        pythonParam.remove("roleList");
        pythonParam.remove("regionList");

        log.info("pythonParam is " + pythonParam);

        return RestTemplateUtils.post(pythonParam, PYTHON_HOT_QA_STORE);
    }

    @Override
    public List<String> randomQuestionList() {
        List<String> stringList = new ArrayList<>();
        stringList.add(ReturnMessageEnum.hello.toString());
        stringList.add(ReturnMessageEnum.what_is_your_name.toString());
        stringList.add(ReturnMessageEnum.how_old_are_you.toString());
        stringList.add(ReturnMessageEnum.are_you_male_or_female.toString());
        stringList.add(ReturnMessageEnum.are_you_an_artificial_intelligence.toString());
        stringList.add(ReturnMessageEnum.are_you_a_robot.toString());
        stringList.add(ReturnMessageEnum.who_are_you.toString());
        stringList.add(ReturnMessageEnum.what_are_you_doing.toString());
        stringList.add(ReturnMessageEnum.do_you_speak_english.toString());
        stringList.add(ReturnMessageEnum.can_you_speak_chinese.toString());
        stringList.add(ReturnMessageEnum.do_you_speak_french.toString());
        stringList.add(ReturnMessageEnum.do_you_speak_german.toString());
        stringList.add(ReturnMessageEnum.can_you_speak_japanese.toString());
        List<String> result = new ArrayList<>();
        while (result.size() < 6) {
            int random = (int) (Math.random() * stringList.size());
            if (!result.contains(stringList.get(random))) {
                result.add(stringList.get(random));
            }
        }
        return result;
    }


    private <T> void getBatchUpdateValue(Optional<T> optional, List<HotQuestionVo> hotQuestionOldVos, Function<HotQuestionVo, T> getter, Consumer<T> setter) {
        if (optional.isPresent() && !hotQuestionOldVos.isEmpty()) {
            T value = getter.apply(hotQuestionOldVos.get(0));
            if (value == null || value.equals(optional.get())) {
                setter.accept(null);
            } else {
                setter.accept(optional.get());
            }
        }
    }

    private void constructHotPyUpdateJson(HotQuestionEditBackgroundVo hotPyJson, Optional<String> regionOptional, Optional<String> roleOptional, JSONObject tmpQA, Optional<String> questionOptional, Optional<String> answerOptional, Optional<String> statusOptional, Optional<String> languageOptional) {
        hotPyJson.setRegion(regionOptional.orElse(null));
        hotPyJson.setRole(roleOptional.orElse(null));
        hotPyJson.setStatus(statusOptional.orElse(null));
        hotPyJson.setLanguage(languageOptional.orElse(null));
        hotPyJson.setQuestion(questionOptional.orElse(null));
        hotPyJson.setAnswer(answerOptional.orElse(null));

        tmpQA.putOpt("question", questionOptional.orElse(null));
        tmpQA.putOpt("answer", answerOptional.orElse(null));
        if (questionOptional.orElse(null) == null && answerOptional.orElse(null) == null) {
            tmpQA = null;
        }
        hotPyJson.setQa(tmpQA);
    }

    private HotQuestion vo2HotQuestion(Optional<String> regionOptional, Optional<String> roleOptional, Long recordId) {
        HotQuestion hotQuestion = new HotQuestion();
        regionOptional.ifPresent(hotQuestion::setRegion);
        roleOptional.ifPresent(hotQuestion::setRole);
        hotQuestion.setRecordId(recordId);
        return hotQuestion;
    }

    @Transactional(rollbackFor = Exception.class)
    public void untrainedUpdate(Optional<String> regionOptional, Optional<String> roleOptional, List<HotQuestionVo> hotQuestionOldVos) {
        Consumer<HotQuestion> updateFunction = this::updateBatchHotQuestionByRecordId;
        hotQuestionOldVos.stream()
                .filter(vo -> ((StringUtils.isEmpty(vo.getStatus()) || "untrained".equals(vo.getStatus()))))
                .map(vo -> vo2HotQuestion(regionOptional, roleOptional, vo.getRecordId()))
                .forEach(updateFunction);
        hotQuestionOldVos.removeIf(vo -> ((StringUtils.isEmpty(vo.getStatus()) || "untrained".equals(vo.getStatus()))));
    }
}
