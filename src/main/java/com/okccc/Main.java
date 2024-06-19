package com.okccc;

import com.alibaba.druid.FastsqlException;
import com.okccc.bean.Person;
import com.okccc.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: okccc
 * @Date: 2023/12/30 16:52:01
 * @Desc: 启动类
 */
@EnableWebMvc
@SpringBootApplication
@EnableConfigurationProperties(value = {Person.class})
public class Main {
    public static void main(String[] args) {
        // java10局部变量自动推断类型
        var ioc = SpringApplication.run(Main.class, args);

        // 看看IOC容器中都有哪些组件
        String[] beans = ioc.getBeanDefinitionNames();
        for (String bean : beans) {
            // SpringBoot把SSM手动配置的核心组件都整合好了,导入启动器就会导入该场景下的所有依赖
            // web场景：dispatcherServlet、viewResolver、characterEncodingFilter、handlerExceptionResolver...
            System.out.println("bean = " + bean);
        }

        // 测试组件注册
        for (String bean : ioc.getBeanNamesForType(FastsqlException.class)) {
            System.out.println("bean = " + bean);
        }

        // 测试条件注解
        for (String bean : ioc.getBeanNamesForType(User.class)) {
            System.out.println("bean = " + bean);
        }

        // 测试属性绑定
        Person person = ioc.getBean(Person.class);
        System.out.println(person.getName());
        System.out.println(person.getChild().getHobbys().get(0));
        System.out.println(person.getCats().get(1));
        System.out.println(person.getDogs().get("d2"));
    }

}