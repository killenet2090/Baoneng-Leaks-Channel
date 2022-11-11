package com.bnmotor.icv.tsp.device.model.response.vehDevice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: VehicleDeviceVo
 * @Description: 车辆设备信息
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDeviceVo {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "硬件版本号")
    private String hardVersion;

    @ApiModelProperty(value = "软件版本号")
    private String softwareVersion;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "设备生产序列号")
    private String productSn;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "更新日期")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "换件次数")
    private Integer replaceTimes;
}
