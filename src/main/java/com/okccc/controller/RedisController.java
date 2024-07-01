package com.okccc.controller;

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

}
