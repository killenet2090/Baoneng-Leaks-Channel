package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

/**
 * @ClassName: ActionType
 * @Description: 动作
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ActionType {
    ADD(1, "新增"),
    UPDATE(2, "更新"),
    DELETE(3, "删除");

    private final Integer type;
    private final String desp;

    ActionType(Integer type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static ActionType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (ActionType actionType : ActionType.values()) {
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
