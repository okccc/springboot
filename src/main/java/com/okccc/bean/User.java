package com.okccc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: okccc
 * @Date: 2023/12/31 17:16:57
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private Integer age;

    private String gender;

    private String email;
}
