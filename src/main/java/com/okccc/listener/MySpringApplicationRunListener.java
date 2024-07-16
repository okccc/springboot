package com.okccc.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @Author: okccc
 * @Date: 2024/1/27 10:52:42
 * @Desc: 自定义类实现SpringApplicationRunListener接口,监听SpringApplication生命周期
 *
 * SpringApplication源码分析
 * Main - SpringApplication类306行run() - 314行getRunListeners() - 466行getSpringFactoriesInstances() -
 * SpringFactoriesLoader类298行forDefaultResourceLocation() - FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories"
 *
 * spring-boot.jar的META-INF/spring.factories在27行配置了SpringApplicationRunListener接口的默认实现类
 * org.springframework.boot.SpringApplicationRunListener=org.springframework.boot.context.event.EventPublishingRunListener
 *
 * 参考SpringBoot的写法在当前项目的META-INF/spring.factories配置自定义SpringApplicationRunListener
 * org.springframework.boot.SpringApplicationRunListener=com.okccc.listener.MySpringApplicationRunListener
 *
 * SpringApplication六大生命周期
 * SpringApplication源码315/366/398/431/329/341/799行 - SpringApplicationRunListeners源码53/62/67/71/75/79/83行 - SpringApplicationRunListener
 * 引导阶段：ConfigurableBootstrapContext
 * 1.starting           ：源码315行,run方法启动时立即调用,可以用作非常早期的初始化
 * 2.environmentPrepared：源码366行,环境已准备好(将启动参数绑定到环境变量)时调用,但IOC容器还没创建
 * 启动阶段：ConfigurableApplicationContext
 * 3.contextPrepared    ：源码398行,IOC容器创建并准备好时调用,但sources主配置类还没加载,并关闭引导上下文,组件还没创建
 * 4.contextLoaded      ：源码431行,IOC容器已加载时调用,主配置类加载进去了,但IOC容器还没刷新(Bean还没造)
 * ========== 源码312行refreshContext()会触发AbstractApplicationContext.refresh()的IOC容器刷新经典12大步 ==========
 * 5.started            ：源码329行,IOC容器已刷新,所有Bean都已造好,但runner还没调用
 * 6.ready              ：源码341行,run方法结束时立即调用,IOC容器已刷新,所有Bean都已造好,且runner调用完毕
 * 运行阶段：以上步骤都正确代表容器running
 * failed               ：源码799行,当应用运行发生异常时调用
 *
 * 主程序启动后查看控制台输出日志,观察生命周期监听和事件触发时机
 */
public class MySpringApplicationRunListener implements SpringApplicationRunListener {

    /**
     * Called immediately when the run method has first started. Can be used for very early initialization.
     */
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        SpringApplicationRunListener.super.starting(bootstrapContext);
        System.out.println("========== starting 正在启动 ==========");
    }

    /**
     * Called once the environment has been prepared, but before the ApplicationContext has been created.
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
        System.out.println("========== environmentPrepared 环境准备完成 ==========");
    }

    /**
     * Called once the ApplicationContext has been created and prepared, but before sources have been loaded.
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextPrepared(context);
        // ApplicationContext结尾说明是IOC容器
        System.out.println("========== contextPrepared IOC容器准备完成 ==========");
    }

    /**
     * Called once the application context has been loaded but before it has been refreshed.
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextLoaded(context);
        System.out.println("========== contextLoaded IOC容器加载完成 ==========");
    }

    /**
     * The context has been refreshed and the application has started but CommandLineRunner and ApplicationRunner have not been called.
     */
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.started(context, timeTaken);
        System.out.println("========== started 启动完成 ==========");
    }

    /**
     * Called immediately before the run method finishes, when the application context has
     * been refreshed and all CommandLineRunner and ApplicationRunner have been called.
     */
    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        SpringApplicationRunListener.super.ready(context, timeTaken);
        System.out.println("========== ready 准备就绪 ==========");
    }

    /**
     * Called when a failure occurs when running the application.
     */
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        SpringApplicationRunListener.super.failed(context, exception);
        System.out.println("========== failed 启动失败 ==========");
    }

}
