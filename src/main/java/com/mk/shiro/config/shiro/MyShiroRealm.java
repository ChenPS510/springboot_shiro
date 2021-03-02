package com.mk.shiro.config.shiro;

import com.mk.shiro.model.Power;
import com.mk.shiro.model.User;
import com.mk.shiro.service.PowerService;
import com.mk.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: mk.chen
 * @Date: 2020/1/9 15:12
 * @Description:
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {
    private PowerService powerService;
    private UserService userService;

    public MyShiroRealm() {
    }

    /**
     * 同@Autowired效果一样：把@Autowired去掉，用下面的代码，效果一样
     *
     * @param userService
     * @param powerService
     * @param authorizationCacheName
     */
    public MyShiroRealm(UserService userService, PowerService powerService, String authorizationCacheName) {
        this.userService = userService;
        this.powerService = powerService;
        super.setAuthorizationCacheName(authorizationCacheName);
    }

    /**
     * realm的名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "userRealm";
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        log.info("==> doGetAuthorizationInfo, userId is {}", user.getId());
        // 根据身份信息从数据库中查询权限数据
        List<String> permissions = powerService.getUserPowers(user.getId())
                .stream()
                .filter(power -> "BUTTON".equals(power.getPowerType()))
                .map(Power::getValue)
                .collect(Collectors.toList());
        //将权限信息封装为AuthorizationInfo
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证:验证用户输入的账号和密码是否正确。
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String phone = token.getUsername();
        log.info("=> doGetAuthorizationInfo, phone is {}", phone);
        // 从数据库获取对应用户名密码的用户
        User user = userService.findByPhone(Long.parseLong(phone));
        // 如果该手机号下无用户，抛出异常
        if (user == null) {
            throw new UnknownAccountException();
        }
        // 账户不可用
        if (!user.getEnable()) {
            throw new DisabledAccountException();
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }

}