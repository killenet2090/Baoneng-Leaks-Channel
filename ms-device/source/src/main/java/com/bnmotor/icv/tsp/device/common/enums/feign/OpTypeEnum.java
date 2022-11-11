package com.bnmotor.icv.tsp.device.common.enums.feign;

/**
 * @ClassName: OpTypeEnum
 * @Description: 操作类型
 * @author: zhangjianghua1
 * @date: 2020/11/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum OpTypeEnum {
    SETTING_GUEST_MODEL(1, "设置临客模式"),
    CANCEL_GUEST_MODEL(2, "取消临客模式"),
    HU_ACTIVATE(3, "车机激活");

    private final Integer type;
    private final String desp;

    OpTypeEnum(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static OpTypeEnum valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (OpTypeEnum opType : OpTypeEnum.values()) {
            if (opType.type.equals(type)) {
                return opType;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
