package com.bnmotor.icv.tsp.sms.common;

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
     * 默认新增/更新用户
     */
    public static final String DEFAULT_USER = "system";

    /**
     *redis项目前缀
     */
    public static final String REDIS_PROJECT_PREFIX = "sms";
    /**
     *redis连接字符
     */
    public static final String REDIS_JOINER_CHAR = ":";
    /**
     *黑白名单模块
     */
    public static final String ROSTER_MODEL = "roster";
    /**
     *初始化标志位
     */
    public static final String HAS_INIT_FLAG = "hasInitFlag";
    /**
     * 启用黑白名单配置key
     */
    public static final String ENABLE_BLACK_WHITE_LIST_KEY = "tsp.sms.enable-black-white-list";
    /**
     * 是否启用发送短信功能key
     */
    public static final String ENABLE_SMS_KEY = "tsp.sms.enabled";
    /**
     * 点
     */
    public static final String DOT_CHAR = ".";

}
