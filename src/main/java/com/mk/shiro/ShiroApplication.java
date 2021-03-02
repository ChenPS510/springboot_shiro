package com.mk.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mk.chen
 * @date 2020/01/09 14:59
 */
@MapperScan(basePackages = {"com.mk.shiro.mapper"})
@SpringBootApplication
public class ShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class, args);
    }

}
