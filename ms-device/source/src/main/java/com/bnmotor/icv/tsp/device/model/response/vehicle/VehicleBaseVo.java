package com.bnmotor.icv.tsp.device.model.response.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehicleBaseVo
 * @Description: 车辆基本信息
 * @author: zhangwei2
 * @date: 2021/1/7
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleBaseVo {
    @ApiModelProperty(value = "车辆唯一标识")
    private String vin;

    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;

    @ApiModelProperty(value = "车辆型号")
    private String vehModel;

    @ApiModelProperty(value = "车辆类型")
    private String vehType;

    @ApiModelProperty(value = "发动机号")
    private String engineNo;

    @ApiModelProperty(value = "工作模式")
    private Integer vehLifecircle;
}
