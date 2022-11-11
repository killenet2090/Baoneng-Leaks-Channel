package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: QualityInspectStatus
 * @Description: 质检状态
 * @author: zhangwei2
 * @date: 2020/8/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum QualityInspectStatus {
    NO_PASS(0, "未通过"),
    PASS(1, "通过");

    private final int status;
    private final String desp;

    QualityInspectStatus(int status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static QualityInspectStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (QualityInspectStatus vehStatus : QualityInspectStatus.values()) {
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
