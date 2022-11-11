package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: BindStatus
 * @Description: 车辆绑定状态
 * @author: zhangwei2
 * @date: 2020/8/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BindStatus {
    NOT_BIND(0, "未绑定"),
    BIND(1, "绑定"),
    UN_BIND(2, "已解绑"),
    BINDING(3, "绑定中"),
    UN_BINDING(4, "解绑中");

    private final Integer status;
    private final String desp;

    BindStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static BindStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (BindStatus vehStatus : BindStatus.values()) {
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
