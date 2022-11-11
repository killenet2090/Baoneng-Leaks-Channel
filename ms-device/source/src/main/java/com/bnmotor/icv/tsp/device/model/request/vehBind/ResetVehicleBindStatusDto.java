package com.bnmotor.icv.tsp.device.model.request.vehBind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: ResetVehicleUserBindStatusDto
 * @Description: 重置用户车辆绑定状态请求对象
 * @author: huangyun1
 * @date: 2020/7/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "ResetVehicleUserBindStatusDto", description = "重置用户车辆绑定状态请求对象")
public class ResetVehicleBindStatusDto {
    @NotBlank(message = "vin码不能为空")
    @Length(min = 5, max = 32, message = "vin长度不符合")
    @ApiModelProperty(value = "车辆vin", name = "vin", required = true)
    private String vin;

    @NotNull(message = "绑定状态不能为空！")
    @ApiModelProperty(value = "绑定状态 0-未绑定；1-已绑定 3-绑定中 4-解绑中", required = true)
    private Integer bindStatus;

    @NotNull(message = "原绑定状态不能为空")
    @ApiModelProperty(value = "原绑定状态 0-未绑定；1-已绑定 3-绑定中 4-解绑中", name = "oldBindStatus", required = true)
    private Integer oldBindStatus;

    @NotNull(message = "用户id不能为空！")
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

}
