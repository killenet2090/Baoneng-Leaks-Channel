package com.bnmotor.icv.tsp.device.model.response.vehDetail;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehInfoVo {
    String imei;
    String vin;
    String engineNo;
    String drivingLicPlate;
    LocalDateTime downlineDate;
    LocalDateTime saleDate;
}
