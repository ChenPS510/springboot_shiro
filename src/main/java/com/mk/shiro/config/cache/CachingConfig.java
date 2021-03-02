package com.mk.shiro.config.cache;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mk.chen
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>(4);
        config.put(CacheNames.VALIDATE_CODE, new CacheConfig(3600 * 1000, 0));
        config.put(CacheNames.SUB_DEPARTMENT, new CacheConfig(3600 * 10, 0));
        config.put(CacheNames.DEPARTMENT_DETAIL, new CacheConfig(3600 * 10, 0));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}