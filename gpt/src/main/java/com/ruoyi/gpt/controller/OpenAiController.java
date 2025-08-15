package com.ruoyi.gpt.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.model.domain.ChatgptQARecord;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.domain.ThirdPartyError;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.service.IChatgptQARecordService;
import com.ruoyi.gpt.service.IKbQARecordService;
import com.ruoyi.gpt.service.impl.DeptKbConfigServiceImpl;
import com.ruoyi.gpt.service.impl.util.RoleRegionServiceImpl;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/ny")
public class OpenAiController {

    @Resource
    private IChatgptQARecordService iChatgptQARecordService;

    @Resource
    private IKbQARecordService kbQARecordService;

    @Resource
    private ISysUserService iSysUserService;

    @Resource
    private DeptKbConfigServiceImpl deptKbConfigService;

    @Resource
    private RoleRegionServiceImpl roleRegionService;

    @Resource
    private RedisCache redisCache;

    @Value("${chatGPT.availableBalancePrefix}")
    private String availableBalancePrefix;

    private static final String GPT_35 = "";
    private static final String GPT_40 = "";
    private static final String GPT_40_32K = "";
    private static final String GPT_40_32K_UNEXAMINE = "";

    private static final String PT_GPT_35 = "";
    private static final String PT_GPT_40 = "";

    private static final String WXYY = "";

    private static final String CHATGLM2_6B = "";

    private static final String XFXH = "";

    @Value("${kbQA.pythonSummaryTitle}")
    private String PYTHON_SUMMARY_TITLE;

    @Value("${kbQA.kbQAAsk}")
    private String PYTHON_KBQA;


