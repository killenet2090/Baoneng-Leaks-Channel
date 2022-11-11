package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: FromTypeEnum
 * @Description: 来源类型枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum FromTypeEnum implements BaseEnum<Integer> {
    /**
     * 管理平台
     */
    MANAGER_PLAT(0, "管理平台"),
    /**
     * 手机app
     */
    APP(1, "手机app"),
    /**
     * 车机
     */
    CARMACHINE(2, "车机"),
    /**
     * tbox
     */
    TBOX(3, "tbox");

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    FromTypeEnum(Integer code, String desc) {
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
