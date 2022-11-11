package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: SeatVentStateEnum
 * @Description: 座椅通风枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum SeatVentStateEnum implements BaseEnum<String> {
    /**
     * 关闭
     */
    CLOSE("0", "关闭"),
    /**
     * 1-lv1 一级
     */
    LV1("1", "lv1 一级"),
    /**
     * 2-lv2 二级
     */
    LV2("2", "lv2 二级"),
    /**
     * 3-lv3 三级
     */
    LV3("3", "lv3 三级"),
    /**
     * 4-短循环
     */
    SHORT_CYCLE("4", "短循环"),
    /**
     * 5-打开循环
     */
    OPEN_CYCLE("5", "打开循环"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private SeatVentStateEnum(String code, String desc) {
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