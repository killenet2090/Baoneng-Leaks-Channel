package com.bnmotor.icv.tsp.device.model.response.device;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DeviceModelInfoVo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/8/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class DeviceModelInfoVo {
    @ApiModelProperty(value = "设备型号ID")
    private Long deviceModelInfoId;

    @ApiModelProperty(value = "设备型号名称")
    private String deviceModelInfoName;
}
