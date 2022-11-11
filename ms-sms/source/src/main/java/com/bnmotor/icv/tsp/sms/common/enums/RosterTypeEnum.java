package com.bnmotor.icv.tsp.sms.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: RosterTypeEnum
 * @Description: 名单类型
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum RosterTypeEnum implements BaseEnum<Integer> {
    /**
     * 1白名单
     */
    WHITE_ROSTER(1, "白名单"),
    /**
     * 2黑名单
     */
    BLACK_ROSTER(2, "黑名单"),
    ;

    /**
     * 编码
     */
    private Integer code;

    private String desc;

    RosterTypeEnum(Integer code, String desc) {
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
