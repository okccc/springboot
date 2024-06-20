package com.okccc.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: okccc
 * @Date: 2023/12/30 17:03:45
 * @Desc: web开发详解
 */
@Slf4j
@RestController  // 点进去发现就等于 @Controller + @ResponseBody
public class HelloController {

    // http://localhost:8080/log?name=aaa&age=19
    @GetMapping(value = "log")
    public String log(String name, int age) {
        for (int i = 0; i < 10000; i++) {
            log.info("name = " + name + ", age = " + age);
        }
        return "hello springboot3!";
    }

    /**
     * 路径匹配：默认使用新版策略PATH_PATTERN_PARSER,比老版策略ANT_PATH_PARSER性能更好
     * 注意事项：新版策略**只能出现在末尾不能出现在中间,其它语法和老版策略相同
     * Ant风格路径写法：*任意数量的字符、?任意单个字符、**任意数量的目录、{}路径占位符、[]字符集合,[a-z]表示小写字母
     */
    // http://localhost:8080/a123/b1/1/2/3/abcdefg/abc/123
    @GetMapping(value = "/a*/b?/**/{p1:[a-z]+}/**")
    public String path(HttpServletRequest request, @PathVariable("p1") String path) {
        log.info("path variable p1: {}", path);
        return request.getRequestURI();
    }

}
