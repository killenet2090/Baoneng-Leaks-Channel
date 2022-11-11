package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: BusinessTypeEnum
 * @Description: 业务类型枚举类
 * @author: huangyun1
 * @date: 2020/12/09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum BusinessTypeEnum implements BaseEnum<Integer> {
    /**
     * 开始充电提醒
     */
    CHARGING_START_NOTIFY(1, "开始充电提醒"),
    /**
     * 充电完成提醒
     */
    CHARGING_FINISH_NOTIFY(2, "充电完成提醒"),
    /**
     * 开始充电提醒
     */
    CHARGING_EXCEPTION_NOTIFY(3, "充电异常提醒"),
    /**
     * 同步车辆状态信息
     */
    SYNC_VEH_STATUS(4, "同步车辆状态信息"),
    /**
     * 车辆状态发生改变通知
     */
    VEH_STATUS_CHANGE_NOTIFY(5, "车辆状态发生改变通知"),
    ;


    private Integer code;
    private String message;

    BusinessTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getDescription() {
        return this.message;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
