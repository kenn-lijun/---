package com.kenn.book.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ApiModel(value = "统一返回实体类",description = "统一返回实体类")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "返回状态码===>200:成功 返回状态码===>500:服务器内部错误", example = "200")
    private Integer code;

    @ApiModelProperty(value = "描述信息", example = "操作成功")
    private String msg;

    private T data;

    private Result() {
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(HttpStatus.SUCCESS, msg, data);
    }

    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    public static <T> Result<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> Result<T> success() {
        return success("操作成功");
    }

    public static <T> Result<T> error(String msg, T data) {
        return new Result<>(HttpStatus.ERROR, msg, data);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <T> Result<T> error(T data) {
        return error("操作失败", data);
    }

    public static <T> Result<T> error(String msg) {
        return error(msg, null);
    }

    public static <T> Result<T> error() {
        return error("操作失败");
    }

}