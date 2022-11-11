package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

/**
 * @ClassName: DataType
 * @Description: 数据类型
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum DataType {
    VEHICLE(1, "车辆数据"),
    VEH_DEVICE_MODEL(2, "车型零部件型号"),
    TAG(3, "车辆标签");

    private final Integer type;
    private final String desp;

    DataType(Integer type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static DataType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (DataType dataType : DataType.values()) {
            if (dataType.type.equals(type)) {
                return dataType;
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
