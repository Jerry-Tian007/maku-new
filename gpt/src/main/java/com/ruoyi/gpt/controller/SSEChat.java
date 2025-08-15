package com.ruoyi.gpt.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.gpt.configuration.*;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.service.IEmitService;
import com.ruoyi.gpt.service.impl.KbQARecordServiceImpl;
import com.ruoyi.gpt.service.impl.util.MyWebSocketUtils;
import com.ruoyi.gpt.service.impl.util.RoleRegionServiceImpl;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/ny")
public class SSEChat {

    private OpenAiSession openAiSession;

    @Resource
    private IEmitService iEmitService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private KbQARecordServiceImpl kbQARecordService;

    @Resource
    private RoleRegionServiceImpl roleRegionService;

    @Resource
    private MyWebSocketUtils myWebSocketUtils;

    @Resource
    private SysUserMapper iSysUserService;

    public static ConcurrentHashMap<String, StringBuffer> sessionAnswer = new ConcurrentHashMap<>();


    public void sse_OpenAiSessionFactory() {
        // 1. 配置文件
        OKConfiguration configuration = new OKConfiguration();
//        configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }


    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt40-32K')")
    @PostMapping("/gpt/kbqaAskSSEDeprecated")
    public void KbqaAskSSE(@RequestBody ChatCompletionRequest request, HttpServletResponse rp) throws JsonProcessingException, InterruptedException {
        // 入参；模型、请求信息
        sse_OpenAiSessionFactory();

//        sessionIdQ.put(request.getSessionId(), request.getHistory().getJSONObject(request.getHistory().size() - 1).getStr("user"));

        EventSourceChatListener eventSourceChatListener = new EventSourceChatListener(redisCache, kbQARecordService, iEmitService);

        eventSourceChatListener.setRequest(request, rp);
        // 请求

        openAiSession.completions(request, eventSourceChatListener);
        // 等待

//        eventSourceChatListener.getCountDownLatch().await();

//        return emit.get(request.getSessionId());
    }


    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt35')")
    @PostMapping(value = "/gpt/kbqaAskWS")
    public void getAnswer(@RequestBody ChatCompletionRequest params) {
        log.info("进来了，是ws传输");
        params.setRegionId(roleRegionService.getRegionId(params.getRegion()));
        params.setRoleId(roleRegionService.getRoleId(params.getRole()));
        params.setKbQARecordService(kbQARecordService);


        log.info("param is" + params.toString());
        String url = "http://127.0.0.1:5000/chat";
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(10);
            connection.getOutputStream().write(params.toString().getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            params.setRegionId(roleRegionService.getRegionId(params.getRegion()));
            params.setRoleId(roleRegionService.getRoleId(params.getRole()));
            while ((line = reader.readLine()) != null) {
                if (JSONUtil.parseObj(line).getStr("event").equals("finish")) {
                    try {
                        // 更新chatTitle
                        List<KbQARecordDto> kbQARecordDtos = kbQARecordService.selectKbQARecordBySessionId(params.getSessionId());
                        List<Long> idList = kbQARecordDtos.stream().map(KbQARecord::getId).collect(Collectors.toList());

                        KbQARecordDto kbqaRecordTemp = new KbQARecordDto();
                        kbqaRecordTemp.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        idList.forEach(id1 -> {
                            kbqaRecordTemp.setId(id1);
                            kbQARecordService.updateKbQARecord(kbqaRecordTemp);
                        });

                        insertUser(params, SecurityUtils.getDeptId());

                        // 推送数据
                        myWebSocketUtils.sendOneMessage(params.getSessionId(), line);


                        // 更新KbqaRecord
                        KbQARecordDto kbQARecordDto = new KbQARecordDto();
                        kbQARecordDto.setAnswer(Optional.ofNullable(sessionAnswer.get(params.getSessionId()))
                                .map(Object::toString)
                                .orElse("回答出错请重试"));
                        kbQARecordDto.setQuestion(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        kbQARecordDto.setSessionId(params.getSessionId());
                        kbQARecordDto.setUserId(iSysUserService.selectUserIdByUserName(params.getUserId()));
                        kbQARecordDto.setCreateTime(DateUtils.getNowDate());
                        kbQARecordDto.setDeptId(SecurityUtils.getDeptId());
                        kbQARecordDto.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        kbQARecordDto.setCreateBy(String.valueOf(params.getUserId()));

                        kbQARecordService.insertKbQARecordKbqa(kbQARecordDto);
                    } catch (Exception e) {
                        // 这里你可以选择记录异常，或者根据需要进行其他处理。
                        log.error("推送数据异常", e);
                        throw new RuntimeException("推送数据异常", e);
                    }
                    reader.close();
                    connection.disconnect();
                    return ;
//                    iEmitService.closeConn(params.getSessionId());
                } else if (JSONUtil.parseObj(line).getStr("event").equals("add")){
                    try {
                        JSONObject entries = JSONUtil.parseObj(line);
                        System.out.println("add is " + entries);
//                        iEmitService.send(params.getSessionId(), entries.getStr("event"), entries.getStr("data"));
                        myWebSocketUtils.sendOneMessage(params.getSessionId(), line);
                        if (sessionAnswer.containsKey(params.getSessionId())) {
                            sessionAnswer.get(params.getSessionId()).append(entries.getStr("data"));
                        } else {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(entries.getStr("data"));
                            sessionAnswer.put(params.getSessionId(), stringBuffer);
                        }
                    } catch (Exception e) {
                        // 这里你可以选择记录异常，或者根据需要进行其他处理。
                        log.error("推送数据异常", e);
                        throw new RuntimeException("推送数据异常", e);
                    }
                    System.out.println(line);
                } else {
                    reader.close();
                    connection.disconnect();
                    return ;
                }
            }

            reader.close();
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt35')")
    @PostMapping(value = "/gpt/kbqaAskSSE")
    public SseEmitter getAnswerAA(@RequestBody ChatCompletionRequest params) {

        log.info("进来了，是sse传输");

        params.setRegionId(roleRegionService.getRegionId(params.getRegion()));
        params.setRoleId(roleRegionService.getRoleId(params.getRole()));
        params.setKbQARecordService(kbQARecordService);

        log.info("param is" + params.toString());

        String url = "http://127.0.0.1:5000/chat";
        SseEmitter connTemp = null;
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(10);
            connection.getOutputStream().write(params.toString().getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            SseEmitter conn = iEmitService.getConn(params.getSessionId());
            connTemp = conn;


            while ((line = reader.readLine()) != null) {
                if (JSONUtil.parseObj(line).getStr("event").equals("finish")) {
                    try {
                        // 更新chatTitle
                        List<KbQARecordDto> kbQARecordDtos = kbQARecordService.selectKbQARecordBySessionId(params.getSessionId());
                        List<Long> idList = kbQARecordDtos.stream().map(KbQARecord::getId).collect(Collectors.toList());

                        KbQARecordDto kbqaRecordTemp = new KbQARecordDto();
                        kbqaRecordTemp.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        idList.forEach(id1 -> {
                            kbqaRecordTemp.setId(id1);
                            kbQARecordService.updateKbQARecord(kbqaRecordTemp);
                        });

                        insertUser(params, SecurityUtils.getDeptId());

                        // 推送数据
//                        myWebSocketUtils.sendOneMessage(params.getSessionId(), line);
                        iEmitService.send(params.getSessionId(),  JSONUtil.parseObj(line).getStr("event"), JSONUtil.parseObj(line).getStr("data"));


                        // 更新KbqaRecord
                        KbQARecordDto kbQARecordDto = new KbQARecordDto();
                        kbQARecordDto.setAnswer(Optional.ofNullable(sessionAnswer.get(params.getSessionId()))
                                .map(Object::toString)
                                .orElse("回答出错请重试"));
                        kbQARecordDto.setQuestion(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        kbQARecordDto.setSessionId(params.getSessionId());
                        kbQARecordDto.setUserId(iSysUserService.selectUserIdByUserName(params.getUserId()));
                        kbQARecordDto.setCreateTime(DateUtils.getNowDate());
                        kbQARecordDto.setDeptId(SecurityUtils.getDeptId());
                        kbQARecordDto.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
                        kbQARecordDto.setCreateBy(String.valueOf(params.getUserId()));

                        kbQARecordService.insertKbQARecordKbqa(kbQARecordDto);

                    } catch (Exception e) {
                        // 这里你可以选择记录异常，或者根据需要进行其他处理。
                        log.error("推送数据异常", e);
                        throw new RuntimeException("推送数据异常", e);
                    }
                    iEmitService.closeConn(params.getSessionId());
                    reader.close();
                    connection.disconnect();

                    return conn;
                } else if (JSONUtil.parseObj(line).getStr("event").equals("add")){
                    try {
                        JSONObject entries = JSONUtil.parseObj(line);
                        System.out.println("add is " + entries);
                        iEmitService.send(params.getSessionId(), entries.getStr("event"), entries.getStr("data"));
//                        myWebSocketUtils.sendOneMessage(params.getSessionId(), line);
                        if (sessionAnswer.containsKey(params.getSessionId())) {
                            sessionAnswer.get(params.getSessionId()).append(entries.getStr("data"));
                        } else {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(entries.getStr("data"));
                            sessionAnswer.put(params.getSessionId(), stringBuffer);
                        }
                        iEmitService.send(params.getSessionId(),  JSONUtil.parseObj(line).getStr("event"));
                    } catch (Exception e) {
                        // 这里你可以选择记录异常，或者根据需要进行其他处理。
                        log.error("推送数据异常", e);
                        throw new RuntimeException("推送数据异常", e);
                    }
                    System.out.println(line);
                } else {
                    iEmitService.closeConn(params.getSessionId());
                    reader.close();
                    connection.disconnect();
                    return conn;
                }
            }
            iEmitService.closeConn(params.getSessionId());
            reader.close();
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return connTemp;
    }

    private void insertUser(ChatCompletionRequest params, Long deptId) {

        if (iSysUserService.selectUserIdByUserName(params.getUserId()) == null) {
            //如果没有这个user就新建一个user
            SysUser sysUser = new SysUser();
            sysUser.setRoleIds(new Long[]{roleRegionService.getRoleId(params.getRole())});
//            sysUser.setUserId()
            sysUser.setDeptId(deptId);
            //默认是token部门
            sysUser.setUserName(String.valueOf(params.getUserId()));
            sysUser.setNickName(String.valueOf(params.getUserId()));
            iSysUserService.insertUser(sysUser);
            //这里插入了
        }
    }

//    @PreAuthorize("@ss.hasPermi('ny:thirdParty:gpt40-32K')")
//    @PostMapping(value = "/gpt/kbqaAskSSE", produces="text/event-stream;charset=utf-8")
//    public SseEmitter getAnswer(@RequestBody ChatCompletionRequest params) {
//        String url = "http://124.221.19.138:5000/chat";
//        SseEmitter emitterResult = null;
//        try {
//            URL apiUrl = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//            connection.setChunkedStreamingMode(10);
//            connection.getOutputStream().write(params.toString().getBytes());
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            final SseEmitter emitter = iEmitService.getConn(params.getSessionId());
//            emitterResult = emitter;
//
//            while ((line = reader.readLine()) != null) {
//
//
//            }
//        }
//    }

}
