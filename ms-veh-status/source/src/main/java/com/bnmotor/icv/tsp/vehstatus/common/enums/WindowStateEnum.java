package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: WindowStateEnum
 * @Description: 车窗状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum WindowStateEnum implements BaseEnum<Integer> {
    /**
     * 关
     */
    CLOSE(0, "关"),
    /**
     * 10%
     */
    TEN_PERCENT(10, "10%"),
    /**
     * 20%
     */
    TWENTY_PERCENT(20, "20%"),
    /**
     * 100%
     */
    HUNDRED_PERCENT(100, "100%"),
    ;

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    private WindowStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}