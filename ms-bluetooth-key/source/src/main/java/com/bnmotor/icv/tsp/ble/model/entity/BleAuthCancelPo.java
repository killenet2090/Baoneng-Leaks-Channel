package com.bnmotor.icv.tsp.ble.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shuqi1
 * @ClassName: BleAuthCancelPo
 * @Description: 撤销权限PO
 * 蓝牙授权时授予的角色 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-20
 */
@Data
public class BleAuthCancelPo implements Serializable {
    /**
     * 车辆设备ID
     */
    private String deviceId;
    /**
     * 授权手机号码
     */
    private String phoneNumber;
}
