package com.example.campus_mealcardsystem.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 无数据成功
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    // 仅返回数据
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    // 自定义提示 + 数据（解决 success("xxx",obj) 泛型报错）
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }
    // 通用失败
    public static <T> Result<T> fail(String msg) {
        return new Result<>(500, msg, null);
    }
    // 自定义错误码+提示（error(int,str)）
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
    // 404
    public static <T> Result<T> notFound(String msg) {
        return new Result<>(404, msg, null);
    }
    // 参数错误400
    public static <T> Result<T> paramError(String msg) {
        return new Result<>(400, msg, null);
    }
}