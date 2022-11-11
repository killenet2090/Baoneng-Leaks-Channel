package com.bnmotor.icv.tsp.device.common.enums.veh;

/**
 * @ClassName: LoginStatus
 * @Description: 登录状态
 * @author: zhangjianghua1
 * @date: 2020/11/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum LoginStatus {
    LOGIN(1, "登录"),
    LOGIN_OUT(2, "退出");

    private final int status;
    private final String desp;

    LoginStatus(int status, String desp) {
        this.status = status;
        this.desp = desp;
    }

    public static LoginStatus valueOf(Integer status) {
        if (status == null) {
            return null;
        }
        for (LoginStatus loginStatus : LoginStatus.values()) {
            if (loginStatus.status == status) {
                return loginStatus;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public String getDesp() {
        return desp;
    }
}
