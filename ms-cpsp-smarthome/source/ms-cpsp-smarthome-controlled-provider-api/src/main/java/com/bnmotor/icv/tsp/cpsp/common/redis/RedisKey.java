package com.bnmotor.icv.tsp.cpsp.common.redis;

/**
* @ClassName: RedisKey
* @Description: redis缓存键
* @author: liuhuaqiao1
* @date: 2020/11/13
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public class RedisKey {

    public static final String SMART_HOME_BIND_ACCOUNT_STATUS_API = "ms-cpsp-smarthome-provider-api:smarthome:bind:account:status:%s";
    public static final String SMART_HOME_ACCOUNT_QRCODE_API = "ms-cpsp-smarthome-provider-api:smarthome:account:qrcode:%s";
    public static final String SMART_HOME_UNBIND_ACCOUNT_API = "ms-cpsp-smarthome-provider-api:smarthome:unbind:account:%s";
    public static final String SMART_HOME_EQUIPMENT_LIST = "ms-cpsp-smarthome-provider-api:smarthome:equipment:list:%s";
    public static final String SMART_HOME_EQUIPMENT_STATUS = "ms-cpsp-smarthome-provider-api:smarthome:equipment:status:%s";
    public static final String SMART_HOME_EQUIPMENT_CONTROL = "ms-cpsp-smarthome-provider-api:smarthome:equipment:control:%s";
    public static final String SMART_HOME_EQUIPMENT_POSITION = "ms-cpsp-smarthome-provider-api:smarthome:equipment:postion:%s";
    public static final String SMART_HOME_SCENE_LIST = "ms-cpsp-smarthome-provider-api:smarthome:scene:list:%s";
    public static final String SMART_HOME_SCENE_DETAILS = "ms-cpsp-smarthome-provider-api:smarthome:scene:details:%s";
    public static final String SMART_HOME_SCENE_EXECUTION = "ms-cpsp-smarthome-provider-api:smarthome:scene:execution:%s";
    public static final String SMART_HOME_EQUIPMENT_COLLECTION = "ms-cpsp-smarthome-provider-api:smarthome:equipment:collection:%s";

    /**
     * 空气质量缓存key
     */
    public static final String REMOTE_VEH_INFO_KEY = "ms-cpsp-smarthome:veh:info:%s";

    /**
     * 空气质量缓存key
     */
    public static final String REMOTE_VEH_CONTROL_AIR_OFFON_KEY = "ms-cpsp-smarthome:veh:control:air:offon:%s";

    /**
     * 空气质量缓存key
     */
    public static final String REMOTE_VEH_CONTROL_AIR_TEMP_KEY = "ms-cpsp-smarthome:veh:control:air:temp:%s";

    /**
     * 空气质量缓存key
     */
    public static final String REMOTE_VEH_CONTROL_WINDOWS_SET_KEY = "ms-cpsp-smarthome:veh:control:windows:%s";

}
