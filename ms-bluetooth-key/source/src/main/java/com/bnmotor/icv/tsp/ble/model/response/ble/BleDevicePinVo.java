package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BleDevicePinVo {
    /**
     * 序号
     */
    private long id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 设备ID
     */
    private String bleDeviceId;
    /**
     * 用户类型
     */
    private String userTypeId;
    /**
     * pin码
     */
    private String pinCode;
    /**
     * 蓝牙设备MAXC
     */
    private String deviceMac;

}
