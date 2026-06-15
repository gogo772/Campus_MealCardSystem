package com.example.campus_mealcardsystem.exception;

import com.example.campus_mealcardsystem.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusiness(BusinessException e) {
        return Result.fail(e.getMessage());
    }

    // 兜底捕获所有未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleAll(Exception e) {
        e.printStackTrace();
        return Result.fail("系统异常，请联系管理员");
    }
}