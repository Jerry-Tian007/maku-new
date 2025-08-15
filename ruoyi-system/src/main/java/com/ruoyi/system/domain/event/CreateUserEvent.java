package com.ruoyi.system.domain.event;

import com.ruoyi.common.core.domain.entity.SysUser;
import org.springframework.context.ApplicationEvent;

/**
 * 用户创建事件 创建用户时触发初始化gpt账单
 */
public class CreateUserEvent extends ApplicationEvent {

    private SysUser user;

    public CreateUserEvent(Object source) {
        super(source);
    }

    public CreateUserEvent(Object source, SysUser user) {
        super(source);
        this.user = user;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }
}
