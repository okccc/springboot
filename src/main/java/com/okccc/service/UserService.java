package com.okccc.service;

import com.okccc.mapper.UserMapper;
import com.okccc.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: okccc
 * @Date: 2023/12/31 20:11:30
 * @Desc:
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> list() {
        return userMapper.selectList();
    }

    public int save(User user) {
        return userMapper.insert(user);
    }

    public int updateById(User user) {
        return userMapper.updateById(user);
    }

    @Transactional
    public int removeById(Integer id) {
        return userMapper.deleteById(id);
    }

}
