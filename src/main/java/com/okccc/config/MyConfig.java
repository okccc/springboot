package com.okccc.config;

import com.alibaba.druid.FastsqlException;
import com.okccc.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
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
 *
 * 2.条件注解
 * 如果注解指定的条件成立就触发指定行为,放在类上针对当前类,放在方法上针对当前方法
 * 举例：@ConditionalOnClass/@ConditionalOnMissingClass/@ConditionalOnBean/@ConditionalOnMissingBean
 *
 * 3.属性绑定(详见Person)
 * 自定义类：@Component注册组件 - @ConfigurationProperties(prefix = "...")声明组件属性和配置文件哪些配置项绑定
 * SpringBoot默认只扫描主程序所在包,第三方包即使标注了@Component和@ConfigurationProperties也扫不到
 * 第三方类：@EnableConfigurationProperties({WebMvcProperties.class, WebProperties.class})
 */
@Configuration  // 使用配置类代替配置文件,@Configuration包含@Component,所以配置类本身也是容器中的组件
public class MyConfig {

    @Bean  // 使用注解标记方法代替<bean>标签,方法名就是id属性,返回值类型就是class属性
    public FastsqlException fastsqlException() {
        return new FastsqlException();
    }

    @Bean
    @ConditionalOnClass(name = "com.alibaba.druid.FastsqlException")  // Ctrl点不动说明没有导入相关依赖
    public User user01() {
        return new User();
    }

    @Bean
    @ConditionalOnMissingClass(value = "com.alibaba.druid.FastsqlException")
    public User user02() {
        return new User();
    }
}
