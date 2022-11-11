package com.bnmotor.icv.tsp.sms.common.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @ClassName: CommonUtils
 * @Description: 抽取common工具类
 * @author: huangyun1
 * @date: 2020/7/30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class CommonUtils {
    /**
     * 设置redis序列化方式
     * @param redisTemplate
     */
    public static RedisTemplate setRedisTemplateSerializer(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.setStringSerializer(RedisSerializer.string());
        return redisTemplate;
    }
}
