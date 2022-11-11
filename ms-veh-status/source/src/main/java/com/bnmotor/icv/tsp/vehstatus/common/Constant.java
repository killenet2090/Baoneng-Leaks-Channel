package com.bnmotor.icv.tsp.vehstatus.common;

/**
 * @ClassName: Constant
 * @Description: 通用基础类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Constant {
    /**
     *请求头用户id
     */
    public static final String UID = "uid";
    /**
     *时间戳字段名字
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     *redis项目前缀
     */
    public static final String REDIS_PROJECT_PREFIX = "veh-status";
    /**
     *车况模块
     */
    public static final String STATUS_MODEL = "status";
    /**
     *redis连接字符
     */
    public static final String REDIS_JOINER_CHAR = ":";

    /**
     *值-1 开/解锁
     */
    public static final Integer OPEN_OR_UNLOCK = 1;

    /**
     *值-1 重发标志位
     */
    public static final Integer IS_RE_SEND = 1;

    /**
     *值-0 关/锁定
     */
    public static final Integer CLOSE_OR_LOCK = 0;
    /**
     *值-2 通风
     */
    public static final Integer WINDOW_VENTILATE = 2;
    /**
     *值-2 充电口盖中间状态
     */
    public static final Integer CHARGING_MIDDLE_STATE = 2;

}
