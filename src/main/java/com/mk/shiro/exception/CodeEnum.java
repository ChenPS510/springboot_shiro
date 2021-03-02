package com.mk.shiro.exception;

/**
 * @Author: mk.chen
 * @Date: 2020/1/2 11:22
 * @Description:
 */
public interface CodeEnum {
    /**
     * 获取返回值状态码
     *
     * @return
     */
    Integer getCode();

    /**
     * 获取提示信息
     *
     * @return
     */
    String getMessage();
}
