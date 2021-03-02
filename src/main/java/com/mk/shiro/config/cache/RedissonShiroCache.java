package com.mk.shiro.config.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.redisson.api.RMap;
import org.redisson.spring.cache.NullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @param <K>
 * @param <V>
 * @author mk.chen
 */
public class RedissonShiroCache<K, V> implements Cache<K, V> {

    private final RMap<K, Object> map;

    private final boolean allowNullValues;

    public RedissonShiroCache(RMap<K, Object> map, boolean allowNullValues) {
        this.map = map;
        this.allowNullValues = allowNullValues;
    }

    @Override
    public V get(K key) throws CacheException {
        Object value = this.map.get(key);
        return fromStoreValue(value);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        Object previous;
        if (!allowNullValues && value == null) {
            previous = map.remove(key);
            return fromStoreValue(previous);
        }

        Object val = toStoreValue(value);
        previous = map.put(key, val);
        return fromStoreValue(previous);
    }

    @Override
    public V remove(K key) throws CacheException {
        Object previous = this.map.remove(key);
        return fromStoreValue(previous);
    }

    @Override
    public void clear() throws CacheException {
        this.map.clear();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Set<K> keys() {
        return this.map.readAllKeySet();
    }

    @Override
    public Collection<V> values() {
        Collection<Object> innerValues = this.map.readAllValues();
        Collection<V> res = new ArrayList<>(innerValues.size());
        for (Object val : innerValues) {
            res.add(fromStoreValue(val));
        }
        return res;
    }

    private V fromStoreValue(Object storeValue) {
        if (storeValue instanceof NullValue) {
            return null;
        }
        return (V) storeValue;
    }

    private Object toStoreValue(V userValue) {
        if (userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

}