package com.cloud.common.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Redis工具类
 *
 * @author likain
 * @since 2023/6/10 14:58
 **/
@SuppressWarnings("all")
@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置值
     */
    public <K, V> void setCacheObject(final K key, final V value) {
        ValueOperations<K, V> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value);
    }

    /**
     * 设置值和过期时间
     */
    public <K, V> void setCacheObject(final K key, final V value, final long timeout, final TimeUnit timeUnit) {
        ValueOperations<K, V> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, value, timeout, timeUnit);
    }

    /**
     * 设置过期时间
     */
    public <K> Boolean expire(final K key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取剩余有效时间
     */
    public <K> Long getExpire(final K key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断是否有此key
     */
    public <K> Boolean hasKey(K key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取值
     */
    public <K, V> V getCacheObject(final K key) {
        ValueOperations<K, V> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(key);
    }

    /**
     * 删除值
     */
    public <K> Boolean deleteObject(final K key) {
        return redisTemplate.delete(key);
    }

    /**
     * 根据多个键批量删除值
     */
    public <K> Long deleteObject(final Collection<K> collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 设置集合
     */
    public <K, V> Long setCacheList(final K key, final List<V> dataList) {
        ListOperations<K, V> opsForList = redisTemplate.opsForList();
        return opsForList.rightPushAll(key, dataList);
    }

    /**
     * 获取集合的某个范围
     */
    public <K, V> List<V> getCacheList(final K key, final long start, final long end) {
        ListOperations<K, V> opsForList = redisTemplate.opsForList();
        return opsForList.range(key, start, end);
    }

    /**
     * 获取集合所有
     */
    public <K, V> List<V> getCacheListAll(final K key) {
        ListOperations<K, V> opsForList = redisTemplate.opsForList();
        return opsForList.range(key, 0, -1);
    }

    /**
     * 缓存Map
     */
    public <K, HK, HV> void setCacheMap(final K key, final Map<HK, HV> dataMap) {
        HashOperations<K, HK, HV> opsForHash = redisTemplate.opsForHash();
        if (dataMap != null) {
            opsForHash.putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     */
    public <K, HK, HV> Map<HK, HV> getCacheMap(final K key) {
        HashOperations<K, HK, HV> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(key);
    }

    /**
     * 往Hash中存入数据
     */
    public <K, HK, HV> void setCacheMapValue(final K key, final HK hKey, final HV value) {
        HashOperations<K, HK, HV> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     */
    public <K, HK, HV> HV getCacheMapValue(final K key, final HK hKey) {
        HashOperations<K, HK, HV> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     */
    public <K, HK, HV> List<HV> getMultiCacheMapValue(final K key, final Collection<HK> hKeys) {
        HashOperations<K, HK, HV> opsForHash = redisTemplate.opsForHash();
        return opsForHash.multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     */
    public <K, HK> Long deleteCacheMapValue(final K key, final HK hKey) {
        HashOperations<K, HK, ?> opsForHash = redisTemplate.opsForHash();
        return opsForHash.delete(key, hKey);
    }
}


