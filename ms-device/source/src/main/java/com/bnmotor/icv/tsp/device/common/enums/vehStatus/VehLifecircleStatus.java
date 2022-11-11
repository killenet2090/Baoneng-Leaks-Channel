package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

/**
 * @ClassName: CheckStatus
 * @Description: 任务校验状态
 * @author: zhangjianghua1
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum VehLifecircleStatus {
    FACTORY_MODE(0, "工厂模式"),
    EXHIBITION_MODE(1, "展车模式"),
    SALE_MODE(2, "销售模式"),
    USER_MODE(3, "用户模式"),
    SCRAP_MODE(4, "报废模式"),
    GUEST_MODE(5, "临客模式"),
    GUEST_MODE_OPENING(51, "临客模式开启中"),
    GUEST_MODE_CLOSING(52, "临客模式关闭中");

    private final Integer status;
    private final String desp;

    VehLifecircleStatus(int status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static VehLifecircleStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }

        for (VehLifecircleStatus vehStatus : VehLifecircleStatus.values()) {
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
