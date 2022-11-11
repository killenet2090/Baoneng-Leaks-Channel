package com.bnmotor.icv.tsp.apigateway.common;

/**
 * @ClassName: RespCode
 * @Description: Web异常码定义
 * @author: hao.xinyue
 * @date: 2020/3/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum RespCode
{

    SERVER_DATA_ERROR("数据错误", "B0500"),

    // 第三方服务
    OTHER_SERVICE_INVOKE_ERROR("调用第三方服务出错", "C0001"),

    // 未知错误
    UNKNOWN_ERROR("未知错误", "ZOOOO"),

    // 成功
    SUCCESS("成功", "00000");

    RespCode(String value, String code)
    {
        this.code = code;
        this.value = value;
    }

    private String value;

    private String code;

    public String getValue()
    {
        return value;
    }

    public String getCode()
    {
        return code;
    }

}
