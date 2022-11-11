package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: TemplateStatusEnum
 * @Description: 模板状态类型
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum TemplateStatusEnum implements BaseEnum<Integer> {
    /**
     * 0审核中
     */
    CHECKING(0, "审核中"),
    /**
     * 1审核通过
     */
    PASS(1, "审核通过"),
    /**
     * 2审核不通过
     */
    FAIL(2, "审核不通过"),
    ;

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    TemplateStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
