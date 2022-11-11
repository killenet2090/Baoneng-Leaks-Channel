package com.bnmotor.icv.tsp.apigateway.common.enums;


import com.bnmotor.icv.tsp.apigateway.common.base.BaseEnum;

/**
 * @ClassName: ResultEnum
 * @Description: 结果枚举类
 * @author: huangyun1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessResultEnum implements BaseEnum<String> {
    /**
     * 鉴权参数为空
     */
    AUTHORIZED_EMPTY("A0400", "鉴权参数不能为空"),
    /**
     * 未鉴权
     */
    UNAUTHORIZED("A0401", "鉴权失败"),
    ;
    

    private String code;
    private String message;

    private BusinessResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getDescription() {
        return this.message;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
