package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: DelFlagEnum
 * @Description: 删除标志类型
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum DelFlagEnum implements BaseEnum<Integer> {
    /**
     * 0正常
     */
    NORMAL(0, "正常"),
    /**
     * 1删除
     */
    DELETE(1, "删除"),
    ;

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    DelFlagEnum(Integer code, String desc) {
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