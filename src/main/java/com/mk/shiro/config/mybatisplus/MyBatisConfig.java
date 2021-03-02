package com.mk.shiro.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: mk.chen
 * @Date: 2019/12/27 10:32
 * @Description:
 */
@Configuration
@MapperScan("com.mk.shiro.mapper")
public class MyBatisConfig {

    /**
     * mybatis-plus分页配置
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}