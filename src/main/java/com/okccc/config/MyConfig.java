package com.okccc.config;

import com.alibaba.druid.FastsqlException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: okccc
 * @Date: 2024/1/7 12:18:03
 * @Desc: springboot常用注解
 *
 * 1.组件注册
 * 自定义类：@Component/@Controller/@Service/@Repository
 * 第三方类：@Configuration配置类中通过@Bean注册,或者直接@Import导入(参考@EnableAutoConfiguration)
 */
@Configuration  // 使用配置类代替配置文件,@Configuration包含@Component,所以配置类本身也是容器中的组件
public class MyConfig {

    @Bean  // 使用注解标记方法代替<bean>标签,方法名就是id属性,返回值类型就是class属性
    public FastsqlException fastsqlException() {
        return new FastsqlException();
    }
}
