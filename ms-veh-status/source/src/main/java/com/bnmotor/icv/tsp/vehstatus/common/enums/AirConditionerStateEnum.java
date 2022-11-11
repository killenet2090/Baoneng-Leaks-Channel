package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: AirConditionerStateEnum
 * @Description: 空调状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum AirConditionerStateEnum implements BaseEnum<String> {
    /**
     * AC off
     */
    AC_OFF("0", "AC off"),
    /**
     * 1-Defrost or defog
     */
    DEFROST_DEFOG("1", "Defrost or defog"),
    /**
     * 2-Auto AC
     */
    AUTO_AC("2", "Auto AC"),
    /**
     * 3-Manual AC
     */
    MANUAL_AC("3", "Manual AC"),
    /**
     * 4-Air clean
     */
    AIR_CLEAN("4", "Air clean"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private AirConditionerStateEnum(String code, String desc) {
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