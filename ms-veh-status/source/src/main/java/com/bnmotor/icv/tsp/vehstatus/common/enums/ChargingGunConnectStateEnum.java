package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: ChargingDoorStateEnum
 * @Description: 充电枪状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ChargingGunConnectStateEnum implements BaseEnum<String> {
    /**
     * 未连接
     */
    NO_CONNECTED("0", "未连接"),
    /**
     * AC连接
     */
    AC_CONNECTED("1", "AC连接"),
    /**
     * DC连接
     */
    DC_CONNECTED("2", "DC连接"),
    /**
     * AC & DC连接
     */
    AC_AND_DC_CONNECTED("3", "AC&DC连接"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private ChargingGunConnectStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}