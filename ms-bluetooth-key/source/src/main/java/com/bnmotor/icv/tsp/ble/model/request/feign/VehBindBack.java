package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "车辆解绑回调", description = "车辆解绑回调")
public class VehBindBack {
    /**
     * 车辆识别号VIN码
     */
    private String vin;
    /**
     * 车主ID
     */
    private Long userId;
    /**
     * 业务类型，蓝牙钥匙默认为1
     */
    private Integer type=1;
    /**
     * 车下所有蓝牙蓝牙钥匙注销结果
     */
    private String respCode;
}
