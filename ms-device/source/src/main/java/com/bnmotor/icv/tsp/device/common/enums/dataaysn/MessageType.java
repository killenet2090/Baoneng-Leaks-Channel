package com.bnmotor.icv.tsp.device.common.enums.dataaysn;

/**
 * @ClassName: MessageType
 * @Description: 消息类型
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum MessageType {
    TOTAL(1, "全量"),
    INCREMENT(2, "增量");

    private final int type;
    private final String desp;

    MessageType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static MessageType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (MessageType messageType : MessageType.values()) {
            if (messageType.type == type) {
                return messageType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
