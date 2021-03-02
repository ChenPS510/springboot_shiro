package com.mk.shiro.controller;

import com.mk.shiro.bean.req.LoginReq;
import com.mk.shiro.bean.resp.LoginResp;
import com.mk.shiro.exception.BaseResponse;
import com.mk.shiro.exception.BusinessException;
import com.mk.shiro.exception.ResponseUtil;
import com.mk.shiro.model.Role;
import com.mk.shiro.model.User;
import com.mk.shiro.service.AuthService;
import com.mk.shiro.service.UserRoleService;
import com.mk.shiro.service.UserService;
import com.mk.shiro.util.VerificationCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author mk.chen
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private AuthService authService;

    /**
     * 登录
     * {
     * "phone": "11111111111",
     * "password": "8a6f2805b4515ac12058e79e66539be9",
     * "requestId": "4cda7c",
     * "verificationCode": "ggd3"
     * }
     *
     * @param loginReq
     * @return
     */
    @PostMapping("login")
    public BaseResponse login(@Valid @RequestBody LoginReq loginReq) {
        //校验验证码
//        authService.validateCode(loginReq.getVerificationCode(), loginReq.getRequestId());

        UsernamePasswordToken token = new UsernamePasswordToken(loginReq.getPhone(), loginReq.getPassword());
        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            throw new BusinessException("api.error.code.auth.not_exist", "api.error.message.auth.not_exist", true);
        } catch (DisabledAccountException e) {
            throw new BusinessException("api.error.code.auth.disabled", "api.error.message.auth.disabled", true);
        } catch (AuthenticationException ae) {
            throw new BusinessException("api.error.code.auth.authenticate_fail", "api.error.message.auth.authenticate_fail", true);
        }

        //验证是否登录成功
        if (!currentUser.isAuthenticated()) {
            throw new BusinessException("api.error.code.auth.authenticate_fail", "api.error.message.auth.authenticate_fail", true);
        } else {
            User user = (User) currentUser.getPrincipal();
            LoginResp loginResp = new LoginResp();
            // token为当前登录人的sessionId
            loginResp.setToken(currentUser.getSession().getId().toString());
            LoginResp.UserInfo userInfo = new LoginResp.UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            Boolean isAdmin = user.getAdmin();
            userInfo.setIsRoot(isAdmin);
            if (!isAdmin) {
                // 获取用户的角色信息
                Role role = userRoleService.getRoleInfoByUserId(user.getId())
                        .orElseGet(() -> {
                            Role role1 = new Role();
                            role1.setId(-1);
                            role1.setName("unKnown");
                            return role1;
                        });
                userInfo.setRoleId(role.getId());
                userInfo.setRoleName(role.getName());
            } else {
                userInfo.setRoleId(null);
                userInfo.setRoleName(null);
            }
            loginResp.setUserInfo(userInfo);
            return ResponseUtil.success(loginResp);
        }
    }

    /**
     * 登出接口
     *
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse logout() {
        SecurityUtils.getSubject().logout();
        return ResponseUtil.success();
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @RequiresPermissions(value = {"platform:employee:select"})
    @GetMapping("/getUserList")
    public BaseResponse getUserList() {
        List<User> userList = userService.list();
        return ResponseUtil.success(userList);
    }


    /**
     * 获取认证结果
     *
     * @return
     */
    @GetMapping("getInfo")
    public BaseResponse getInfo() {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getPrincipal());
        boolean authenticated = subject.isAuthenticated();

        return ResponseUtil.success(authenticated);
    }


    /**
     * 图形验证码
     */
    @GetMapping("verificationCode")
    public void verificationCode(Integer width, Integer height, @RequestParam String requestId, HttpServletResponse response) throws IOException {
        String code = authService.getCode(requestId);
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        VerificationCode verificationCode = new VerificationCode();
        if (width != null && height != null) {
            verificationCode.graphicsImage(width, height, code, response.getOutputStream());
        } else {
            verificationCode.graphicsImage(code, response.getOutputStream());
        }
    }

}
