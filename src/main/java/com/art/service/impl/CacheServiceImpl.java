/*
 *
 *
 *
 */
package com.art.service.impl;

import com.art.service.CacheService;
import com.art.utils.PropertiesUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Service - 缓存
 */
@Service("cacheServiceImpl")
@Slf4j
public class CacheServiceImpl implements CacheService {


    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";

    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    public static volatile boolean redisCacheEnable = true;

    private static CacheServiceImpl instance;
    private JedisPool jedisPool;

    public static CacheServiceImpl getInstance() {
        if (instance == null) {
            synchronized (CacheServiceImpl.class) {
                instance = new CacheServiceImpl();
            }
        }
        return instance;
    }

    private CacheServiceImpl() {
        this.jedisPool = buildJedisConnectionPool(PropertiesUtils.getInstance().getValue("spring.redis.config"));
    }

    public static JedisPool buildJedisConnectionPool(String param) {
        try {
            JedisPoolConfig e = new JedisPoolConfig();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            HashMap params = (HashMap) mapper.readValue(param, HashMap.class);
            int min = 10;
            int max = 1000;
            int maxWait = 10000;
            int port = 6379;
            int timeout = 20000;
            int db = 1;
            String host = "127.0.0.1";
            String password = "";
            if (params.get("min") != null) {
                min = ((Integer) params.get("min")).intValue();
            }

            if (params.get("max") != null) {
                max = ((Integer) params.get("max")).intValue();
            }

            if (params.get("maxWait") != null) {
                maxWait = ((Integer) params.get("maxWait")).intValue();
            }

            if (params.get("host") != null) {
                host = (String) params.get("host");
            }

            if (params.get("port") != null) {
                port = ((Integer) params.get("port")).intValue();
            }

            if (params.get("timeout") != null) {
                timeout = ((Integer) params.get("timeout")).intValue();
            }

            if (params.get("db") != null) {
                db = ((Integer) params.get("db")).intValue();
            }

            if (params.get("password") != null) {
                password = (String) params.get("password");
            }

            e.setMinIdle(min);
            e.setMaxIdle(min);
            e.setTestWhileIdle(false);
            e.setTestOnBorrow(true);
            e.setTestOnReturn(false);
            e.setTimeBetweenEvictionRunsMillis(60000L);
            e.setMaxTotal(max);
            e.setMaxWaitMillis(maxWait);
            JedisPool jedisPool = null;
            if (StringUtils.isNotBlank(password)) {
                jedisPool = new JedisPool(e, host, port, timeout, password);
            } else {
                jedisPool = new JedisPool(e, host, port, timeout);
            }
            jedisPool.getResource().select(db);
            return jedisPool;
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }

    public static Jedis getJedisConnection(JedisPool jedisPool) {
        try {
            if (jedisPool == null) {
                log.error("can not get redis connection from pool!");
                return null;
            }
            Jedis e = (Jedis) jedisPool.getResource();
            if (e != null) {
                return e;
            }
        } catch (JedisConnectionException var5) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
            var5.printStackTrace();
        }
        log.error("jedis 连接资源获取失败，请检查网络情况或者增大配置的连接池数量!2秒钟后将尝试获取链接");
        return null;
    }

