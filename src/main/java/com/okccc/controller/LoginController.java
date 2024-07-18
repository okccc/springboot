package com.okccc.controller;

import com.okccc.bean.UserInfo;
import com.okccc.listener.MyEventPublisher;
import com.okccc.listener.LoginSuccessEvent;
import com.okccc.service.AccountService;
import com.okccc.service.CouponService;
import com.okccc.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:09:53
 * @Desc: SpringBoot事件驱动开发
 */
@RestController
public class LoginController {

    // 常规方式：注入一堆Service
    // 弊端：每次新增功能都要改代码,设计模式开闭原则是对新增开放对修改关闭
    @Autowired
    private AccountService accountService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private SysService sysService;

    @GetMapping(value = "login01")
    public String login01(@RequestParam("username") String username, @RequestParam("password") String password) {
        // 处理登录业务
        System.out.println("login...");

        // 1.账户服务添加积分
        accountService.addScore(username);

        // 2.优惠券服务发放优惠券
        couponService.sendCoupon(username);

        // 3.系统服务登记用户登录信息
        sysService.recordLog(username);

        return username + " login success";
    }

    // 解耦方式：使用事件触发机制,只需注入一个事件发布器
    @Autowired
    private MyEventPublisher myEventPublisher;

    @GetMapping(value = "login02")
    public String login02(@RequestParam("username") String username, @RequestParam("password") String password) {
        // 处理登录业务
        System.out.println("login...");

        // 发送事件：需要一个事件发布者来发布事件,让下游订阅即可
        // 1.准备事件
        LoginSuccessEvent event = new LoginSuccessEvent(new UserInfo(username, password));
        // 2.发布事件
        myEventPublisher.sendEvent(event);

        return username + " login success";
    }

}
