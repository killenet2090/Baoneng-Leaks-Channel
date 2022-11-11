package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: UnBindTypeEnum
 * @Description: 车辆解绑类型
 * @author: zhoulong1
 * @date: 2020/6/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum UnBindTypeEnum implements BaseEnum<Integer> {
    TRANSFER_VEHICLE_OWNERSHIP(0,"车辆过户"),
    VEHICLE_DRAW_BACK(1, "车辆退回"),
    VEHICLE_SCRAPPING(3, "车辆报废"),
    VEHICLE_LOST(4, "车辆丢失"),
    OTHER(5,"其他");

    private final int code;
    private final String description;

    public static UnBindTypeEnum getValueByCode(int code) {
        for (UnBindTypeEnum status : UnBindTypeEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }


    UnBindTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
