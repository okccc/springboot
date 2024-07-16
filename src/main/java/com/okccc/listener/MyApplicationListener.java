package com.okccc.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Author: okccc
 * @Date: 2024/1/27 16:58:55
 * @Desc: 自定义类实现ApplicationListener<E extends ApplicationEvent>接口,监听事件触发时机
 *
 * SpringApplication源码分析
 * Main - SpringApplication类1330行run() - 268行SpringApplication() - 282/283/284行getSpringFactoriesInstances()
 * 在项目启动前要依次读取BootstrapRegistryInitializer、ApplicationContextInitializer、ApplicationListener -
 * SpringFactoriesLoader类298行forDefaultResourceLocation() - FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories"
 *
 * spring-boot.jar的META-INF/spring.factories在43行配置了ApplicationListener接口的众多实现类
 * org.springframework.context.ApplicationListener=...
 *
 * 参考SpringBoot的写法在当前项目的META-INF/spring.factories配置自定义ApplicationListener
 * org.springframework.context.ApplicationListener=com.okccc.listener.MyApplicationListener
 *
 * BootstrapRegistryInitializer：感知特定阶段 - 引导初始化
 *   - 在META-INF/spring.factories配置
 *   - BootstrapRegistryInitializer反推 - SpringApplication源码346行initialize() - 300行createBootstrapContext()
 *   - 应用场景：项目启动前进行秘钥校对授权
 *
 * ApplicationContextInitializer：感知特定阶段 - IOC容器初始化
 *   - 在META-INF/spring.factories配置
 *   - ApplicationContextInitializer反推 - SpringApplication源码607行initialize() - 311行prepareContext()
 *
 * ApplicationListener：感知全阶段 - 感知事件触发,到什么阶段就干什么事,相当于AOP的普通通知
 *   - 在META-INF/spring.factories配置
 *   - @Bean或@EventListener事件驱动
 *
 * SpringApplicationRunListener：感知全阶段 - 感知生命周期,各阶段都能自定义操作,相当于AOP的环绕通知,功能更强大
 *   - 在META-INF/spring.factories配置
 *
 * ApplicationRunner/CommandLineRunner：感知特定阶段 - ready准备就绪
 * SpringApplication源码330行callRunners()
 *
 * 九大事件触发顺序(结合控制台输出日志和原理图分析)
 * 1.ApplicationStartingEvent - starting之前
 * 2.ApplicationEnvironmentPreparedEvent - environmentPrepared之前
 * 3.ApplicationContextInitializedEvent - contextPrepared之前
 * 4.ApplicationPreparedEvent - contextLoaded之前
 * 5.ApplicationStartedEvent - started之前
 * ========== 以下就开始插入了探针机制,对接K8S云平台时使用 ==========
 * SpringApplication源码318行started() - SpringApplicationRunListener83行started() - EventPublishingRunListener104行
 * 6.AvailabilityChangeEvent：LivenessState.CORRECT (存活探针)感知应用是否存活,可能是植物状态,活着但无法响应请求
 * 7.ApplicationReadyEvent - ready之前
 * SpringApplication源码331行ready() - SpringApplicationRunListener95行ready() - EventPublishingRunListener110行
 * 8.AvailabilityChangeEvent：ReadinessState.ACCEPTING_TRAFFIC (就绪探针)感知应用是否就绪,活着且能响应请求
 * 9.ApplicationFailedEvent - failed
 *
 * 最佳实战
 * 如果想在项目启动前做事：BootstrapRegistryInitializer、ApplicationContextInitializer
 * 如果想在项目启动后做事：ApplicationRunner、CommandLineRunner
 * 如果想干涉生命周期做事：SpringApplicationRunListener
 * 如果想在事件触发时做事：ApplicationListener
 */
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    /**
     * Handle an application event.
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event + " 事件到达");
    }

}
