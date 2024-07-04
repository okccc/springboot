package com.okccc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: okccc
 * @Date: 2024/3/1 16:36:55
 * @Desc:
 */
@Configuration
public class Knife4jConfig {

    // 给页面添加标题和描述信息
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("SpringBoot3")
                        .description("SpringBoot3")
                        .contact(new Contact().name("okccc"))
                        .version("1.0.0")
        );
    }

    // 按照不同业务分组
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/user/**", "/users")
                .build();
    }

    @Bean
    public GroupedOpenApi redisApi() {
        return GroupedOpenApi.builder()
                .group("redis-api")
                .pathsToMatch("/redis/**")
                .build();
    }

}
