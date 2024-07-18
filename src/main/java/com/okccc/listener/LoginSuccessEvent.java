package com.okccc.listener;

import com.okccc.bean.UserInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:32:32
 * @Desc: 登录成功事件,推荐所有事件都继承ApplicationEvent抽象类
 */
public class LoginSuccessEvent extends ApplicationEvent {

    public LoginSuccessEvent(UserInfo source) {
        super(source);
    }

}
