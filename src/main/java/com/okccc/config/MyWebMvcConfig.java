package com.okccc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @Author: okccc
 * @Date: 2024/1/13 11:59:37
 * @Desc: 自定义类实现WebMvcConfigurer接口,定义SpringMVC底层组件
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源,其它底层组件使用方式也都类似,之前SpringMVC的组件该怎么用还怎么用
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 保留默认规则
        WebMvcConfigurer.super.addResourceHandlers(registry);
        // 自定义新规则
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/a/", "classpath:/b/")
                .setCacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS));
    }

}
