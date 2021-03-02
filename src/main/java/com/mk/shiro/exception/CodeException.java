package com.mk.shiro.exception;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:23
 * @Description:
 */
public class CodeException extends RuntimeException {
    private Integer code;
    private String message;

    public CodeException(CodeEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public CodeException(CodeEnum exceptionEnum, String message) {
        this.code = exceptionEnum.getCode();
        this.message = message;
    }

    public CodeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CodeException(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
