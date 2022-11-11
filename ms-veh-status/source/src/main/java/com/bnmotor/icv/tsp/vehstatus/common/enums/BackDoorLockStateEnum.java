package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: BackDoorLockStateEnum
 * @Description: 后备箱状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BackDoorLockStateEnum implements BaseEnum<String> {
    /**
     * 1-Cinching
     */
    CINCHING("1", "Cinching"),
    /**
     * 2-Cinched
     */
    CINCHED("2", "Cinched"),
    /**
     * 3-releasing
     */
    RELEASING("3", "releasing"),
    /**
     * 4-released
     */
    RELEASED("4", "released"),
    /**
     * 5-FailueBreak
     */
    FAILUE_BREAK("5", "FailueBreak"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private BackDoorLockStateEnum(String code, String desc) {
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