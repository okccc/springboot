package com.okccc.config;

import com.okccc.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.*;

/**
 * @Author: okccc
 * @Date: 2024/2/15 14:37:15
 * @Desc: web新特性之函数式web
 *
 * SpringMVC5.2以后允许使用函数式来定义web的请求处理流程
 * 路由和业务耦合：@Controller + @RequestMapping
 * 路由和业务分离：函数式web(不常用,目前主流方式还是写Controller)
 *
 * 给容器中放一个Bean,类型是RouterFunction<ServerResponse>,集中定义所有路由信息
 * 给每个业务准备一个自己的Handler
 *
 * 四大核心对象
 * RouterFunction：  定义路由,发送什么请求,由谁处理
 * RequestPredicate：定义请求规则,包括请求方式(GET/POST/PUT/DELETE)和请求参数
 * ServerRequest：   封装请求数据
 * ServerResponse：  封装响应数据
 */
@Configuration
public class WebFunctionConfig {

    /**
     * 用户相关的路由
     */
    @Bean  // 使用@Bean往容器中放组件时,如果参数是对象就默认从容器中获取
    public RouterFunction<ServerResponse> userRoute(UserHandler userHandler) {
        // 带s的通常是工具类,比如RouterFunctions是RouterFunction接口的工具类
        return RouterFunctions.route()
                .GET("/user/{id}", RequestPredicates.accept(MediaType.ALL), userHandler::getUser)
                .GET("/users", userHandler::getUsers)
                .POST("/user", RequestPredicates.accept(MediaType.APPLICATION_JSON), userHandler::saveUser)
                .PUT("/user/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), userHandler::updateUser)
                .DELETE("/user/{id}", userHandler::deleteUser)
                .build();
    }

    /**
     * 商品相关的路由
     */
    @Bean
    public RouterFunction<ServerResponse> productRoute() {
        return null;
    }

    /**
     * 订单相关的路由
     */
    @Bean
    public RouterFunction<ServerResponse> orderRoute() {
        return null;
    }

}
