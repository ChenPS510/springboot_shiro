package com.mk.shiro.exception;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:24
 * @Description:
 */
public class BusinessException extends RuntimeException {

    private String code;
    private String additionalMessage;
    private boolean translate;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.Codes.PARRAMS_ERROR.getCode().toString();
        this.translate = false;
    }

    public BusinessException(CodeEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode().toString();
        this.translate = false;
    }

    public BusinessException(String code, String message, boolean translate) {
        super(message);
        this.code = code;
        this.translate = translate;
    }

    public BusinessException(String code, String message, String additionalMessage, boolean translate) {
        super(message);
        this.code = code;
        this.additionalMessage = additionalMessage;
        this.translate = translate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

    public boolean isTranslate() {
        return translate;
    }

    public void setTranslate(boolean translate) {
        this.translate = translate;
    }
}