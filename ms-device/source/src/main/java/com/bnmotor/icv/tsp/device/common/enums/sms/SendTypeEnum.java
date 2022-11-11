package com.bnmotor.icv.tsp.device.common.enums.sms;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: SendTypeEnum
 * @Description: 发送终端类型
 * @author: huangyun1
 * @date: 2020/8/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum SendTypeEnum implements BaseEnum<Integer> {
    /**
     * 0后台管理平台
     */
    MANAGER_PLAT(0, "后台管理平台"),
    /**
     * 1远控app
     */
    APP(1, "远控app"),
    /**
     * 2远控车机hu
     */
    CARMACHINE(2, "远控车机hu"),
    /**
     * 3远控tbox
     */
    TBOX(3, "远控tbox");

    /**
     * 编码
     */
    private final Integer code;

    private final String desc;

    SendTypeEnum(Integer code, String desc) {
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
