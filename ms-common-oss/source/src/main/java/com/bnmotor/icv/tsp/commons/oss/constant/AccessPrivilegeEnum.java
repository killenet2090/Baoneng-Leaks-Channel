package com.bnmotor.icv.tsp.commons.oss.constant;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: AccessPrivilegeEnum
 * @Description: bucket 访问权限枚举
 * @author: zhangjianghua1
 * @date: 2020/7/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum AccessPrivilegeEnum implements BaseEnum<String> {
    /**
     * 三种访问控制权限
     */
    READ_ONLY("R", "只读权限"),
    WRITE_ONLY("W", "只写权限"),
    READ_WRITE("R/W", "读写权限"),
    ;
    private String value;
    private String description;

    AccessPrivilegeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return value;
    }
}
