package com.bnmotor.icv.tsp.device.model.response.device;

import com.bnmotor.icv.tsp.device.model.response.feign.SimVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DeviceDetailVo
 * @Description: 设备详情
 * @author: zhangwei2
 * @date: 2020/8/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DeviceDetailVo {
    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "生产序列号")
    private String productSn;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "车牌号")
    private String drivingLicPlate;

    @ApiModelProperty(value = "sim卡相关信息")
    private SimVo simVo;
}
