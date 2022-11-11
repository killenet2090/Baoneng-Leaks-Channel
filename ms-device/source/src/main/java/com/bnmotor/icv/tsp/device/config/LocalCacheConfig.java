package com.bnmotor.icv.tsp.device.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: LocalCache
 * @Description: 本地缓存
 * @author: zhangwei2
 * @date: 2020/11/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
public class LocalCacheConfig {
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(10)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }
}

