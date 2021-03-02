package com.mk.shiro.exception;

import java.io.Serializable;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:21
 * @Description:
 */
public class BaseResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(T t) {
        this.data = t;
        this.code = ResultCode.Codes.SUCCESS.getCode();
        this.message = ResultCode.Codes.SUCCESS.getMessage();
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(CodeEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{code=" + this.code + ", message='" + this.message + '\'' + ", data=" + this.data + '}';
    }
}

