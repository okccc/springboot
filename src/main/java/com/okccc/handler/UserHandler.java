package com.okccc.handler;

import com.okccc.bean.User;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: okccc
 * @Date: 2024/2/15 14:51:26
 * @Desc: 处理User相关业务,参照org.springframework.web.servlet.function.HandlerFunction接口
 *
 * GET /user/1     获取用户
 * GET /users      获取所有用户
 * POST /user      新增用户
 * PUT /user/1     修改用户
 * DELETE /user/1  删除用户
 */
@Slf4j
@Component
public class UserHandler {

    /**
     * 查询指定id的用户
     */
    public ServerResponse getUser(ServerRequest request) {
        String id = request.pathVariable("id");
        log.info("查询 {} 用户信息,正在检索", id);
        // 模拟业务处理
        User user = new User(1, "grubby", "123456", 19, "male", "orc@qq.com");
        // 构造响应
        return ServerResponse.ok().body(user);
    }

    /**
     * 获取所有用户
     */
    public ServerResponse getUsers(ServerRequest request) {
        log.info("查询所有用户信息");
        // 模拟业务处理
        User user01 = new User(1, "grubby", "aaa", 19, "1", "orc@qq.com");
        User user02 = new User(2, "moon", "bbb", 19, "0", "ne@qq.com");
        List<User> list = Arrays.asList(user01, user02);
        // 构造响应
        return ServerResponse.ok().body(list);  // body中的对象就是@ResponseBody的原理,利用HttpMessageConverter写出为json
    }

    /**
     * 新增用户
     */
    public ServerResponse saveUser(ServerRequest request) throws ServletException, IOException {
        // 提取请求体
        User body = request.body(User.class);
        log.info("保存用户信息 {}", body);
        return ServerResponse.ok().build();
    }

    /**
     * 更新用户
     */
    public ServerResponse updateUser(ServerRequest request) throws ServletException, IOException {
        // 提取请求体
        User body = request.body(User.class);
        log.info("更新用户信息 {}", body);
        return ServerResponse.ok().build();
    }

    /**
     * 删除用户
     */
    public ServerResponse deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        log.info("删除用户信息 {}", id);
        return ServerResponse.ok().build();
    }

}
