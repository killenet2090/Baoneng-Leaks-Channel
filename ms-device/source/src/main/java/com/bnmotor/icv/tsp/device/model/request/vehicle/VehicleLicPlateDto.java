package com.bnmotor.icv.tsp.device.model.request.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 车牌号更新参数
 */
@Data
public class VehicleLicPlateDto implements Serializable {

    /**
     * 车架号
     */
    @NotEmpty(message = "车架号不能为空！")
    @ApiModelProperty("车架号")
    private String vin;

    /**
     * 车牌号
     */
    @NotEmpty(message = "车牌号不能为空！")
    @ApiModelProperty("车牌号")
    private String drivingLicPlate;
}
