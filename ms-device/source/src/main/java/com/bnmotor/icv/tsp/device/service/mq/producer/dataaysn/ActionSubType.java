package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

/**
 * @ClassName: ActionType
 * @Description: 动作
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ActionSubType {
    NO_MEANING(0, "无意义"),
    VEHICLE(1, "车辆"),
    VEHICLE_DEVICE(2, "车辆零部件"),
    VEHICLE_TAG(3, "车辆标签");

    private final Integer type;
    private final String desp;

    ActionSubType(Integer type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static ActionSubType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (ActionSubType actionType : ActionSubType.values()) {
            if (actionType.type.equals(type)) {
                return actionType;
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
