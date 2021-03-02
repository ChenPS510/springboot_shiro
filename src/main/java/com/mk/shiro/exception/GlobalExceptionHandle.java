package com.mk.shiro.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:15
 * @Description:
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {

    @Autowired
    private MessageSource messageSource;

    /**
     * 捕获最大的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleUnKnownException(Exception e) {
        log.error("Returning HTTP 500 Internal Server Error", e);
        return ResponseUtil.error(Integer.valueOf(getLocaleMessage("api.error.code.internal")), getLocaleMessage("api.error.message.internal"));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleMethodException(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        log.error("Returning HTTP 405 Method Not Allowed, path is " + request.getRequestURI(), exception);

        return ResponseUtil.error(Integer.valueOf(getLocaleMessage("api.error.code.method_not_allowed")), getLocaleMessage("api.error.message.method_not_allowed") + " " + exception.getMessage());
    }

    @ExceptionHandler(value = {ServletRequestBindingException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, BindException.class, ConversionException.class, HttpMessageConversionException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleMissingServletRequestParameterException(Exception exception) {
        log.error("Returning HTTP 400 Bad Request cause by ParameterException exception : {}", exception.getMessage());
        return ResponseUtil.error(Integer.valueOf(getLocaleMessage("api.error.code.illegal_parameter")),
                getLocaleMessage("api.error.message.illegal_parameter") + " " + exception.getMessage());
    }

    /**
     * 自定义业务异常处理
     *
     * @param businessException
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleBusinessException(BusinessException businessException) {
        log.error("Returning HTTP 400 Bad Request cause by business exception : {}", businessException.getMessage());

        BaseResponse baseResponse = null;
        if (businessException.isTranslate()) {
            String errorMessage = getLocaleMessage(businessException.getMessage());
            // concat addtional message
            if (!StringUtils.isEmpty(businessException.getAdditionalMessage())) {
                errorMessage = errorMessage + ":" + businessException.getAdditionalMessage();
            }
            baseResponse = ResponseUtil.error(Integer.valueOf(getLocaleMessage(businessException.getCode())), errorMessage);
        } else {
            baseResponse = ResponseUtil.error(Integer.valueOf(businessException.getCode()), businessException.getMessage());
        }
        return baseResponse;
    }

    /**
     * shiro没有权限异常处理
     *
     * @param unauthorizedException
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        log.error("Returning HTTP 403 Forbidden cause by unauthorizedException : {}", unauthorizedException.getMessage());

        return ResponseUtil.error(Integer.valueOf(getLocaleMessage("api.error.code.unauthorized")),
                getLocaleMessage("api.error.message.unauthorized"));
    }

    private String getLocaleMessage(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}