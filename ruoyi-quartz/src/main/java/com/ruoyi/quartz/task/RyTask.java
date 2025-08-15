package com.ruoyi.quartz.task;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.gpt.service.impl.ModelTokenConsumptionOverviewServiceImpl;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Resource
    private ModelTokenConsumptionOverviewServiceImpl modelTokenConsumptionOverviewService;

    @Resource
    private ISysUserService iSysUserService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /**
     * 月末重置redis余额与 user total-cost一下
     */
    public void resetMaximumLimitJob() {
        modelTokenConsumptionOverviewService.resetMaximumLimitJob();
    }

    public void checkUserExpiretime(){
        List<SysUser> sysUsers = iSysUserService.selectUserListUsedTask();
        StringBuilder stringBuilder = new StringBuilder();
        for (SysUser item : sysUsers){
            item.setStatus("1");
            iSysUserService.updateUserStatus(item);
            stringBuilder.append(item.getUserName()).append(":").append("停用").append(" ");
        }
    }

}
