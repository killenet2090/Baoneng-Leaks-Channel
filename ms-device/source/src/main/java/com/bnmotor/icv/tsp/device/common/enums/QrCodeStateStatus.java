package com.bnmotor.icv.tsp.device.common.enums;

/**
 * @ClassName: ActivationStatus
 * @Description: 车辆激活状态
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum QrCodeStateStatus {

    /**
     * 1-正常
     */
    NORMAL(1, "正常"),
    /**
     * 2-已失效
     */
    INVALID(2, "已失效"),
    /**
     * 3-已激活
     */
    ACTIVATION(3, "已激活"),
    /**
     * 4-已扫码
     */
    SCANNED(4, "已扫码"),
    /**
     * 5-已取消
     */
    CANCEL(5, "已取消"),
    ;

    private final Integer status;
    private final String desp;

    QrCodeStateStatus(Integer status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesp() {
        return desp;
    }
}