    /**
     * 平台内部调用XFXH
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:xfxh')")
    @PostMapping("/xfxh")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public JSONObject XFXH(@RequestBody JSONObject params) {
        return askQuestionByXfxh(params, XFXH);
    }

    /**
     * 平台内部调用chatglm2_6b
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:chatglm2_6b')")
    @PostMapping("/chatglm2_6b")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public JSONObject CHATGLM2_6B(@RequestBody JSONObject params) {

        return askQuestionByGpt(params, CHATGLM2_6B);
    }

    /**
     * 平台内部调用3.5
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:gpt35')")
    @PostMapping("/gpt35")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @RateLimiter(count = 90, time = 60, limitType = LimitType.TOKEN)
    public JSONObject azureOpenAiGpt35(@RequestBody JSONObject params) {
        if (this.checkMaxConsumptionAmount() == -1) {
            return new JSONObject().set("code", "429").set("msg", "超过最大消费额度");
        }
        params.set("key", "");
        return askQuestionByGpt(params, PT_GPT_35);
    }

    /**
     * 平台内部调用4.0
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:gpt40')")
    @PostMapping("/gpt40")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @RateLimiter(count = 6, time = 60, limitType = LimitType.IP)
    public JSONObject azureOpenAiGpt40(@RequestBody JSONObject params) {
        if (this.checkMaxConsumptionAmount() == -1) {
            return new JSONObject().set("code", "429").set("msg", "超过最大消费额度");
        }
        params.set("key", "");
        return askQuestionByGpt(params, PT_GPT_40);
    }


    /**
     * 平台内部调用文心一言
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:wxyy')")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @PostMapping("/wxyy")
    public JSONObject wxyyApi(@RequestBody JSONObject params) {
        return askQuestionByWxyy(params, WXYY);
    }

    /**
     * 第三方对外调用3.5
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt35')")
    @PostMapping("/thirdParty/gpt35")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @RateLimiter(count = 90, time = 60, limitType = LimitType.TOKEN)
    public JSONObject thirdPartyAskQuestion35(@RequestBody JSONObject params) {
        try {
            params.set("key", SecurityUtils.getDeptId() + "-" + SecurityUtils.getUserId());
            JSONObject answer = askQuestionByGpt(params, GPT_35);
            insertQARecord(params, answer, "gpt-35-turbo");
            return answer;
        } catch (Exception e) {
            log.error("第三方调用3.5接口异常", e);
            ThirdPartyError thirdPartyError = new ThirdPartyError();
            thirdPartyError.setErrorDetail(e.getMessage());
            thirdPartyError.setDeptId(SecurityUtils.getDeptId());
            insertError(thirdPartyError);
            return new JSONObject().set("message", "OpenAi服务繁忙，请稍后重试");
        }
    }

    /**
     * 第三方对外调用4.0
     *
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt40')")
    @PostMapping("/thirdParty/gpt40")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @RateLimiter(count = 6, time = 60, limitType = LimitType.TOKEN)
    public JSONObject thirdPartyAskQuestion40(@RequestBody JSONObject params) {
        try {
            params.set("key", SecurityUtils.getDeptId() + "-" + SecurityUtils.getUserId());
            JSONObject answer = askQuestionByGpt(params, GPT_40);
            insertQARecord(params, answer, "gpt-4");
            return answer;
        } catch (Exception e) {
            log.error("第三方调用4.0接口异常", e);
            ThirdPartyError thirdPartyError = new ThirdPartyError();
            thirdPartyError.setErrorDetail(e.getMessage());
            thirdPartyError.setDeptId(SecurityUtils.getDeptId());
            insertError(thirdPartyError);
            return new JSONObject().set("message", "OpenAi服务繁忙，请稍后重试");
        }
    }

    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt40-32K:unexamine')")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @PostMapping("/thirdParty/gpt40-32k/unexamine")
    @RateLimiter(count = 10, time = 60, limitType = LimitType.TOKEN)
    public JSONObject azureOpenAiGpt4032KUnexamine(@RequestBody JSONObject params) {
        try {
            params.set("key", SecurityUtils.getDeptId() + "-" + SecurityUtils.getUserId());
            JSONObject answer = askQuestionByGpt(params, GPT_40_32K_UNEXAMINE);
            insertQARecord(params, answer, "gpt-4-32k");
            return answer;
        } catch (Exception e) {
            log.error("第三方调用4.0-32k接口异常", e);
            ThirdPartyError thirdPartyError = new ThirdPartyError();
            thirdPartyError.setErrorDetail(e.getMessage());
            thirdPartyError.setDeptId(SecurityUtils.getDeptId());
            insertError(thirdPartyError);
            return new JSONObject().set("message", "OpenAi服务繁忙，请稍后重试");
        }
    }

    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt40-32K')")
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    @PostMapping("/thirdParty/gpt40-32k")
    @RateLimiter(count = 10, time = 60, limitType = LimitType.TOKEN)
    public JSONObject azureOpenAiGpt4032K(@RequestBody JSONObject params) {
        try {
            params.set("key", SecurityUtils.getDeptId() + "-" + SecurityUtils.getUserId());
            JSONObject answer = askQuestionByGpt(params, GPT_40_32K);
            insertQARecord(params, answer, "gpt-4-32k");
            return answer;
        } catch (Exception e) {
            log.error("第三方调用4.0-32k接口异常", e);
            ThirdPartyError thirdPartyError = new ThirdPartyError();
            thirdPartyError.setErrorDetail(e.getMessage());
            thirdPartyError.setDeptId(SecurityUtils.getDeptId());
            insertError(thirdPartyError);
            return new JSONObject().set("message", "OpenAi服务繁忙，请稍后重试");
        }
    }


    /**
     * 调用GPT提问接口
     *
     * @param params
     * @return
     */
    public JSONObject askQuestionByXfxh(JSONObject params, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
        return JSONUtil.parseObj(stringResponseEntity.getBody());
    }

    /**
     * 调用GPT提问接口
     *
     * @param params
     * @param url
     * @return
     */
    public JSONObject askQuestionByGpt(JSONObject params, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);

