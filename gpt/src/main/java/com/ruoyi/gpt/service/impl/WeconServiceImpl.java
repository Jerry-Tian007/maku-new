package com.ruoyi.gpt.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.gpt.service.IWeconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Liujr
 */
@Slf4j
@Service
public class WeconServiceImpl implements IWeconService {

    private static final String WX_GET_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpSecret}";

    private static final String WX_GET_USER_ID_URL = "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token={accessToken}&code={code}";

    private static final String WECON_ACCESS_TOKEN = "weCon:accessToken";

//    @Value("${weCon.corpId}")
    private String corpId;

//    @Value("${weCon.corpSecret}")
    private String corpSecret;

    @Resource
    private RedisCache redisCache;


    @Override
    public String getUserIdByCode(String code) {
        String weConAccessToken = IsEffectiveWeConAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> userInfoResponse = restTemplate.getForEntity(WX_GET_USER_ID_URL, String.class, weConAccessToken, code);
        JSONObject userInfo = new JSONObject(userInfoResponse.getBody());
        if(userInfo.getInt("errcode")>200){
            log.info("获取微信用户ID失败");
        }
        log.info("userId:"+userInfo.getInt("userId"));
        return userInfo.getStr("userid");
    }

    public String getWeConAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(WX_GET_TOKEN_URL, String.class,
                corpId, corpSecret);
        JSONObject entries = new JSONObject(response.getBody());
        if(entries.getInt("errcode")>200){
            log.info("获取token失败");
        }
        String accessToken = entries.getStr("access_token");
        Integer expiresIn = entries.getInt("expires_in"); //获取token的有效期
        log.info("Access token: " + accessToken);
        redisCache.setCacheObject(WECON_ACCESS_TOKEN, accessToken, expiresIn, TimeUnit.SECONDS); // 将token和有效期存到redis
        return accessToken;
    }

    public String IsEffectiveWeConAccessToken() {
        String weConAccessToken;
        if (redisCache.getCacheObject(WECON_ACCESS_TOKEN) == null) {
            weConAccessToken = getWeConAccessToken();
        } else {
            weConAccessToken = redisCache.getCacheObject(WECON_ACCESS_TOKEN);
        }
        return weConAccessToken;
    }
}

