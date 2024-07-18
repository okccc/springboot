package com.okccc.service;

import com.okccc.bean.UserInfo;
import com.okccc.listener.LoginSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:16:31
 * @Desc:
 */
@Service
public class SysService {

    public void recordLog(String username) {
        System.out.println(username + " 登录信息已被记录");
    }

    @EventListener
    public void hehe(LoginSuccessEvent event) {
        System.out.println("========== SysService 监听到事件 ========== " + event);
        UserInfo source = (UserInfo) event.getSource();
        recordLog(source.getUsername());
    }

}
