package com.ruoyi.gpt.core.aspect;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ExceptionStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.gpt.model.domain.ModelResourceClient;
import com.ruoyi.gpt.service.IModelResourceClientService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Aspect
@Component
public class ModelResourceAuthenticationAspect {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IModelResourceClientService iModelResourceClientService;


    @Pointcut(value = "@annotation(com.ruoyi.gpt.core.annotation.ModelResourcesAuthentication)")
    public void authentication(){
    }

    @Before("authentication()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String remoteAddr = request.getRemoteAddr();
        if (redisCache.getCacheObject("model-client:" +remoteAddr) != null){
            throw new ServiceException(ExceptionStatusEnum.PARAMETER_ANOMALY_FORBIDDEN);
        }
        String client = request.getHeader("client");
        if (client == null || client.isEmpty()){
            redisCache.setCacheObject("model-client:" + remoteAddr,"ANOMALY",60, SECONDS);
            throw new ServiceException(ExceptionStatusEnum.PARAMETER_ANOMALY);
        }
        Set<String> clientKeys = redisCache.getCacheSet("clientKeys");
        if (clientKeys.isEmpty()){
            clientKeys = iModelResourceClientService.selectModelResourceClientList();
            redisCache.setCacheSet("clientKeys",clientKeys);
        }
        if (!clientKeys.contains(client)){
            redisCache.setCacheObject(remoteAddr,"ANOMALY",60, SECONDS);
            throw new ServiceException(ExceptionStatusEnum.PERMISSIONS_ERROR);
        }

    }
    
}
