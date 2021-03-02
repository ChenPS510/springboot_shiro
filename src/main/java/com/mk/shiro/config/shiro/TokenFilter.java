package com.mk.shiro.config.shiro;

import com.alibaba.fastjson.JSON;
import com.mk.shiro.exception.BaseResponse;
import com.mk.shiro.exception.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: mk.chen
 * @Date: 2020/1/11 0:23
 * @description: token认证拦截器，对每次请求检查，如果该用户未通过认证，则会提示用户登录超时，需要重新登录
 */
@Slf4j
public class TokenFilter extends AccessControlFilter {

    private MessageSource messageSource;

    public TokenFilter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        Subject subject = getSubject(servletRequest, servletResponse);
        // 只有完成认证的用户才允许访问
        boolean authenticated = subject.isAuthenticated();
        log.info("TokenFilter: current User is authenticated flag: {}", authenticated);
        return authenticated;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        /*
          当不允许访问时，通过response回写错误信息
         */
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        //设置Http响应头控制UTF-8
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        BaseResponse commonResult = ResponseUtil.error(Integer.valueOf(getLocaleMessage("api.error.code.invalid_token")), getLocaleMessage("api.error.message.invalid_token"));
        response.getWriter().write(JSON.toJSONString(commonResult));
        return false;
    }

    private String getLocaleMessage(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}
