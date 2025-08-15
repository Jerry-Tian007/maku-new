package com.ruoyi.gpt.core.aspect;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.RandomUtil;
import com.ruoyi.gpt.core.annotation.GenerateUniqueId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
public class GenerateUniqueIdAspect {

    @Autowired
    private SnowflakeGenerator snowflakeGenerator;

    @Pointcut(value = "@annotation(com.ruoyi.gpt.core.annotation.GenerateUniqueId)")
    public void annotationPointcut() {
    }

    @Before("annotationPointcut()")
    public void beforePointcut(JoinPoint joinPoint) throws Throwable {
        // 参数
        Object[] args = joinPoint.getArgs();

        // 获取注解上的target值
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        GenerateUniqueId generateUniqueId = signature.getMethod().getAnnotation(GenerateUniqueId.class);
        String filed = generateUniqueId.filed();
        int index = generateUniqueId.index();

        Class<?> aClass = args[index].getClass();
        Field field = aClass.getDeclaredField(filed);
        field.setAccessible(true);
        field.set(args[index], snowflakeGenerator.next());
    }
}
