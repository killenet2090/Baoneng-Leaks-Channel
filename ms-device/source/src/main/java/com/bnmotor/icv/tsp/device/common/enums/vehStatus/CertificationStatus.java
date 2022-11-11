package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: CertificationStatus
 * @Description: 车辆认证状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CertificationStatus {
    /**
     * 车辆认证状态；1-未认证，2-已认证
     */
    NO_CERTIFIED(0, "未认证"),
    CERTIFIED(1, "已认证");

    private final Integer status;
    private final String desp;

    CertificationStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static CertificationStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (CertificationStatus certStatus : CertificationStatus.values()) {
            if (certStatus.status.equals(status)) {
                return certStatus;
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
