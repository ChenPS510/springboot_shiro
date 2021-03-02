package com.mk.shiro.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mk.chen
 * 设置编码格式（一）
 * 这两种方式都可以，也可以在application.yml中配置（目前配置在yml文件中）
 */
//@Component
//public class CharacterEncodingFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(CharacterEncodingFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
//        logger.info("SetCharacterEncodingFilter===>UTF-8");
//        HttpServletRequest request = (HttpServletRequest) req;
//        request.setCharacterEncoding("UTF-8");
//        resp.setCharacterEncoding("UTF-8");
//        filterChain.doFilter(req, resp);
//    }
//
//    @Override
//    public void destroy() {
//    }
//}

/**
 * @author mk
 * 字符编码过滤器（二）
 */
//@WebFilter(urlPatterns = "/*", filterName = "CharacterEncodingFilter")
//public class CharacterEncodingFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//    }
//}