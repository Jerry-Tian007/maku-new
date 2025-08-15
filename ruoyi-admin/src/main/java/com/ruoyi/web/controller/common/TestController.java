package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.gpt.model.vo.GptBillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    public RedisCache redisCache;

    @GetMapping("getSetRedisCache")
    public void getSetRedisCache() {
        String redisKey = "test";
        SysUser sysUser = new SysUser();
        sysUser.setUserId(1111L);
        redisCache.setCacheObject(redisKey, sysUser, 5, TimeUnit.MINUTES);
        SysUser result1 = redisCache.getCacheObject(redisKey);
        result1.setUserName("userName");
        System.out.println(result1.getUserId() + ";" + result1.getUserName());
        redisCache.setCacheObject(redisKey, result1, 5, TimeUnit.MINUTES);
        SysUser result2 = redisCache.getCacheObject(redisKey);
        System.out.println(result2.getUserId() + ";" + result2.getUserName());
    }

    @GetMapping("test-bean-list")
    public R<List<GptBillVo>> testBeanList() {
        List<GptBillVo> re = new ArrayList<>();
        re.add(new GptBillVo());
        re.add(new GptBillVo());
        re.add(new GptBillVo());
        return R.success(re);
    }

    @GetMapping("test-bean")
    public R<GptBillVo> testBean() {
        return R.success(new GptBillVo());
    }

    @GetMapping("test-bool")
    public R<Boolean> testBool() {
        return R.success(true);
    }

    @GetMapping("test-integer")
    public R<Integer> testInteger() {
        return R.success(1);
    }


}
