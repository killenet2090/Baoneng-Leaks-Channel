package com.bnmotor.icv.tsp.device.model.response.device;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DeviceSimVo
 * @Description: 车辆绑定的物联网卡
 * @author: zhangwei2
 * @date: 2020/12/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DeviceSimVo {
    @ApiModelProperty(value = "车架号")
    private String vin;
    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    @ApiModelProperty(value = "物联网卡号")
    private String iccid;
}
