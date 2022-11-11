package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleDeviceDelDto
 * @Description: 删除蓝牙钥匙入参实体
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
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
