package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: ChargingDoorStateEnum
 * @Description: 充电口盖状态枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ChargingDoorStateEnum implements BaseEnum<String> {
    /**
     * 打开
     */
    OPEN("0", "打开"),
    /**
     * 中间状态
     */
    MIDDLE("1", "中间状态"),
    /**
     * 关闭
     */
    CLOSE("2", "关闭"),
    ;

    /**
     * 编码
     */
    private String code;

    private String desc;

    private ChargingDoorStateEnum(String code, String desc) {
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