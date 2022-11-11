package com.bnmotor.icv.tsp.device.model.response.vehDevice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: VehDeviceBindHistory
 * @Description: 车辆设备绑定历史记录
 * @author: zhangwei2
 * @date: 2020/12/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehDeviceBindHistory {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "零部件名称")
    private String deviceName;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "设备生产序列号")
    private String productSn;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "软件版本号")
    private String softwareVersion;

    @ApiModelProperty(value = "硬件版本号")
    private String hardwareVersion;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "换件日期")
    private LocalDateTime replaceTime;
}
