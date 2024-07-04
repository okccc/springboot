package com.okccc.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: okccc
 * @Date: 2023/12/31 17:16:57
 * @Desc:
 */
@Schema(description = "用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "用户名称")
    private String username;

    private String password;

    private Integer age;

    private String gender;

    private String email;
}