        // 非admin用户才更新剩余最大消费额度
        if (!"admin".equals(SecurityUtils.getUsername())) {
            this.updateMaxConsumption(stringResponseEntity);
        }
        return JSONUtil.parseObj(stringResponseEntity.getBody());
    }

    /**
     * 调用GPT提问接口
     *
     * @param params
     * @return
     */
    public JSONObject askQuestionByWxyy(JSONObject params, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
        return JSONUtil.parseObj(stringResponseEntity.getBody());
    }

    /**
     * 通过sessionId查找kbqarecord，通过createdTime排序，拼接一个个qa对，形成json，调用python接口获取sumamry和title （Python)
     */
    @GetMapping("/gpt/summary")
    public AjaxResult summaryByKbqa(@RequestParam("userId") String userId, @RequestParam("sessionId") String sessionId,
                                    @RequestParam("titleSize") Integer titleSize, @RequestParam("contentSize") Integer contentSize,
                                    @RequestParam("language") String language) {
        List<KbQARecordDto> kbQARecordVos = kbQARecordService.selectKbQARecordBySessionId(sessionId);
        for (KbQARecordDto kbQARecordDto : kbQARecordVos) {
            System.out.println(kbQARecordDto);
        }
        JSONArray jsonArrayResult = new JSONArray();
        kbQARecordVos.stream().map(record -> {
            JSONObject json = new JSONObject();
            json.set("user", record.getQuestion());
            json.set("bot", record.getAnswer());
            return json;
        }).forEach(jsonArrayResult::add);

        JSONObject params = new JSONObject();
        params.set("history", jsonArrayResult);
        params.set("titleSize", titleSize);
        params.set("contentSize", contentSize);
        params.set("language", language);

        System.out.println(params);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(PYTHON_SUMMARY_TITLE, entity, String.class);
        JSONObject pythonResponse = JSONUtil.parseObj(stringResponseEntity.getBody());
        if (pythonResponse.getInt("code") == 200) {
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.put("code", 200);
            JSONObject data = pythonResponse.getJSONObject("data");
            String summary = data.getStr("Summary");
            String title = data.getStr("Title");
            ajaxResult.put("summary", summary);
            ajaxResult.put("title", title);
            ajaxResult.put("msg", pythonResponse.getStr("msg"));
            return ajaxResult;
        }
        return AjaxResult.error("问答总结出错");
    }

    // TODO: 2023/11/23 知识库智能问答： 前端传过来一个history的jsonArray，
    // TODO: "history": [
    // TODO:{
    // TODO:    "user": "你好",
    // TODO:        "bot": "您好！我是一名严谨、权威的智能助手，我的回答仅基于私有领域知识库中的参考内容。如果您有任何与私有领域知识库相关的问题，请随时提问。"
    // TODO:},
    // TODO: {
    // TODO:    "user": " IR 故障如何处理"
    // TODO:}
    // TODO:]
    // TODO:遍历这个history中的每一个json，找到最后一个json，返回user的问题，把productName也解析出来，再调用python接口获取result
    // TODO:获取完成result之后,存库，存库的格式就是一个个qa对，加上sessionId
    // TODO:然后返回这组问答的记录
    // TODO:
    // TODO: 每次的问答对都要存title，就直接拿jsonArray的第一个
    // TODO:
    // TODO:

