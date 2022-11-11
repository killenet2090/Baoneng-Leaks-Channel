package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: EnrollStatus
 * @Description: 激活状态 1 成功、 2 失败、 3 进行中
 * @author: huangyun1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum EnrollStatus {
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    ENROLLING(3, "进行中");

    private final Integer status;
    private final String desp;

    EnrollStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static EnrollStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (EnrollStatus vehStatus : EnrollStatus.values()) {
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
