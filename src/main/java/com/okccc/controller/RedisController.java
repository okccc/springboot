package com.okccc.controller;

import com.okccc.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author: okccc
 * @Date: 2024/2/24 16:08:25
 * @Desc:
 */
@RestController
@RequestMapping(value = "redis")
public class RedisController {

    // RedisAutoConfiguration源码70行使用@Bean注册组件StringRedisTemplate
    // key和value是String类型(只能操作字符串,有点呆)
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // http://localhost:8080/redis/cnt
    @GetMapping(value = "cnt")
    public String cnt() {
        Long cnt = stringRedisTemplate.opsForValue().increment("cnt");
        return "访问了【" + cnt + "】次";
    }

    // http://localhost:8080/redis/str
    @GetMapping(value = "str")
    public String str() {
        // 操作字符串(string)
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set("orc", "grubby");
        return valueOperations.get("orc");
    }

    // http://localhost:8080/redis/list
    @GetMapping(value = "list")
    public String list() {
        // 操作列表(list)
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        listOperations.leftPush("war3", "grubby");
        listOperations.leftPush("war3", "moon");
        listOperations.leftPush("war3", "sky");
        return listOperations.leftPop("war3");
    }

    // http://localhost:8080/redis/set
    @GetMapping(value = "set")
    public Boolean set() {
        // 操作集合(set)
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        setOperations.add("s1", "aaa", "bbb", "ccc");
        return setOperations.isMember("s1", "aaa");
    }

    // http://localhost:8080/redis/zset
    @GetMapping(value = "zset")
    public Double zset() {
        // 操作有序集合(zset)
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.add("s2", "grubby", 99.00);
        zSetOperations.add("s2", "moon", 98.00);
        zSetOperations.add("s2", "sky", 98.00);
        return zSetOperations.score("s2", "grubby");
    }

    // http://localhost:8080/redis/hash
    @GetMapping(value = "hash")
    public String hash() {
        // 操作哈希(hash)
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("emp", "name", "james");
        hashOperations.put("emp", "age", "19");
        hashOperations.put("emp", "gender", "男");
        return Objects.requireNonNull(hashOperations.get("emp", "name")).toString();
    }

    // RedisAutoConfiguration源码61行使用@Bean注册组件RedisTemplate
    // key和value是Object类型(可以操作对象,更灵活)
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    // http://localhost:8080/redis/user/save
    // http://localhost:8080/redis/user/save?format=json
    @GetMapping(value = "user/save")
    public User saveUser() {
        // 往redis保存java对象
        User user = new User(1, "grubby", "123456", 19, "1", "orc@qq.com");
        // 问题1：java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.okccc.bean.User]
        // 查看RedisTemplate组件的源码136行,发现使用的默认序列化器是JdkSerializationRedisSerializer
        // 点进去找到DefaultSerializer源码41行,发现要序列化的对象必须实现Serializable接口,也就是上面的异常信息

        // 问题2：修改User类实现Serializable接口后数据能写进redis了,但是保存的User对象不可视(���看着像乱码)
        // 考虑系统兼容性建议将所有对象都存储为JSON格式,官方RedisTemplate实现不了需要定制化组件,详见MyRedisConfiguration
        redisTemplate.opsForValue().set("user", user);
        return user;
    }

    // http://localhost:8080/redis/user/get
    // http://localhost:8080/redis/user/get?format=json
    @GetMapping(value = "user/get")
    public User getUser() {
        return (User) redisTemplate.opsForValue().get("user");
    }

}
