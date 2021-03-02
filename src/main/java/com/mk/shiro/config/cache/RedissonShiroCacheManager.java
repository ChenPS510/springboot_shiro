package com.mk.shiro.config.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author mk.chen
 */
public class RedissonShiroCacheManager implements CacheManager {

    private Codec codec;

    private RedissonClient redisson;

    private boolean allowNullValues = true;

    private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    public RedissonShiroCacheManager() {
    }

    public RedissonShiroCacheManager(RedissonClient redisson) {
        this(redisson, null);
    }

    public RedissonShiroCacheManager(RedissonClient redisson, Codec codec) {
        this.redisson = redisson;
        this.codec = codec;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache<K, V> cache = this.instanceMap.get(name);
        if (cache != null) {
            return cache;
        }

        return createMap(name);
    }

    private <K, V> Cache<K, V> createMap(String name) {
        RMap<K, Object> map = getMap(name);
        Cache<K, V> cache = new RedissonShiroCache<>(map, this.allowNullValues);
        Cache<K, V> oldCache = this.instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    protected <K> RMap<K, Object> getMap(String name) {
        if (this.codec != null) {
            return this.redisson.getMap(name, this.codec);
        }
        return this.redisson.getMap(name);
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public boolean isAllowNullValues() {
        return allowNullValues;
    }

    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }
}