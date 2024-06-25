package com.okccc.common;

import lombok.Data;

/**
 * @Author: okccc
 * @Date: 2024/3/24 20:10:47
 * @Desc: 定义Controller层的通用返回结果,泛型<T>包含String/Integer/Double/POJO等各种类型
 */
@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;
    private long timestamp;  // 时间戳字段很有用,每次调用接口时间肯定不一样,方便对比结果

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    // 返回数据
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 通过枚举构造Result对象
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        return build(body, resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    // 成功就返回200和具体数据
    public static <T> Result<T> success(T data) {
        Result<T> resultData = new Result<>();
        resultData.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultData.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        resultData.setData(data);
        return resultData;
    }

    // 失败就返回具体的状态码和异常信息
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

}
