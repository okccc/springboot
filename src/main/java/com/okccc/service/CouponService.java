package com.okccc.service;

import com.okccc.bean.UserInfo;
import com.okccc.listener.LoginSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:15:51
 * @Desc: 事件监听方式2：给方法添加@EventListener注解
 */
@Service
public class CouponService {

    public void sendCoupon(String username) {
        System.out.println(username + " 随机得到了一张优惠券");
    }

    @Order(2)  // 指定事件的触发顺序,默认是按照类名的首字母排序
    @EventListener
    public void haha(LoginSuccessEvent event) {
        System.out.println("========== CouponService 监听到事件 ========== " + event);
        UserInfo source = (UserInfo) event.getSource();
        sendCoupon(source.getUsername());
    }

}
