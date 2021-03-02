package com.mk.shiro.service;

import com.mk.shiro.config.cache.CacheNames;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @Author: mk.chen
 * @Date: 2020/1/14 19:00
 * @Description:
 */
@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private org.apache.shiro.cache.CacheManager shiroCacheManager;

    public org.apache.shiro.cache.Cache<String, SimpleAuthorizationInfo> getEmployeePowerCache() {
        return shiroCacheManager.getCache(CacheNames.SHIRO_AUTHORIZATION_CACHE);
    }

    public Cache getValidateCodeCache() {
        return cacheManager.getCache(CacheNames.VALIDATE_CODE);
    }

    public Cache getSubDepartmentCache() {
        return cacheManager.getCache(CacheNames.SUB_DEPARTMENT);
    }

    public Cache getDepartmentDetailCache() {
        return cacheManager.getCache(CacheNames.DEPARTMENT_DETAIL);
    }
}
