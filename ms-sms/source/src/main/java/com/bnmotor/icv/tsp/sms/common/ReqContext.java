package com.bnmotor.icv.tsp.sms.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ReqContext
 * @Description: 请求线程上下文，线程安全的
 * @author: zhangwei2
 * @date: 2020/5/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ReqContext {
    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<>();

    /**
     * 设置用户id
     *
     * @param uid 用户id
     */
    public static void setUid(Long uid) {
        Map<String, Object> threadMap = local.get();
        if (threadMap == null) {
            threadMap = new HashMap<>(1);
            local.set(threadMap);
        }
        threadMap.put(Constant.UID, uid);
    }

    /**
     * 获取用户uid
     *
     * @return 用户uid
     */
    public static Long getUid() {
        if (local.get() != null && local.get().get(Constant.UID) != null) {
            return (Long)local.get().get(Constant.UID);
        }

        return null;
    }

    public static void remove() {
        local.remove();
    }
}
