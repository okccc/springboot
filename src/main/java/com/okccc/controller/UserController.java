package com.okccc.controller;

import com.okccc.bean.User;
import com.okccc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: okccc
 * @Date: 2023/12/31 17:10:40
 * @Desc: 整合SSM,测试数据访问时GET请求可使用浏览器,POST/PUT/DELETE请求得使用Postman
 *
 * Field injection is not recommended
 * 相较于@Autowired注解,Spring更推荐使用构造函数进行依赖注入
 * 但是成员变量很多的话就要手写大量构造函数代码,此时可以使用lombok的@AllArgsConstructor自动生成构造函数
 * 但是有些成员变量不需要初始化,此时可以给需要的添加final关键字将其变成常量,这样就必须初始化,然后使用lombok的@RequiredArgsConstructor
 */
@Tag(name = "用户接口", description = "基于用户的CRUD")
@RestController
@RequestMapping(value = "user")
@RequiredArgsConstructor
public class UserController {

//    @Autowired
    private final JdbcTemplate jdbcTemplate;  // 很少用,访问数据库都用mybatis

    // http://localhost:8080/user/all
    @GetMapping(value = "all")
    public List<User> queryAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Autowired
    private UserService userService;

    // http://localhost:8080/user/1
    @Operation(summary = "查询", description = "根据id查询用户")
    @GetMapping(value = "{id}")
    public User queryUser(@PathVariable("id") Integer id) {
        return userService.getById(id);
    }

    // http://localhost:8080/user/list
    @Operation(summary = "查询", description = "查询所有用户")
    @GetMapping(value = "list")
    public List<User> queryList() {
        return userService.list();
    }

    // http://localhost:8080/user/save
    @Operation(summary = "新增", description = "新增用户")
    @PostMapping(value = "save")
    public int saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    // http://localhost:8080/user/update
    @Operation(summary = "修改", description = "修改用户")
    @PutMapping(value = "update")
    public int updateUser(@RequestBody User user) {
        return userService.updateById(user);
    }

    // http://localhost:8080/user/1
    @Operation(summary = "删除", description = "删除用户")
    @DeleteMapping(value = "{id}")
    public int removeUser(@PathVariable Integer id) {
        return userService.removeById(id);
    }
}
