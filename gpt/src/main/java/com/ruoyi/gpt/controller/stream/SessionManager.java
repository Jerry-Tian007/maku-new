package com.ruoyi.gpt.controller.stream;

import com.ruoyi.common.core.domain.model.LoginUser;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理器
 */
public class SessionManager {

    /**
     * 存储sessionId和会话的映射关系
     */
    private static final Map<String, Session> REQUEST_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 存储会话和sessionId的映射关系
     */
    private static final Map<String, String> SESSION_REQUESTS = new ConcurrentHashMap<>();

    /**
     * 存储sessionId和登录用户的映射关系
     */
    private static final Map<String, LoginUser> SESSION_LOGIN_USER = new ConcurrentHashMap<>();

    public static void saveSession(String requestId, Session session) {
        REQUEST_SESSIONS.put(requestId, session);
        SESSION_REQUESTS.put(session.getId(), requestId);
    }

    public static void saveSessionLoginUser(String sessionId, LoginUser loginUser) {
        SESSION_LOGIN_USER.put(sessionId, loginUser);
    }

    public static LoginUser getLoginUser(String sessionId) {
        return SESSION_LOGIN_USER.get(sessionId);
    }

    public static Session getSession(String requestId) {
        return REQUEST_SESSIONS.get(requestId);
    }

    public static void removeSession(Session session) {
        String requestId = SESSION_REQUESTS.get(session.getId());
        if (requestId != null) {
            REQUEST_SESSIONS.remove(requestId);
            SESSION_REQUESTS.remove(session.getId());
            SESSION_LOGIN_USER.remove(requestId);
        }
    }
}
