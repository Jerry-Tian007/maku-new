package com.ruoyi.gpt.service.impl.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.constant.PermissionConstants;
import com.ruoyi.common.core.domain.entity.SysRegion;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.gpt.configuration.ChatCompletionRequest;
import com.ruoyi.gpt.model.domain.KbQARecord;
import com.ruoyi.gpt.model.dto.kb_q_a_record.KbQARecordDto;
import com.ruoyi.gpt.service.impl.KbQARecordServiceImpl;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.impl.SysRegionServiceImpl;
import com.ruoyi.system.service.impl.SysRoleServiceImpl;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Component
@Slf4j
//@ServerEndpoint(value = "/websocket/{sessionId}", configurator = WebSocketConfig.class)
// 接口路径 ws://localhost:8888/websocket/{sessionId};
public class MyWebSocketUtils {


    public static RedisCache redisCache;

    public static TokenServiceWs tokenServiceWs;

    public static SysUserServiceImpl iSysUserService;

    public static ISysRoleService iSysRoleService;

    public static RoleRegionServiceImpl roleRegionService;

    public static KbQARecordServiceImpl kbQARecordService;

    public static SysRoleServiceImpl sysRoleService;

    public static SysRegionServiceImpl sysRegionService;

    public static ConcurrentHashMap<String, LoginUser> getSessionToken = new ConcurrentHashMap<>();

    private static final String PERMISSION_DELIMETER = ",";

    private static final String ALL_PERMISSION = "*:*:*";

