package com.bnmotor.icv.tsp.ble.util;

import com.bnmotor.icv.adam.data.redis.StringRedisProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisHelper1
 * @Description: redis帮助类，用户提供redis相关操作
 * @author: shuqi1
 * @date: 2020/6/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class RedisHelper{
    @Autowired
    private StringRedisProvider redisClient;


    /**
     * 从redis中删除登录密码错误计数和用户锁定状态
     */
    public void clearLoginPwdErrorCount(String key) {
        redisClient.delete(key);
    }

    /**
     * 提供redis key的生成
     *
     * @param appName 应用名称
     * @param keys    可变key列表
     * @return key
     */
    public static String generateKey(String appName, String... keys) {
        String redisKey = appName + ":" + String.join(":", keys);
        return redisKey.toLowerCase();
    }

    /**
     * 缓存string值
     *
     * @param key      redis key
     * @param value    缓存值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void setStr(String key, String value, long timeout, TimeUnit timeUnit) {
        redisClient.setStr(key, value, timeout, timeUnit);
    }

    /**
     * 获取redis缓存的字符串值
     */
    public String getStr(String key) {
        return redisClient.getStr(key);
    }

    public Boolean hasKey(String key) {
        return redisClient.hasKey(key);
    }

    /**
     * 删除redis缓存值
     */
    public void delStr(String key) {
        redisClient.delete(key);
    }

    /**
     * 获取redis中缓存的对象
     *
     * @param key       rediskey
     * @param beanClass 实体class
     * @param <T>       泛型
     * @return 实体对象
     */
//    public <T> T getObject(String key, Class<T> beanClass) throws IOException {
//        return redisClient.getObject(key, beanClass);
//    }

    /**
     * 保存实体对象到redis
     */
    public <T> void setObject(String key, T value, long timeout, TimeUnit timeUnit) throws IOException {
        redisClient.setObject(key, value, timeout, timeUnit);
    }

    /**
     * 获取redis中key所对应值的过期时间
     */
    public long ttl(String key, TimeUnit timeUnit) {
        return redisClient.getExpire(key, timeUnit);
    }
}
