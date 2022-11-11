package com.bnmotor.icv.tsp.device.common.enums.dataaysn;

/**
 * @ClassName: ImportFailedReason
 * @Description: 导入失败原因
 * @author: zhangjianghua1
 * @date: 2020/11/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ImportFailedReason {
    VEHICLE_EXISTS(1, "车辆已经存在"),
    MODEL_CONFIG_NOT_EXIST(2, "车型配置不存在"),
    VEHICLE_DUB(1, "导入车辆重复"),
    ALREADY_BINDED(7, "该部件已经被绑定"),

    ICCID_NOT_EXIST(27, "物联网卡不存在"),
    ICCID_HAS_BINDED(28, "网联网卡已经被绑定到其他设备"),
    ICCID_DUB(29, "物联网卡重复导入"),
    DEVICE_DUB(30, "重复导入设备"),
    DEVICE_EXIST(31, "设备已存在"),
    VEH_DEVICE_SAME_TYPE_BIND(32, "车辆已经绑定同种类型设备"),
    VEH_DEVICE_SAME_TYPE_DUB(33, "车辆重复导入同种类型设备");

    private final int type;
    private final String desp;

    ImportFailedReason(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public int getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
