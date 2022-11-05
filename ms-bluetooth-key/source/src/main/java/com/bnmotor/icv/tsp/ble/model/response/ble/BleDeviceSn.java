package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BleDeviceSn {
    private String deviceModel;
    private String deviceType;
    private String deviceId;
    private String hardVersion;
    private String sorfwareVersion;
    private String name;
    private String productSn;
    private String supplierName;
    private String updateTime;
}
