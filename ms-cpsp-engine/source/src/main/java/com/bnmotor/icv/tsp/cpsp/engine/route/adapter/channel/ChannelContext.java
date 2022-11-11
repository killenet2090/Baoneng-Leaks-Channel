package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ChannelContext
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-26 10:38
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ChannelContext {

    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<>();

    /**
     * 存储ThreadLocal变量
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Map<String, Object> threadMap = local.get();

        if (threadMap == null) {
            threadMap = new HashMap<>();
            local.set(threadMap);
        }

        threadMap.put(key, value);
    }

    /**
     * 根据key, 获取local map值
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getByKey(String key) {
        if (local.get() == null) {
            return null;
        }

        if (local.get().get(key) == null) {
            return null;
        }

        return (T) local.get().get(key);
    }

    /**
     * 清除ThreadLocal变量
     */
    public static void remove() {
        local.remove();
    }
}
