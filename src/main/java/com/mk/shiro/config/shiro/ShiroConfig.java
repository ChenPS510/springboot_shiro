package com.mk.shiro.config.shiro;

import com.mk.shiro.config.cache.RedissonShiroCacheManager;
import com.mk.shiro.config.session.RedissonSessionDao;
import com.mk.shiro.config.session.RedissonSessionManager;
import com.mk.shiro.config.cache.CacheNames;
import com.mk.shiro.service.PowerService;
import com.mk.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: mk.chen
 * @Date: 2020/1/9 15:11
 * @Description:
 */
@Slf4j
@Configuration
public class ShiroConfig {
    @Bean
    public MyShiroRealm myShiroRealm(@Lazy UserService userService, @Lazy PowerService powerService) {
        MyShiroRealm myShiroRealm = new MyShiroRealm(userService, powerService, CacheNames.SHIRO_AUTHORIZATION_CACHE);
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义cache manager，可以用来缓存employee认证以及授权信息
     *
     * @param redisson
     * @return
     */
    @Bean
    public CacheManager shiroCacheManager(RedissonClient redisson) {
        return new RedissonShiroCacheManager(redisson);
    }

    /**
     * 自定义会话管理器，会话管理器默认从request的header中取会话的id，通过sessionDao匹配会话
     *
     * @param redisson
     * @return
     */
    @Bean
    public SessionManager sessionManager(RedissonClient redisson) {
        RedissonSessionManager redissonWebSessionManager = new RedissonSessionManager();
        //设置会话过期时间，1800秒（0.5个小时）
        redissonWebSessionManager.setGlobalSessionTimeout(1800000L);
        redissonWebSessionManager.setSessionDAO(new RedissonSessionDao(redisson, null));
        return redissonWebSessionManager;
    }

    @Bean
    public SecurityManager securityManager(MyShiroRealm myShiroRealm, CacheManager cacheManager, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, MessageSource messageSource) {
        log.info("ShiroConfiguration.shirFilter() starting...........");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义拦截器，加入自定义的tokenFilter
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("token", new TokenFilter(messageSource));
        shiroFilterFactoryBean.setFilters(filtersMap);

        // 权限控制map，登录、验证码以及微服务内部调用的接口可以匿名访问，其他的接口都要通过tokenFilter校验
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/getInfo", "anon");
        filterChainDefinitionMap.put("/user/verificationCode", "anon");
        filterChainDefinitionMap.put("/internal/**", "anon");
        filterChainDefinitionMap.put("/user/logout", "anon");
        filterChainDefinitionMap.put("/**", "token");

//        filterChainDefinitionMap.put("/user/login", "anon");
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/static/**", "anon");
//        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/user/logout", "logout");
//        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
//        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        filterChainDefinitionMap.put("/**", "authc");
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");

        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 设置shiro拦截器（filter）
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        // shiroFilter 就是上面的方法名
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * bean post processor，没有做增强，只是做了bean的生命周期管理
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 设置proxyTargetClass为true，使用cglib
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        //数据库异常处理
        mappings.setProperty("DatabaseException", "databaseError");
        mappings.setProperty("UnauthorizedException", "403");
        // None by default
        exceptionResolver.setExceptionMappings(mappings);
        // No default
        exceptionResolver.setDefaultErrorView("error");
        // Default is "exception"
        exceptionResolver.setExceptionAttribute("ex");
        // No default
        //r.setWarnLogCategory("example.MvcLogger");
        return exceptionResolver;
    }

}