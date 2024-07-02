package com.okccc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @Author: okccc
 * @Date: 2024/2/24 19:03:11
 * @Desc: 参考SpringBoot源码自定义RedisTemplate组件,方便额外扩展功能
 *
 * RedisAutoConfiguration源码59行：@ConditionalOnMissingBean(name = "redisTemplate")
 * 自定义RedisTemplate组件使该条件注解失效,这样就不会使用SpringBoot自带的RedisTemplate,而是使用我们自定义的RedisTemplate
 *
 * RedisAutoConfiguration源码49行：@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
 * LettuceConnectionConfiguration和JedisConnectionConfiguration都使用@Bean注册了RedisConnectionFactory接口
 * redis场景启动器点进去发现默认的redis客户端是lettuce,也可以在引入依赖时将其注释掉然后手动导入jedis客户端进行切换
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 将序列化器修改为可以将java对象转换成json字符串的GenericJackson2JsonRedisSerializer
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
