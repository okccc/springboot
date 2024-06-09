package com.okccc.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: okccc
 * @Date: 2024/1/10 11:10:07
 * @Desc:
 */
@Data
public class Child {

    private String name;

    private Integer age;

    private Date birthday;

    private List<String> hobbys;
}
