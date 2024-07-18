package com.okccc.service;

import com.okccc.bean.UserInfo;
import com.okccc.listener.LoginSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:13:30
 * @Desc: 事件监听方式1：实现ApplicationListener<E extends ApplicationEvent>接口,监听事件
 *
 * 泛型传入ApplicationEvent表示当前关心所有事件
 * 泛型传入LoginSuccessEvent表示当前只关心登录成功事件
 */
@Order(1)
@Service
public class AccountService implements ApplicationListener<LoginSuccessEvent> {

    public AccountService() {
        System.out.println("调用构造器说明这个组件被造了");
    }

    public void addScore(String username) {
        System.out.println(username + " 新增一个积分");
    }

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        System.out.println("========== AccountService 监听到事件 ========== " + event);
        UserInfo source = (UserInfo) event.getSource();
        addScore(source.getUsername());
    }
}

//@Service
//public class AccountService {
//
//    public void addScore(String username) {
//        System.out.println(username + " 新增一个积分");
//    }
//
//}
