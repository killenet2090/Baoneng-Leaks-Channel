package com.bnmotor.icv.tsp.common.data.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: CompareEnum
 * @Description: 比较枚举
 * @author: zhangjianghua1
 * @date: 2020/7/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CompareEnum implements BaseEnum {
    EQ("eq", "等于"),
    GT("gt", "大于"),
    LT("lt", "小于"),
    GE("ge", "大于等于"),
    LE("le", "小于等于"),

    ;

    private String value;
    private String description;

    CompareEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
