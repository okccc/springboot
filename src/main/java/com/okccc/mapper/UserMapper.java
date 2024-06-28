package com.okccc.mapper;

import com.okccc.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: okccc
 * @Date: 2023/12/31 18:12:30
 * @Desc:
 */
@Mapper
public interface UserMapper {

    User selectById(@Param("id") Integer id);

    List<User> selectList();

    int insert(User user);

    int updateById(User user);

    int deleteById(@Param("id") Integer id);
}
