package com.okccc.handler;

import com.okccc.common.ResultCodeEnum;
import lombok.Getter;

/**
 * @Author: okccc
 * @Date: 2024/4/22 17:59:32
 * @Desc: 自定义异常继承RuntimeException,将具体的ReturnCodeEnum作为构造参数传入,方便统一响应Result
 */
@Getter
public class MyRuntimeException extends RuntimeException {

    private final ResultCodeEnum resultCodeEnum;

    public MyRuntimeException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }
}
