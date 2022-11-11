package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: ChargingStateEnum
 * @Description: 充电状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BmsChargingStateEnum implements BaseEnum<String> {
    /**
     * 默认
     */
    DEFAULT("0", "默认"),
    /**
     * 充电准备
     */
    PREPARE_CHARGING("1", "充电准备"),
    /**
     * 充电中
     */
    CHARGING("2", "充电中"),
    /**
     * 充电完成
     */
    COMPLETE_CHARGING("3", "充电完成"),

    /**
     * 充电发生异常
     */
    ERROR_CHARGING("4", "充电发生异常"),

    /**
     * 预约充电
     */
    APPOINTMENT_CHARGE("7", "预约充电"),

    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private BmsChargingStateEnum(String code, String desc) {
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