/*
 *
 *
 *
 */
package com.art.service;

import java.util.Map;

/**
 * Service - 缓存
 */
public interface CacheService {

    void add(String key, Object value, int expiration);

    boolean safeAdd(String key, Object value, int expiration);

    /**
     * @param key
     * @param value
     * @param expiration unit/seconds
     */
    void set(String key, Object value, int expiration);

    boolean safeSet(String key, Object value, int expiration);

    void replace(String key, Object value, int expiration);

    boolean safeReplace(String key, Object value, int expiration);

    /**
     * 根据key获取数据
     *
     * @param key
     * @return
     */
    Object get(String key);

    Object getStr(String key);

    /**
     * 获取多个缓存
     *
     * @param keys
     * @return
     */
    Map<String, Object> get(String[] keys);

    /**
     * 原子递增
     *
     * @param key
     * @return
     */
    long incr(String key);

    /**
     * 原子递减
     *
     * @param key
     * @param by
     * @return
     */
    long decr(String key, int by);

    /**
     * 清除redis 缓存
     */
    void clear();


    /**
     * 根据key删除对应的缓存值
     *
     * @param key
     */
    void delete(String key);

    boolean safeDelete(String key);


    void init();

    /**
     * 缓存中是否存在
     *
     * @param key
     * @return
     */
    boolean hasExists(String key);

    /**
     * 获取存储在red is中的对象
     * 亲测有效
     *
     * @param str
     * @return
     */
    Object getObj(String str);

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime);

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId);

    /**
     * 信息入队
     *
     * @param key
     * @param values
     * @return
     */
    boolean leftPush(String key, Object values);

    /**
     * 信息出列
     *
     * @param key
     */
    String leftPop(String key);

    Long expire(String key, int expiration);

    /**
     * 获取expire时间
     *
     * @param key
     * @return
     */
    Long ttl(String key);
}