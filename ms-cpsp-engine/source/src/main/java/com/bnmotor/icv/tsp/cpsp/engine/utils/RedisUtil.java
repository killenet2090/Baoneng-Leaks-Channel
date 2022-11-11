package com.bnmotor.icv.tsp.cpsp.engine.utils;

import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisUtil
 * @Description:
 * @author liuhuaqiao1
 * @date 2020-09-16 19:59
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisProvider redisClient;

    /**
     * 生成redis存储键，外部使用
     * @param appName
     * @param keys
     * @return
     */
    public static String genKey(String appName, String... keys) {
        String redisKey = appName + ":" + String.join(":", keys);
        return redisKey.toLowerCase();
    }

    /**
     * 生成redis存储键,内部使用
     * @return
     */
    public String createKey(String appName, String... keys) {
        String redisKey = appName + ":" + String.join(":", keys);
        return redisKey.toLowerCase();
    }

    /**
     * 获取缓存对象
     * @param key
     * @param cacheClass
     * @return
     */
    public Object get(String key, Class cacheClass) {
        Object result = null;
        try {
            result = redisClient.getObject(key, cacheClass);
            log.info("成功读取缓存数据， key-{}, data-{}", key, result);
        } catch (IOException e) {
            log.error("获取缓存数据失败", e);
        }
        return result;
    }

    /**
     * 缓存序列化对象
     * @param key
     * @param duration
     * @param cacheObject
     * @return
     */
    public void set(String key, Integer duration, Object cacheObject) {
        try {
            redisClient.setObject(key, cacheObject, duration, TimeUnit.SECONDS);
            log.info("成功缓存数据， key-{}, duration-{}", key, duration);
        } catch (IOException e) {
            log.error("缓存数据失败", e);
        }
    }

    /**
     * 是否存在缓存key值
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        log.info("key-{}", key);
        return redisClient.hasKey(key);
    }

}
