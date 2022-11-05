package com.bnmotor.icv.tsp.ble.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: UserTypeEnum
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum UserTypeEnum implements BaseEnum<Integer> {

    VEH_OWNER(1, "车主"),
    FAMILY(2, "家人"),
    FRIEND(3, "朋友"),
    OTHER(4, "其他");

    private Integer val;
    private String desc;

    UserTypeEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return val;
    }
}
