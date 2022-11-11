package com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis;

/**
* @ClassName: RedisKey
* @Description: redis缓存键
* @author: liuhuaqiao1
* @date: 2020/11/13
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public class RedisKey {

    /**
     * 违章查询缓存key
     */
    public static final String VEHVIOLATION_QUERY_VIN_KEY = "ms-cpsp-vehviolation:query:vin:%s";

    /**
     * 违章查询缓存key
     */
    public static final String VEHVIOLATION_HOTSPOT_QUERY_LNGANDLAT_KEY = "ms-cpsp-vehviolation:hotspot:query:lng:%s:lat:%s";

    /**
     * 违章查询缓存key
     */
    public static final String VEHVIOLATION_ALERT_QUERY_VIN_KEY = "ms-cpsp-vehviolation:alert:query:vin:%s";

    /**
     * 违章办理缓存key
     */
    public static final String VEHVIOLATION_HANDLE_VIN_KEY = "ms-cpsp-vehviolation:handle:vin:%s";

    /**
     * 违章办理缓存key
     */
    public static final String VEHVIOLATION_HANDLE_RESULT_ORDERNO_KEY = "ms-cpsp-vehviolation:handle:result:orderno:%s";

}
