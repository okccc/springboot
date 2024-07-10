package com.okccc;

import com.alibaba.druid.FastsqlException;
import com.okccc.bean.Person;
import com.okccc.bean.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: okccc
 * @Date: 2023/12/30 16:52:01
 * @Desc: 启动类
 */
//@EnableWebMvc  // 不能随便加,会禁用SpringBoot的默认配置,相应的application.yaml的部分配置也会失效
@MapperScan(basePackages = "com.okccc.mapper")  // 告诉mybatis扫描哪个包,这样就不用给每个Mapper接口添加@Mapper注解
@SpringBootApplication
@EnableConfigurationProperties(value = {Person.class})
public class Main {
    public static void main(String[] args) {
        // java10局部变量自动推断类型
//        var ioc = SpringApplication.run(Main.class, args);

        // 查看run方法源码发现包含两步,可以拆开写方便自定义SpringApplication
        SpringApplication application = new SpringApplication(Main.class);

        // 此处可以做很多设置,比如设置banner模式,但是配置文件优先级更高
        application.setBannerMode(Banner.Mode.OFF);

        // 启动应用
        ConfigurableApplicationContext ioc = application.run(args);

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