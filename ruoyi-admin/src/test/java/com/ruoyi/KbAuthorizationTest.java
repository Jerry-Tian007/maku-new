package com.ruoyi;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.gpt.configuration.RabbitmqProducer;
import com.ruoyi.gpt.service.*;
import com.ruoyi.gpt.service.impl.FileCommonServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KbAuthorizationTest {

    @Autowired
    private IKbAuthorizationService iKbAuthorizationService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void thirdPartyLoginTest() {
        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            ServletRequestAttributes attributes = new ServletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(attributes);
            String token = sysLoginService.login("tiankk", "123456", "", "");
            Claims claims = tokenService.parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = tokenService.getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            tokenService.verifyToken(user);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            System.out.println("模拟登录成功！");
        } catch (Exception ex) {

        }
    }
}
