package com.okccc.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: okccc
 * @Date: 2024/3/24 20:04:23
 * @Desc: 定义通用枚举类步骤：1.举值 - 2.构造 - 3.遍历
 *
 * 状态码,参考org.springframework.http.HttpStatus
 * 100 ~ 199：信息,服务器收到请求,需要请求者继续执行操作
 * 200 ~ 299：成功,操作被成功接收并处理
 * 300 ~ 399：重定向,需要进一步操作并完成请求
 * 400 ~ 499：客户端错误,请求包含语法错误或无法完成请求
 * 500 ~ 599：服务器错误,服务器在处理请求过程中发生了错误
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),

    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试");

    // 自定义状态码
    private final Integer code;

    // 自定义描述信息
    private final String message;

    ResultCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    // 传统方式遍历
    public static ResultCodeEnum getReturnCodeEnum(Integer code) {
        for (ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()) {
            if (Objects.equals(code, resultCodeEnum.getCode())) {
                return resultCodeEnum;
            }
        }
        return null;
    }

    // Stream流计算方式遍历
    public static ResultCodeEnum getReturnCodeEnumV2(Integer code) {
        return Arrays.stream(ResultCodeEnum.values()).filter(x -> Objects.equals(x.getCode(), code)).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        System.out.println(getReturnCodeEnum(200));
        System.out.println(getReturnCodeEnum(200).getCode());
        System.out.println(getReturnCodeEnum(200).getMessage());

        System.out.println(getReturnCodeEnumV2(200));
        System.out.println(getReturnCodeEnumV2(200).getCode());
        System.out.println(getReturnCodeEnumV2(200).getMessage());
    }
}
