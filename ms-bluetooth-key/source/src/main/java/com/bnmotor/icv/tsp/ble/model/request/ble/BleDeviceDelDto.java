package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BleDeviceDelDto {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;

    /**
     * 蓝牙钥匙注销时间
     */
    @NotBlank(message = "蓝牙钥匙注销时间不能为空")
    @ApiModelProperty(value = "蓝牙注销时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    private  String bleKeyDestroyTime;
}
