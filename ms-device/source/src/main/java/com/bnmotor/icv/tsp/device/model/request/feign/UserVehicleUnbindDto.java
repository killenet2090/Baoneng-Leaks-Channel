package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: VehicleUnbindDto
 * @Description: 绑定用户车辆请求对象
 * @author: zhangwei2
 * @date: 2020/7/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "vehicleUnbindDto", description = "解绑用户车辆")
public class UserVehicleUnbindDto {
    @NotBlank(message = "vin码不能为空")
    @Length(min = 5, max = 32, message = "vin长度不符合")
    @ApiModelProperty(value = "车辆vin", name = "vin", required = true)
    private String vin;

//    @NotNull(message = "解绑类型不能为空！", groups = {VehicleUnbinding.class})
//    @ApiModelProperty("解绑类型 0-车辆过户 1-车辆退回 3-车辆报废 4-车辆丢失 5-其他")
//    private Integer unbindType;
//
//    @NotNull(message = "unbindChannel不能为空", groups = {VehicleUnbinding.class})
//    @ApiModelProperty(value = "解绑渠道 1-APP、2-小程序、3-服务店、4-企业账号", name = "unbindChannel", required = true)
//    private Integer unbindChannel;
//
//    @ApiModelProperty(value = "用户车辆解绑原因", name = "vin")
//    private String unbindReason;
}
