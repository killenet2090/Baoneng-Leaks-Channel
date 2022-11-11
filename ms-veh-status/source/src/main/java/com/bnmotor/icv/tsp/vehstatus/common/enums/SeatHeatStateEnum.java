package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: SeatHeatStateEnum
 * @Description: 座椅加热枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum SeatHeatStateEnum implements BaseEnum<String> {
    /**
     * 关闭
     */
    CLOSE("0", "关闭"),
    /**
     * 1-低温
     */
    MOD_TEMPERATURE("1", "低温"),
    /**
     * 2-中温
     */
    LOW_TEMPERATURE("2", "中温"),
    /**
     * 3-高温
     */
    HIGHT_TEMPERATURE("3", "高温"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private SeatHeatStateEnum(String code, String desc) {
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