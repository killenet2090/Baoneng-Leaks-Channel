package com.bnmotor.icv.tsp.apigateway.common;

/**
 * @ClassName: Constant
 * @Description: 通用基础类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Constant {
    /**
     * 忽略全局鉴权过滤器属性名称
     */
    public static final String IGNORE_AUTH_GLOBAL_FILTER = "@IgnoreAuthenticationGatewayFilter";
    /**
     * 请求头用户id
     */
    public static final String UID = "uid";
    /**
     * 请求头projectId
     */
    public static final String PROJECT_ID = "projectId";
    /**
     * 请求头设备id
     */
    public static final String DEVICE_ID = "deviceId";
    /**
     * 请求头vin
     */
    public static final String VIN = "vin";
    /**
     * 请求头鉴权参数名称
     */
    public static final String AUTHORIZATION = "Authorization";

}