/*    	"approach": "rrr",
                "top": 4,
                "search": "vector",
                "sessionId": "978654321",
                "region": "China",
                "userId": 276,
                "role": "installer",
                "inverter": "光伏逆变器",
                "power": "小型电源",
                "trainType": "Embedding"*/

    /**
     * 调用知识库智能问答接口（Python)
     */
    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt35')")
    @PostMapping("/gpt/kbqaAsk")
    public JSONObject askByKbqa(@RequestBody JSONObject params) {
        String title = "";
        int successInsert = 0;
        String sessionId = "";

        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        JSONArray jsonArray = (JSONArray) params.get("history");
        title = jsonArray.getJSONObject(jsonArray.size() - 1).getStr("user");


        if (params.getStr("sessionId") == null) {
            sessionId = String.valueOf(System.currentTimeMillis());
            kbQARecordDto.setSessionId(sessionId);
            //插入一条新的记录
        } else {
            sessionId = params.getStr("sessionId");
            kbQARecordDto.setSessionId(params.getStr("sessionId"));
        }

        List<KbQARecordDto> kbQARecordDtos = kbQARecordService.selectKbQARecordBySessionId(sessionId);
        List<Long> idList = kbQARecordDtos.stream().map(KbQARecord::getId).collect(Collectors.toList());

        KbQARecordDto kbqaRecordTemp = new KbQARecordDto();
        kbqaRecordTemp.setChatTitle(title);
        idList.forEach(id1 -> {
            kbqaRecordTemp.setId(id1);
            kbQARecordService.updateKbQARecord(kbqaRecordTemp);
        });

        insertUser(params, SecurityUtils.getDeptId());

        //调用python接口
        ResponseEntity<String> stringResponseEntity = getStringResponseEntity(params, kbQARecordDto);
        JSONObject pythonResponse = JSONUtil.parseObj(stringResponseEntity.getBody());

        //最新一条的提问
        kbQARecordDto.setQuestion(jsonArray.getJSONObject(jsonArray.size() - 1).getStr("user"));

        if (pythonResponse.getInt("code") == 200 && pythonResponse.getJSONObject("data").getStr("error") != null) {
            String error = pythonResponse.getJSONObject("data").getStr("error");
//            {
//                "code": 200,
//                    "data": {
//                "error": "文件未训练，请前往文件管理进行训练。"
//            },
//                "msg": "回答生成成功"
//            }
            kbQARecordDto.setAnswer(error);
        } else if (pythonResponse.getInt("code") != 200) {
            kbQARecordDto.setAnswer(pythonResponse.getStr("msg"));
        }

        kbQARecordDto.setChatTitle(title);
        kbQARecordDto.setDeptId(SecurityUtils.getDeptId());
        //token不一定是当前帐号，当前帐号是传进来的userId， 但是他们的部门肯定相同， 相当于token是大账号，而其他传进来的userId是小账号
        kbQARecordDto.setUserId(params.getLong("userId"));
        kbQARecordDto.setCreateBy(String.valueOf(params.getLong("userId")));
        kbQARecordDto.setCreateTime(DateUtils.getNowDate());

        String role = params.getStr("role");
        if (role.equals("超级管理员")) {
            JSONObject entries = new JSONObject();
            entries.putOpt("code", 500);
            entries.putOpt("msg", "角色错误");
            return entries;
        }

        if (pythonResponse.getInt("code") == 200 && pythonResponse.getJSONObject("data").getStr("error") == null) {
            //此时， python响应为200，且没有error字段，那么就 有answer和questionVector
            kbQARecordDto.setAnswer(pythonResponse.getJSONObject("data").getStr("answer"));
            log.info("******************************");
            //hiddenQuestion就是猜你想问,在pythonResponse中
        }

        //给问答记录设置问题向量
        kbQARecordDto.setQuestionVector(String.valueOf((pythonResponse.getJSONObject("data").getJSONArray("questionVector"))));
        //在python返回中删去问题向量
        pythonResponse.getJSONObject("data").remove("questionVector");
        //设置response，用来让前端展示
        kbQARecordDto.setResponse(String.valueOf(pythonResponse.getJSONObject("data")));
        //插入问答记录
                if (kbQARecordService.insertKbQARecordKbqa(kbQARecordDto) != null) {
                    successInsert ++;
                };
        Long id = kbQARecordDto.getId();
        log.info("插入后的QAid为" + id);
        log.info("插入了 " + successInsert + "条kbqa记录");

        //给前端返回这次的sessionId，和这次问答对的id
        pythonResponse.putOpt("sessionId", kbQARecordDto.getSessionId());
        pythonResponse.putOpt("id", kbQARecordDto.getId());
        return pythonResponse;
    }

    private ResponseEntity<String> getStringResponseEntity(JSONObject params, KbQARecordDto kbQARecordDto) {
        JSONObject pythonParams = params;
        System.out.println(pythonParams);

        pythonParams.putOpt("approach", params.getStr("approach"));
        pythonParams.putOpt("top", params.getLong("top"));
        pythonParams.putOpt("search", params.getStr("search"));
        pythonParams.putOpt("trainType", params.getStr("trainType"));
        pythonParams.putOpt("temperature", params.getFloat("temperature"));
        pythonParams.putOpt("config", kbQARecordService.getKbQAConfig());
        if (params.getStr("inverter") != null) {
            pythonParams.putOpt("inverter", params.getStr("inverter"));
        } else if (params.getStr("power") != null) {
            pythonParams.putOpt("power", params.getStr("power"));
        }
        pythonParams.putOpt("regionId", roleRegionService.getRegionId(params.getStr("region")));
        pythonParams.putOpt("roleId", roleRegionService.getRoleId(params.getStr("role")));

        Optional.ofNullable(params.getStr("language")).ifPresent(value -> pythonParams.set("language", value));


        log.info("*******************pythonParams是*******" + pythonParams);

        //获得知识库配置
        System.out.println("加了config，approach，top，search，trainType，temperature" + pythonParams);


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(pythonParams, headers);
        //修改
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(PYTHON_KBQA, entity, String.class);
        return stringResponseEntity;
    }

    /**
     * 新建user
     */
    private void insertUser(JSONObject params, Long deptId) {
        if (iSysUserService.selectUserById(params.getLong("userId")) == null) {
            //如果没有这个user就新建一个user
            SysUser sysUser = new SysUser();
            sysUser.setRoleIds(new Long[]{roleRegionService.getRoleId(params.getStr("role"))});
            sysUser.setUserId(params.getLong("userId"));
            sysUser.setDeptId(deptId);
            //默认是token部门
            sysUser.setUserName(String.valueOf(params.getLong("userId")));
            sysUser.setNickName(String.valueOf(params.getLong("userId")));
            iSysUserService.insertUser(sysUser);
            //这里插入了
        }
    }

    /**
     * 更新剩余最大消费额度
     *
     * @param stringResponseEntity 调用GPT接口返回的结果
     */
    private void updateMaxConsumption(ResponseEntity<String> stringResponseEntity) {
        JSONObject body = JSONUtil.parseObj(stringResponseEntity.getBody());
        if ("200".equals(body.get("code").toString())) {
            String totalTokens = body.getJSONObject("usage").getStr("total_tokens");
            String model = body.getStr("model");
            String unitPrice = redisCache.getCacheObject(model);
            BigDecimal multiply = new BigDecimal(unitPrice).multiply(new BigDecimal(totalTokens));
            String availableBalance = redisCache.getCacheObject(this.availableBalancePrefix + SecurityUtils.getUserId());
            BigDecimal subtract = new BigDecimal(availableBalance).subtract(multiply);
            redisCache.setCacheObject(this.availableBalancePrefix + SecurityUtils.getUserId(), subtract.toString());
        }
    }

    /**
     * 检查是否超过最大消费额度 -1:超过 1:未超过
     *
     * @return -1:超过 1:未超过
     */
    private int checkMaxConsumptionAmount() {
        // admin用户不限制
        if ("admin".equals(SecurityUtils.getUsername())) {
            return 1;
        }
        Long userId = SecurityUtils.getLoginUser().getUserId();
        String availableBalance = redisCache.getCacheObject(this.availableBalancePrefix + userId);
        if (new BigDecimal(availableBalance).signum() <= 0) {
            return -1;
        }
        return 1;
    }

    /**
     * 插入数据
     *
     * @param question
     * @param answer
     * @param model
     * @return
     */
    public void insertQARecord(JSONObject question, JSONObject answer, String model) {
        ChatgptQARecord chatgptQARecord = new ChatgptQARecord();
        chatgptQARecord.setDeptId(SecurityUtils.getDeptId());
        chatgptQARecord.setUserId(SecurityUtils.getUserId());
        chatgptQARecord.setModel(model);
        chatgptQARecord.setQuestion(question.getJSONArray("question").getJSONObject(question.getJSONArray("question").size() - 1).getStr("content"));
        if ("421".equals(answer.get("code").toString()) || "422".equals(answer.get("code").toString()) || "430".equals(answer.get("code").toString()) || "438".equals(answer.get("code").toString())
                || "431".equals(answer.get("code").toString()) || "432".equals(answer.get("code").toString())) {
            chatgptQARecord.setTokenUsage(0);
            chatgptQARecord.setAnswer(answer.getStr("msg"));
        } else {
            chatgptQARecord.setQuestion(answer.getJSONArray("question").getJSONObject(answer.getJSONArray("question").size() - 1).getStr("content"));
            chatgptQARecord.setModel(model);
            chatgptQARecord.setTokenUsage(answer.getJSONObject("usage").getInt("total_tokens"));
            chatgptQARecord.setAnswer(answer.getJSONArray("choices").getJSONObject(answer.getJSONArray("choices").size() - 1).getJSONObject("message").getStr("content"));
        }
        iChatgptQARecordService.insertChatgptQARecord(chatgptQARecord);
    }

    public void insertError(ThirdPartyError thirdPartyError) {
        iChatgptQARecordService.insertError(thirdPartyError);
    }

    /**
     * 获取今日剩余秒数
     *
     * @return 今日剩余秒数
     */
    public Integer getTodayRemainingSeconds() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        long remainingSeconds = Duration.between(now, endOfDay).getSeconds();
        return (int) remainingSeconds;
    }

}
