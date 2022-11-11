package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: VehicleStatus
 * @Description: 车辆状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum VehicleStatus {
    CREATED(1, "已创建"),
    SOLED(2, "已销售"),
    SCRAPED(3, "已报废");

    private final Integer status;
    private final String desp;

    VehicleStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static VehicleStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (VehicleStatus vehStatus : VehicleStatus.values()) {
            if (vehStatus.status.equals(status)) {
                return vehStatus;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesp() {
        return desp;
    }
}
