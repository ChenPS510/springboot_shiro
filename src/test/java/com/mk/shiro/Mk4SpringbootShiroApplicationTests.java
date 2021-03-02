package com.mk.shiro;

import com.mk.shiro.model.User;
import com.mk.shiro.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Mk4SpringbootShiroApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }

}
