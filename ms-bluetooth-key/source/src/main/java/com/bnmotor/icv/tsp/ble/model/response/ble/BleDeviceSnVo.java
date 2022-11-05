package com.bnmotor.icv.tsp.ble.model.response.ble;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BleDeviceSnVo implements Serializable {
    private List<BleDeviceSn> BleDeviceSns;
}
