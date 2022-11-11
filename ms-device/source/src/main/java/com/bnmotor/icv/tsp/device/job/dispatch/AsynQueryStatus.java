package com.bnmotor.icv.tsp.device.job.dispatch;

/**
 * @ClassName: VehicleStatus
 * @Description: 车辆状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum AsynQueryStatus {
    UNEXECUTED(1, "未执行"),
    EXEXUTING(2, "执行中"),
    SUCCESSED(3, "执行成功"),
    FAILED(4,"执行失败");

    private final int status;
    private final String desp;

    AsynQueryStatus(int status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static AsynQueryStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (AsynQueryStatus vehStatus : AsynQueryStatus.values()) {
            if (vehStatus.status == status) {
                return vehStatus;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public String getDesp() {
        return desp;
    }
}
