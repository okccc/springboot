package com.okccc.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * @Author: okccc
 * @Date: 2024/1/27 17:28:28
 * @Desc: 自定义事件发布器实现ApplicationEventPublisherAware接口
 *
 * 应用启动过程生命周期事件感知(9大事件),应用运行过程事件感知(无数种)
 * 事件发布：ApplicationEventPublisherAware
 * 事件监听：方式1.实现ApplicationListener接口 方式2.给组件标识@EventListener
 */
@Service
public class MyEventPublisher implements ApplicationEventPublisherAware {

    // 底层发送事件的组件,SpringBoot会通过ApplicationEventPublisherAware接口自动注入
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 项目启动时会被自动调用,把真正发送事件的底层组件注入进来
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendEvent(ApplicationEvent event) {
        // 调用底层api发送事件,可以发送任意事件
        // 事件是广播出去的,所有监听该事件的监听器都能感知到,运行时发现自定义的MyApplicationListener已经监听到了
        // com.okccc.listener.LoginSuccessEvent[source=UserInfo(username=aaa, password=123)] 事件到达
        // 只要让那些Service也能监听到LoginSuccessEvent就行,事件监听有两种方式,详见AccountService和CouponService
        applicationEventPublisher.publishEvent(event);
    }

}
