package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: InteceptTypeEnum
 * @Description: 拦截类型
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum InteceptTypeEnum implements BaseEnum<Integer> {
    /**
     * 1放行全部
     */
    PASS_ALL(1, "放行全部"),
    /**
     * 2拦截全部
     */
    INTECEPT_ALL(2, "拦截全部"),
    ;

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    InteceptTypeEnum(Integer code, String desc) {
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
