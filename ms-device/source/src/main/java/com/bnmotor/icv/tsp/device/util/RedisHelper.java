package com.bnmotor.icv.tsp.device.util;

/**
 * @ClassName: RedisHelper1
 * @Description: redis帮助类，用户提供redis相关操作
 * @author: zhangwei2
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class RedisHelper {
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
}
