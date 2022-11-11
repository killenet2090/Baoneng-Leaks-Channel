package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "更新车辆信息请求对象", description = "更新车辆信息请求对象")
public class UpdateVehInfoBluDto {
    /**
     * 车辆识别号vin
     */
    @ApiModelProperty(value = "车辆识别号vin")
    private String deviceId;

    @ApiModelProperty(value = "车牌号")
    String deviceName;

}
