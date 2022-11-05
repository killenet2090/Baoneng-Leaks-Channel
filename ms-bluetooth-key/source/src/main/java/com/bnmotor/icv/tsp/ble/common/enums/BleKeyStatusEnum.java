package com.bnmotor.icv.tsp.ble.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: BleKeyStatusEnum
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BleKeyStatusEnum implements BaseEnum<Integer> {
    UNABLE(0, "未启用"),
    ENABLE(1, "已启用"),
    DEPRECATED(2, "已作废"),
    EXPIRED(3, "已过期");

    private Integer val;
    private String desc;

    BleKeyStatusEnum(Integer val, String desc) {
        this.val =val;
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
