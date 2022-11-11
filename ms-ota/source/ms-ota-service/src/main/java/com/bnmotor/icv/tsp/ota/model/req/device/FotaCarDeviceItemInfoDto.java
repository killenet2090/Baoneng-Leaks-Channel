package com.bnmotor.icv.tsp.ota.model.req.device;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FotaCarDeviceInfoDto
 * @Description: 车辆零件同步信息对象
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value="车辆零件同步信息对象", description="车辆零件同步信息对象")
public class FotaCarDeviceItemInfoDto implements Serializable {

    private static final long serialVersionUID = 6645972559237407525L;

    @ApiModelProperty(value = "零件类型")
    private String componentType;

    @ApiModelProperty(value = "零件类型名称")
    private String componentTypeName;

    @ApiModelProperty(value = "零件类型", required = true, example = "cCode1")
    private String componentCode;

    @ApiModelProperty(value = "零件型号:同一类型的零件不同的型号")
    private String componentModel;

    @ApiModelProperty(value = "零件名称", required = true, example = "TCU")
    private String componentName;

    @ApiModelProperty(value = "TSP设备数据老Id", example = "100086")
    private String oldDeviceId;

    @ApiModelProperty(value = "TSP设备数据新Id", example = "1000867")
    private String deviceId;

    @ApiModelProperty(value = "唯一序列码", example = "cCode1")
    private String sn;

    @ApiModelProperty(value = "固件代码固件代码由OTA平台同终端提前约定", required = true, example = "fCode1")
    private String firmwareCode;

    @ApiModelProperty(value = "软件初始版本")
    private String softwareVersion;

    @ApiModelProperty(value = "硬件初始版本")
    private String hardVersion;
}
