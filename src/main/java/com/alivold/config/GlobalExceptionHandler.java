package com.alivold.config;

import com.alivold.resp.ResponseResult;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 确保它是最高优先级的异常处理器
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseResult handleBaseException(BaseException e) {
       return ResponseResult.fail(e.getMessage());
    }
}