    public static void closeJedisConnection(JedisPool jedisPool, Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }

    }

    public void add(String key, Object value, int expiration) {
        this.set(key, value, expiration);
    }

    public Object get(String key) {
        return this.get(key, Object.class, 0);
    }

    public Object getStr(String key) {
        return this.get(key, Object.class, 1);
    }

    /**
     * type 转换类型 0 1
     *
     * @param key
     * @param clazz
     * @param type
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz, int type) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        Object obj = null;
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return null;
        }
        try {

            byte[] e;
            if (type == 0) {
                e = jedis.get(key.getBytes());
            } else {
                Object data = jedis.get(key.getBytes()); // 目标对象，需要序列化为 byte []
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(data);
                e = baos.toByteArray();
            }
            if (e != null) {
                final ByteArrayInputStream bis = new ByteArrayInputStream(e);
                ObjectInputStream ois = new ObjectInputStream(bis) {
                    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                        return Class.forName(desc.getName(), false, ClassLoader.getSystemClassLoader());
                    }
                };
                obj = ois.readObject();
            }
        } catch (Exception var11) {
            log.error("redis.cache get exception, use get return string method :" + var11.getMessage(), var11);
            obj = jedis.get(key);
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

        return (T) obj;
    }

    public void clear() {
        log.warn("redis.cache clear but not execute");
    }

    public void delete(String key) {
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return;
        }
        try {
            jedis.del(new byte[][]{key.getBytes()});
        } catch (Exception var7) {
            var7.printStackTrace();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

    }

    public Map<String, Object> get(String[] keys) {
        HashMap result = new HashMap(keys.length);
        String[] arr$ = keys;
        int len$ = keys.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String key = arr$[i$];
            result.put(key, this.get(key));
        }

        return result;
    }

    public long incr(String key) {
        long v = -1L;
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return v;
        }
        try {
            v = jedis.incr(key);
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

        return v;
    }

    public long decr(String key, int by) {
        long v = -1L;
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return v;
        }
        try {
            v = jedis.decrBy(key.getBytes(), (long) by).longValue();
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

        return v;
    }

    public void replace(String key, Object value, int expiration) {
        this.set(key, value, expiration);
    }

    public boolean safeAdd(String key, Object value, int expiration) {
        try {
            this.add(key, value, expiration);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean safeDelete(String key) {
        try {
            this.delete(key);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public boolean safeReplace(String key, Object value, int expiration) {
        try {
            this.replace(key, value, expiration);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean safeSet(String key, Object value, int expiration) {
        try {
            this.set(key, value, expiration);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public void set(String key, Object value, int expiration) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return;
        }
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(e);
            oos.writeObject(value);
            jedis.set(key.getBytes(), e.toByteArray());
            if (expiration > 0) {
                jedis.expire(key.getBytes(), expiration);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

    }

    public Long expire(String key, int expiration) {
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            log.info("未获取到redis服务信息");
            return null;
        }
        try {
            if (expiration > 0) {
                return jedis.expire(key.getBytes(), expiration);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }
        return null;
    }

    public void stop() {
        log.warn("redis.cache stop but not execute, because should not execute");
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public String info() {
        Jedis jedis = getJedisConnection(this.jedisPool);
        if (jedis == null) {
            return "redis can not get pool info";
        }
        String var2;
        try {
            var2 = jedis.info();
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }

        return var2;
    }

    public void init() {
        if (instance != null) {
            log.info("redis cache have exist ");
            return;
        }
        if (PropertiesUtils.getInstance().getValue("redis.cache").equals("enabled")) {
            try {
                log.info("==================Connected to redis==================");
                getInstance();
            } catch (Exception e) {
                log.error("Error while connecting to redis", e);
                instance = new CacheServiceImpl();
            }
        } else {
            log.info("there have no cache started!");
        }
    }

    /**
     * 缓存中是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean hasExists(String key) {
        boolean exists = false;
        if (getInstance().get(key) != null) {
            exists = true;
        }
        return exists;
    }

    public Object getObj(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Jedis jedis = getJedisConnection(this.jedisPool);
        ByteArrayInputStream bais = null;
        byte[] bytes = null;
        try {
            bytes = jedis.get(key.getBytes());
            if (bytes == null) {
                return null;
            }
            bais = new ByteArrayInputStream(jedis.get(key.getBytes()));
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            log.error("Get object fail!", e);
        } finally {
            closeJedisConnection(this.jedisPool, jedis);
        }
        return null;

    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        try (Jedis jedis = getJedisConnection(this.jedisPool)) {
            String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            //System.out.println("result :" + result + "requestId ：" + requestId);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            log.error("取分布式锁", e);
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        try (Jedis jedis = getJedisConnection(this.jedisPool)) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            log.error("释放分布式锁", e);
        }
        return false;

    }

    /**
     * 信息入队
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public boolean leftPush(String key, Object values) {
        boolean result = false;
        try (Jedis jedis = getJedisConnection(this.jedisPool)) {
            long retVal = jedis.lpush(key, values.toString());
            if (retVal > 0) {
                result = true;
            }
        } catch (Exception e) {
            log.error("队列信息插入异常", e);
        }
        return result;
    }


    /**
     * 信息出队(消费消息)
     *
     * @param key
     * @return
     */
    public String leftPop(String key) {
        String result = "";
        try (Jedis jedis = getJedisConnection(this.jedisPool)) {
            result = jedis.lpop(key);
        } catch (Exception e) {
            log.error("取分布式锁", e);
        }
        return result;
    }


    /**
     * 获取expire时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        try (Jedis jedis = getJedisConnection(this.jedisPool)) {
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("get ttl expire", e);
        }
        return null;
    }


}
