package com.okccc.handler;

import com.okccc.common.Result;
import com.okccc.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: okccc
 * @Date: 2024/4/22 17:06:12
 * @Desc: 全局异常处理器(自动挡),统一处理异常而不是给前端响应Whitelabel Error Page,不用再写try/catch(手动挡)
 *
 * AOP应用场景：记录日志、事务处理、异常处理、权限控制、性能监控、缓存控制
 *
 * 事务处理两种方式：
 * 编程式事务：所有操作都是硬编码实现,繁琐且没法复用
 * 声明式事务：通过xml或注解控制事务的提交和回滚,将业务逻辑和事务控制分离,提高代码可读性和维护性
 *
 * 异常处理两种方式：
 * 编程式异常：使用try-catch显式地捕获异常,与业务代码混在一起,可读性较差
 * 声明式异常：通过xml或注解统一处理异常,将业务逻辑和异常处理分离,提高代码可读性和可维护性
 *
 * 统一处理异常之前：
 * {
 *     "timestamp": "2024-04-24T02:33:11.631+00:00",
 *     "status": 500,
 *     "error": "Internal Server Error",
 *     "path": "/admin/system/index/login"
 * }
 *
 * 统一处理异常之后：
 * {"code":202, "message":"验证码错误", "data":null, "timestamp":1713927412809}
 *
 */
//@ControllerAdvice  // Controller增强器,集中处理所有Controller发生的错误
//@ResponseBody  // 像Controller一样给前端返回JSON数据
@RestControllerAdvice  // 点进去发现就等于 @ControllerAdvice + @ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理全局异常,匹配不到具体异常时才会走这里(兜底)
     */
    @ExceptionHandler(value = Exception.class)
    public Result<String> error(Exception e) {
        // 记录异常信息
        log.error(e.getMessage(), e);
        // 给前端响应结果
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 处理具体异常,优先级更高
     */
    @ExceptionHandler(MyRuntimeException.class)
    public Result<String> error(MyRuntimeException e) {
        // 记录异常信息
        log.error(e.getMessage(), e);
        // 给前端响应结果
        return Result.build(null, e.getResultCodeEnum());
    }

    /**
     * 空指针异常时触发
     */
    @ExceptionHandler(value = NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    /**
     * 算术异常时触发
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public String arithmeticExceptionHandler(ArithmeticException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    /**
     * 类型转换异常时触发
     */
    @ExceptionHandler(value = ClassCastException.class)
    public String classCastExceptionHandler(ClassCastException e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

}
