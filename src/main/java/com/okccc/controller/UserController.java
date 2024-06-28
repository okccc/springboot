package com.okccc.controller;

import com.okccc.bean.User;
import com.okccc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: okccc
 * @Date: 2023/12/31 17:10:40
 * @Desc: 整合SSM,测试数据访问时GET请求可使用浏览器,POST/PUT/DELETE请求得使用Postman
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;  // 很少用,访问数据库都用mybatis

    // http://localhost:8080/user/all
    @GetMapping(value = "all")
    public List<User> queryAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Autowired
    private UserService userService;

    // http://localhost:8080/user/1
    @GetMapping(value = "{id}")
    public User queryUser(@PathVariable("id") Integer id) {
        return userService.getById(id);
    }

    // http://localhost:8080/user/list
    @GetMapping(value = "list")
    public List<User> queryList() {
        return userService.list();
    }

    // http://localhost:8080/user/save
    @PostMapping(value = "save")
    public int saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    // http://localhost:8080/user/update
    @PutMapping(value = "update")
    public int updateUser(@RequestBody User user) {
        return userService.updateById(user);
    }

    // http://localhost:8080/user/1
    @DeleteMapping(value = "{id}")
    public int removeUser(@PathVariable Integer id) {
        return userService.removeById(id);
    }
}
