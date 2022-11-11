package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: BusinessRetEnum
 * @Description: 业务结果枚举类
 * @author: hyun1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessRetEnum implements BaseEnum<String> {
    USER_NOT_LOGIN_ERROR("B0717", "用户未登录，请先登录"),
    USER_NOT_PRIVILEGE_ERROR("B0718", "暂无该车操作权限"),
    REPEAT_OPERATOR_ERROR("B0801", "请勿重复操作"),
    QUERY_REDIS_VEH_STATUS_ERROR("B0802", "查询车况信息发生异常"),
    ;
    

    private String code;
    private String message;

    private BusinessRetEnum(String code, String message) {
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
