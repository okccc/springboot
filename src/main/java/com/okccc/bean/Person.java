package com.okccc.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: okccc
 * @Date: 2024/1/10 11:08:59
 * @Desc: 获取application.yaml自定义配置项的方式
 *
 * 1.直接在属性上添加@Value注解
 * 缺点：key必须写全,并且只能读取单值类型,读不了集合类型Could not resolve placeholder 'aaa.hobbys' in value "${aaa.hobbys}"
 *
 * 2.使用@ConfigurationProperties注解进行属性绑定,定义配置项的通用前缀,属性名和配置项的最后一个key相同即可
 * 优点：不用给所有属性挨个添加@Value注解,并且支持读取集合类型
 */
@Data
@ConfigurationProperties(prefix = "person")
@Component  // 或者参考XxxAutoConfiguration,在启动类添加@EnableConfigurationProperties(value = {Person.class})
public class Person {

//    @Value("${person.name}")
    private String name;

    private Integer age;

    // failed to convert java.lang.String to java.util.Date (caused by java.lang.IllegalArgumentException)
    // springboot的Formatter格式化组件默认会将"yyyy/MM/dd"格式的字符串转换成日期或时间,也可以给属性添加注解指定pattern
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private Boolean flag;

    private Child child;

    private List<Cat> cats;

    private Map<String, Dog> dogs;
}
