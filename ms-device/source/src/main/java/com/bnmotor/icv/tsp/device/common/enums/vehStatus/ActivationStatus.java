package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: ActivationStatus
 * @Description: 车辆激活状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ActivationStatus {
    NO_ACTIVED(0, "未激活"),
    ACTIVED(1, "已激活"),
    FAILED_ACTIVE(2, "激活失败"),
    ACTIVING(3, "激活中");

    private final Integer status;
    private final String desp;

    ActivationStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static ActivationStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (ActivationStatus vehStatus : ActivationStatus.values()) {
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