    private final String PYTHON_KBQA = "http://127.0.0.1:5000/chat";
    private final String ERROR_DEFAULT = "抱歉，我不能准确理解您的问题，请尝试重新提问。";
    private final String ERROR_520014 = "请稍等，我正在努力加载，请一分钟后再重新尝试。";
    private final String ERROR_5200256 = "抱歉，我不能准确理解您的问题，请尝试重新提问。";
    private final String ERROR_52003 = "您好，提问内容涉及敏感信息，请重新提问。";

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        MyWebSocketUtils.redisCache = redisCache;
    }

    @Autowired
    public void setTokenServiceWs(TokenServiceWs tokenServiceWs) {
        MyWebSocketUtils.tokenServiceWs = tokenServiceWs;
    }

    @Autowired
    public void setSysUserService(SysUserServiceImpl iSysUserService) {
        MyWebSocketUtils.iSysUserService = iSysUserService;
    }

    @Autowired
    public void setISysRoleService(SysRoleServiceImpl iSysRoleService) {
        MyWebSocketUtils.iSysRoleService = iSysRoleService;
    }

    @Autowired
    public void setRoleRegionService(RoleRegionServiceImpl roleRegionService) {
        MyWebSocketUtils.roleRegionService = roleRegionService;
    }

    @Autowired
    public void setKbQARecordService(KbQARecordServiceImpl kbQARecordServiceImpl) {
        MyWebSocketUtils.kbQARecordService = kbQARecordServiceImpl;
    }

    @Autowired
    public void setSysRoleService(ISysRoleService sysRoleService) {
        MyWebSocketUtils.sysRoleService = (SysRoleServiceImpl) sysRoleService;
    }

    @Autowired
    public void setSysRegionService(SysRegionServiceImpl sysRegionService) {
        MyWebSocketUtils.sysRegionService = sysRegionService;
    }


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String sessionId;

    public String getSessionId() {
        return this.sessionId;
    }

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static CopyOnWriteArraySet<MyWebSocketUtils> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接数
    private static Map<String, Session> sessionPool = new HashMap<String, Session>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) {
        log.info("client received message:{}, session:{},", sessionId, session.hashCode());
        final String token = getHeader(session, "token");
        log.info("client received token:{}", token);
        if (token == null) {
            log.error("Authorization failed, token is null");
            JSONObject errorJSON = JSONUtil.createObj().putOpt("event", "error").putOpt("sessionId", sessionId).putOpt("data", "令牌为空或错误，资源访问失败");
            try {
                session.getBasicRemote().sendText(String.valueOf(errorJSON));
            } catch (IOException e) {
                log.error("发送消息失败", e);
            }
            closeWebSocketSession(session);
        } else if (!StringUtils.isEmpty(token)) {
            LoginUser loginUserWs = tokenServiceWs.getLoginUserWs(token);
            if (loginUserWs == null) {
                log.error("Token已失效，请联系管理员");
                JSONObject errorJSON = JSONUtil.createObj().putOpt("event", "error").putOpt("sessionId", sessionId).putOpt("data", "Token已经失效，请联系管理员");
                try {
                    session.getBasicRemote().sendText(String.valueOf(errorJSON));
                } catch (IOException e) {
                    log.error("发送消息失败", e);
                }
                try {
                    session.close();
                } catch (IOException e) {
                    log.error("关闭websocket连接异常", e);
                }
                closeWebSocketSession(session);
            } else if (!hasAnyPermi(PermissionConstants.GPT_3_5_ROLE, loginUserWs)) {
                log.error("用户{}没有此权限", loginUserWs.getUsername());
                JSONObject errorJSON = JSONUtil.createObj().putOpt("event", "error").putOpt("sessionId", sessionId).putOpt("data", "用户" + loginUserWs.getUsername() + "没有权限，请联系管理员");

                try {
                    session.getBasicRemote().sendText(String.valueOf(errorJSON));
                } catch (IOException e) {
                    log.error("发送消息失败", e);
                }
                closeWebSocketSession(session);

            } else {
                getSessionToken.put(sessionId, loginUserWs);
//                log.info("开始设置loginUser信息");
//                redisCache.setCacheObject(sessionId, loginUserWs, 2, TimeUnit.HOURS);
//                log.info("设置loginUser结束");
                this.session = session;
                this.sessionId = sessionId;
                webSockets.add(this);
                sessionPool.put(sessionId, session);
//                log.info("开始设置session信息");
//                wsSessionManager.saveSession(sessionId, session, 2, TimeUnit.HOURS);
//                log.info("设置session结束");
                log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
            }
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
            log.error("websocket关闭异常： ", e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sessionId") String sessionId) {
//        Session session = redisCache.getCacheObject(sessionId);

        if (session == null) {
            extractedSendError(sessionId, "session无效");
            return;
        }
        ChatCompletionRequest params = null;
        try {
            params = JSONUtil.toBean(message, ChatCompletionRequest.class);
        } catch (Exception e) {
            extractedSendError(sessionId, "请求参数错误");
            log.error("请求参数错误", e);
            try {
                session.close();
            } catch (IOException ex) {
                log.error("关闭websocket连接异常", e);
            }
        }

        if (checkChatRequest(params, sessionId)) {
            ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

            try {
                params.setRegionId(roleRegionService.getRegionId(params.getRegion()));
                params.setRoleId(roleRegionService.getRoleId(params.getRole()));
                params.setKbQARecordService(kbQARecordService);

                LoginUser loginUser = getSessionToken.get(sessionId);
//                LoginUser loginUser = redisCache.getCacheObject(sessionId);
//                redisCache.setCacheObject(sessionId, loginUser, 2, TimeUnit.HOURS);
                /**插入用户*/
                insertUser(params, loginUser.getDeptId());

                /** 新增kbqa记录*/
                Long kbqaId = addKbqa(sessionId, params, loginUser);
                /** 推送kbqaId*/
                JSONObject kbIdObj = new JSONObject();
                kbIdObj.put("event", "id");
                kbIdObj.put("sessionId", params.getSessionId());
                kbIdObj.put("data", kbqaId);
                sendOneMessage(params.getSessionId(), String.valueOf(kbIdObj));

                log.info("param is" + params);

                HttpURLConnection connection = extractedURL(PYTHON_KBQA, params);
                Callable<String> task = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        return reader.readLine();
                    }
                };

                String result = "";

                boolean startStatus = false;
                Future<String> future = threadPoolExecutor.submit(task);
                try {
                    log.info("start first");
                    result = future.get(15, TimeUnit.SECONDS);
                    log.info("end first:" + result);
                } catch (TimeoutException ex) {
                    log.info("first fail");
                    startStatus = true;
                    String dateTimeNow = DateUtils.dateTimeNow("YYYY-MM-dd HH:mm:ss");
                    String startMessages = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"<start>\", 'time': '" + dateTimeNow + "'}";
                    sendOneMessage(params.getSessionId(), startMessages);
                    String messages = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"我正在努力思考，请再给我一点时间\", 'time': '" + dateTimeNow + "'}";
                    sendOneMessage(params.getSessionId(), messages);
                }

                try {
                    result = future.get(30, TimeUnit.SECONDS);
                    log.info("second:" + result);
                } catch (TimeoutException ex) {
                    String dateTimeNow = DateUtils.dateTimeNow("YYYY-MM-dd HH:mm:ss");
                    String messages = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"我想不出答案，请重新提问吧\", 'time': '" + dateTimeNow + "'}";
                    sendOneMessage(params.getSessionId(), messages);
                    String endMessage = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"<end>\", 'time': '" + dateTimeNow + "'}";
                    sendOneMessage(params.getSessionId(), endMessage);
                    log.info("second fail");
                    future.cancel(true);
                    // 更新KbqaRecord
                    extractedKbqaRecord(kbqaId, "我想不出答案，请重新提问吧");
                    dateTimeNow = DateUtils.dateTimeNow("YYYY-MM-dd HH:mm:ss");
                    String noDataMessage = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"no data\", 'time': '" + dateTimeNow + "'}";
                    extractedUpdateKbqaRecord(noDataMessage, kbqaId);
                }

                String line = null;

                if (StringUtils.isEmpty(result)) {
                    log.info("empty:" + result);
                    return;
                } else {
                    line = result;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (!StringUtils.isEmpty(line)) {
                    log.info("while:" + line);
                    if ("vector".equals(JSONUtil.parseObj(line).getStr("event"))) {
                        JSONObject entries = JSONUtil.parseObj(line);
                        extractedKbqaRecordVector(kbqaId, entries.getStr("data"));
                    } else if ("finish".equals(JSONUtil.parseObj(line).getStr("event"))) {
                        sendOneMessage(params.getSessionId(), String.valueOf(JSONUtil.parseObj(line)));
//                        closeConnection(reader, connection);
                        connection.disconnect();
                        return;
                    } else if ("add".equals(JSONUtil.parseObj(line).getStr("event"))) {

                        JSONObject entries = JSONUtil.parseObj(line);

                        try {
                            log.info("add is " + entries);

                            // 更新KbqaRecord
                            extractedKbqaRecord(kbqaId, entries.getStr("data"));

                            /** 如果add event end 结尾*/
                            if (entries.getStr("data").endsWith("<end>")) {
                                try {
                                    // 更新chatTitle
                                    extractedChatTitle(params);
                                } catch (Exception e) {
                                    log.error("推送数据异常", e);
                                }
                            }
                            if (entries.getStr("data").endsWith("<start>") && startStatus) {
                                line = reader.readLine();
                                continue;
                            }
                            sendOneMessage(params.getSessionId(), String.valueOf(entries));

                        } catch (Exception e) {
                            // 这里你可以选择记录异常，或者根据需要进行其他处理。
                            log.error("推送数据异常", e);
                        }
                        log.info(line);
                    } else if ("question".equals(JSONUtil.parseObj(line).getStr("event"))) {
                        //发送猜你想问
                        sendOneMessage(params.getSessionId(), JSONUtil.parseObj(line).toString());
                    } else if ("reference".equals(JSONUtil.parseObj(line).getStr("event"))) {
                        // 通过kbqaRecordId 更新问答文件引用
                        extractedUpdateKbqaRecord(line, kbqaId);
                    } else if ("error".equals(JSONUtil.parseObj(line).getStr("event"))) {
                        sendOneMessage(params.getSessionId(), JSONUtil.parseObj(line).toString());
                        String dateTimeNow = DateUtils.dateTimeNow("YYYY-MM-dd HH:mm:ss");
                        String endMessage = "{'event': 'add', 'sessionId': '" + params.getSessionId() + "', 'data': \"<end>\", 'time': '" + dateTimeNow + "'}";
                        sendOneMessage(params.getSessionId(), endMessage);
                        switch (JSONUtil.parseObj(line).getStr("data")) {
                            case "52001":
                            case "52004":
                                updateKbqa(kbqaId, ERROR_520014);
                                break;
                            case "52002":
                            case "52005":
                            case "52006":
                                updateKbqa(kbqaId, ERROR_5200256);
                                break;
                            case "52003":
                                updateKbqa(kbqaId, ERROR_52003);
                                break;
                            default:
                                break;
                        }
                    } else {
//                        closeConnection(reader, connection);
                        connection.disconnect();
                        return;
                    }
                    line = reader.readLine();
                }
//                closeConnection(reader, connection);
                connection.disconnect();
            } catch (Exception e) {
                log.error("处理消息出错", e);
                JSONObject msg = null;
                if (e instanceof ConnectException) {
                    msg = JSONUtil.createObj().putOpt("event", "error").putOpt("sessionId", sessionId).putOpt("data", "连接服务器失败，请联系管理员");
                } else {
                    msg = JSONUtil.createObj().putOpt("event", "error").putOpt("sessionId", sessionId).putOpt("data", "处理消息出错, 请联系管理员");
                }
                sendOneMessage(sessionId, String.valueOf(msg));
                closeWebSocketSession(session);
            } finally {
                threadPoolExecutor.shutdown();
            }
        }
    }

    private void updateKbqa(Long kbqaId, String answer) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setAnswer(answer);
        kbQARecordDto.setId(kbqaId);
        kbQARecordService.updateKbQAAnswer(kbQARecordDto);
    }

    private Long addKbqa(String sessionId, ChatCompletionRequest params, LoginUser loginUser) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setQuestion(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
        kbQARecordDto.setCreateTime(DateUtils.getNowDate());
        kbQARecordDto.setSessionId(sessionId);
        kbQARecordDto.setUserId(iSysUserService.selectUserIdByUserName(params.getUserId()));
        kbQARecordDto.setDeptId(loginUser.getDeptId());
        kbQARecordDto.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
        kbQARecordDto.setCreateBy(String.valueOf(params.getUserId()));
        kbQARecordDto.setAnswer(ERROR_DEFAULT);

        return kbQARecordService.insertKbQARecordKbqa(kbQARecordDto);
    }

    private void extractedSendError(String sessionId, String message) {
        JSONObject errorJSON = new JSONObject();
        errorJSON.putOpt("event", "error");
        errorJSON.putOpt("sessionId", sessionId);
        errorJSON.putOpt("data", message);
        sendOneMessage(sessionId, errorJSON.toString());

        try {
//            Session session = redisCache.getCacheObject(sessionId);
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            log.error("关闭websocket连接异常", e);
        }
    }

    private static void closeConnection(BufferedReader reader, HttpURLConnection connection) {
        try {
            reader.close();
        } catch (IOException e) {
            log.error("关闭BufferedReader异常", e);
        }
        connection.disconnect();
    }

    @NotNull
    private static HttpURLConnection extractedURL(String url, ChatCompletionRequest params) {
        URL apiUrl = null;
        try {
            apiUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error("", e);

        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) apiUrl.openConnection();
        } catch (IOException e) {
            log.error("", e);

        }
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            log.error("", e);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(10);
        try {
            connection.getOutputStream().write(params.toString().getBytes());
        } catch (IOException e) {
            log.error("", e);
        }
        return connection;
    }

    private static void extractedUpdateKbqaRecord(String line, Long kbqaId) {
        JSONObject entries = JSONUtil.parseObj(line);
        if (kbqaId != null) {
            KbQARecordDto kbQARecordDto = kbQARecordService.selectKbQARecordById(kbqaId);
            kbQARecordDto.setResponse(entries.getStr("data"));
            kbQARecordService.updateKbQARecord(kbQARecordDto);
        }
    }

    /**
     * 插入kbqa
     */
    private void extractedKbqaRecord(Long kbqaId, String pythonAnswer) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setId(kbqaId);
        if ("<end>".equals(pythonAnswer)) {
            return;
        }
        kbQARecordDto.setAnswer(Optional.of(pythonAnswer.replaceAll("^<start>|<end>$", "")).orElse("回答出错请重试"));
        kbQARecordService.updateKbQARecord(kbQARecordDto);
    }


    private void extractedKbqaRecordVector(Long kbqaId, String questionVector) {
        KbQARecordDto kbQARecordDto = new KbQARecordDto();
        kbQARecordDto.setId(kbqaId);
        kbQARecordDto.setQuestionVector(questionVector);
        kbQARecordService.updateKbQARecord(kbQARecordDto);
    }


    /**
     * 更新chatTitle
     */
    private void extractedChatTitle(ChatCompletionRequest params) {
        List<KbQARecordDto> kbQARecordDtos = kbQARecordService.selectKbQARecordBySessionId(params.getSessionId());
        List<Long> idList = kbQARecordDtos.stream().map(KbQARecord::getId).collect(Collectors.toList());

        KbQARecordDto kbqaRecordTemp = new KbQARecordDto();
        kbqaRecordTemp.setChatTitle(params.getHistory().getJSONObject(params.getHistory().size() - 1).getStr("user"));
        idList.forEach(id1 -> {
            kbqaRecordTemp.setId(id1);
            kbQARecordService.updateKbQARecord(kbqaRecordTemp);
        });
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("出现异常，堆栈信息:", error.fillInStackTrace());
    }

    // 此为单点消息
    public void sendOneMessage(String sessionId, String message) {
        Session session = sessionPool.get(sessionId);
//        Session session = redisCache.getCacheObject(sessionId);
//        redisCache.setCacheObject(sessionId, session, 2, TimeUnit.HOURS);
        if (session != null && session.isOpen()) {
            try {
                log.info("message is " + message);
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息失败", e);
            }
        } else {
            log.error("发送消息失败，sessionId不匹配");
        }
    }

    public static String getHeader(Session session, String headerName) {
        final String header = (String) session.getUserProperties().get(headerName);
        if (StrUtil.isBlank(header)) {
            log.error("获取header失败");
        }
        return header;
    }

    private void closeWebSocketSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭websocket连接异常", e);
        }
    }


    /** 获取指定loginUser是否有某项权限*/
    /**
     * 获取指定loginUser是否有某项权限
     */

    public boolean hasAnyPermi(String permissions, LoginUser loginUser) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

    private void insertUser(ChatCompletionRequest params, Long deptId) {

        //这里的userId是他的userName
        if (iSysUserService.selectUserByUserName(params.getUserId()) == null) {
            //如果没有这个user就新建一个user

            SysUser sysUser = new SysUser();
            if (params.getRole().equals("超级管理员")) {
                params.setRole("外部用户");
            }
            sysUser.setRoleIds(new Long[]{roleRegionService.getRoleId(params.getRole())});
            sysUser.setDeptId(deptId);
            //默认是token部门
            sysUser.setUserName(String.valueOf(params.getUserId()));
            sysUser.setNickName(String.valueOf(params.getUserId()));
            iSysUserService.insertUser(sysUser);
            //这里插入了
        }
    }

    private boolean checkChatRequest(ChatCompletionRequest params, String sessionId) {

        //这里检查region和role
        if (params == null) {
            extractedSendError(sessionId, "请求参数不能为空");
            return false;
        }
        if (params.getUserId() == null) {
            extractedSendError(sessionId, "用户ID不能为空");
            return false;

        }
        if (params.getHistory() == null) {
            extractedSendError(sessionId, "问答内容不能为空");
            return false;

        }
        if (params.getSessionId() == null) {
            extractedSendError(sessionId, "会话ID不能为空");
            return false;
        }
        if (!isValidUUID(params.getSessionId())) {
            extractedSendError(sessionId, "会话ID非法");
            return false;
        }
        if (params.getRegion() == null) {
            extractedSendError(sessionId, "地区不能为空");
            return false;
        }
        if (params.getRole() == null) {
            extractedSendError(sessionId, "角色不能为空");
            return false;
        }
        if (params.getNeedHidden() == null) {
            extractedSendError(sessionId, "猜你想问设置是否隐藏不能为空");
            return false;
        }

        SysRegion sysRegion = new SysRegion();
        sysRegion.setRegion(params.getRegion());
        List<SysRegion> sysRegions1 = sysRegionService.selectSysRegionList(sysRegion);
        if (sysRegions1.isEmpty()) {
            log.info("地区错误");
            extractedSendError(sessionId, "地区不存在");
            return false;
        }

        List<SysRole> sysRoles = sysRoleService.selectRoleListWithoutAdmin(null);
        boolean containsRole = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList()).contains(params.getRole());
        if (!containsRole) {
            log.info("角色错误");
            extractedSendError(sessionId, "角色不存在");
            return false;
        }

        return true;
    }

    private boolean isValidUUID(String id) {
        if (id.length() != 36) {
            return false;
        }
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

