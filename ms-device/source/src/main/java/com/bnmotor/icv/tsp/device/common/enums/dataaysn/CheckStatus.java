package com.bnmotor.icv.tsp.device.common.enums.dataaysn;

/**
 * @ClassName: CheckStatus
 * @Description: 任务校验状态
 * @author: zhangwei2
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum CheckStatus {
    SUCCESSED(1, "successed"),
    FAILED(2, "failed");

    private final Integer type;
    private final String desp;

    CheckStatus(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public Integer getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
