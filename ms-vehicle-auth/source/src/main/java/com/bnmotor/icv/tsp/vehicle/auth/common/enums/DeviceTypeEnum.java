package com.bnmotor.icv.tsp.vehicle.auth.common.enums;

import lombok.Getter;

/**
 * @ClassName: DeviceTypeEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum DeviceTypeEnum {

    IHU(1, "设备类型IHU"),
    TBOX(2, "设备类型TBox");

    @Getter
    private int type;
    private String description;

    DeviceTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }
}
