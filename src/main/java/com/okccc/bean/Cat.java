package com.okccc.bean;

import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @Author: okccc
 * @Date: 2024/1/7 18:47:27
 * @Desc:
 */
@Data
@Component
public class Cat {
    private String name;
    private Integer age;
}
