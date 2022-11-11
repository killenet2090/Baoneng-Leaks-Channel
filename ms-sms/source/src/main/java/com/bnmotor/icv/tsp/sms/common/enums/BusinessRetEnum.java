package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: BusinessResultEnum
 * @Description: 业务结果枚举类
 * @author: hyun1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessRetEnum implements BaseEnum<String> {
    /**
     * 映射模板id输入有误
     */
    TEMPLATE_ID_ERROR("B0903", "映射模板id输入有误"),
    /**
     * 短信模板暂未审核通过
     */
    TEMPLATE_ID_NO_PASS("B0904", "短信模板暂未审核通过"),
    /**
     * 短信模板暂未审核通过
     */
    INTECEPT_RETURN("B0905", "发送短信受到拦截返回"),
    ;
    

    private String code;
    private String message;

    BusinessRetEnum(String code, String message) {
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
