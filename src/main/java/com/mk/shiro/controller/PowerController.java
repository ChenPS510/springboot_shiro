package com.mk.shiro.controller;

import com.mk.shiro.exception.BaseResponse;
import com.mk.shiro.exception.ResponseUtil;
import com.mk.shiro.model.Power;
import com.mk.shiro.model.User;
import com.mk.shiro.service.PowerService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mk.chen
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/power")
public class PowerController {

    @Autowired
    private PowerService powerService;

    /**
     * 获取当前登录用户的权限信息
     *
     * @return
     */
    @GetMapping("getUserPowers")
    public BaseResponse getUserPowers() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Power> powerList = powerService.getUserPowers(user.getId());
        return ResponseUtil.success(powerList);
    }

}
