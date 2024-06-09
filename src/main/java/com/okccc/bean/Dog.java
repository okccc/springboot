package com.okccc.bean;

import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @Author: okccc
 * @Date: 2024/1/10 11:10:22
 * @Desc:
 */
@Data
@Component
public class Dog {
    private String name;
    private Integer age;
}
