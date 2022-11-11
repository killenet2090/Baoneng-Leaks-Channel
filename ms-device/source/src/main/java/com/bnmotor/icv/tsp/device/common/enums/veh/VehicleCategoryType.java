package com.bnmotor.icv.tsp.device.common.enums.veh;

/**
 * @ClassName: VehicleStatus
 * @Description: 车辆状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum VehicleCategoryType {
    SUV(1, "SUV"),
    CAR(2, "轿车"),
    MPV(3, "MPV");

    private final int type;
    private final String desp;

    VehicleCategoryType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static VehicleCategoryType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (VehicleCategoryType vehicleType : VehicleCategoryType.values()) {
            if (vehicleType.type == type) {
                return vehicleType;
            }
        }
        return null;
    }

    public static VehicleCategoryType despOf(String desp) {
        for (VehicleCategoryType vehicleType : VehicleCategoryType.values()) {
            if (vehicleType.desp.equals(desp)) {
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
