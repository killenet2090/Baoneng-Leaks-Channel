package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

/**
 * @ClassName: DataType
 * @Description: 数据类型
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessType {
    ALL(0, "all"),
    OTA(1, "ota"),
    UC(2, "uc"),
    PKI(3, "pki");

    private final Integer type;
    private final String desp;

    BusinessType(Integer type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static BusinessType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.type.equals(type)) {
                return businessType;
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
