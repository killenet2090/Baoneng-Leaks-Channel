package com.bnmotor.icv.tsp.device.common.enums.veh;

/**
 * @ClassName: VehicleStatus
 * @Description: 车辆状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum VehicleType {
    ELECTRIC(1, "燃油"),
    FUEL(2, "纯电动"),
    MIX(3, "混动");

    private final int type;
    private final String desp;

    VehicleType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static VehicleType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (VehicleType vehicleType : VehicleType.values()) {
            if (vehicleType.type == type) {
                return vehicleType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
