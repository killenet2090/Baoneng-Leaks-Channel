package com.bnmotor.icv.tsp.cpsp.autodecoration.common.redis;

/**
* @ClassName: RedisKey
* @Description: redis缓存键
* @author: liuhuaqiao1
* @date: 2020/11/13
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public class RedisKey {

    /**
     * 商家列表缓存key
     */
    public static final String AUTODECORATION_QUERY_SORT_KEY = "ms-cpsp-autodecoration:query:sort:%s";

    /**
     * 商家详情查询缓存key
     */
    public static final String AUTODECORATION_QUERY_NAME_KEY = "ms-cpsp-autodecoration:query:name:%s";

}
