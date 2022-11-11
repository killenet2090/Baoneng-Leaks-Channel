package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * @ClassName: VehicleUserRelationDto
 * @Description: 用户关联车辆请求对象
 * @author: huangyun1
 * @date: 2020/7/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "VehicleUserRelationDto", description = "用户关联车辆")
public class UserVehicleRelationDto {
    @NotBlank(message = "vin不能为空")
    @Length(min = 5, max = 32, message = "vin长度不符合")
    @ApiModelProperty(value = "车辆vin", name = "vin", required = true)
    private String vin;

    @Null(message = "非法传递uid")
    @ApiModelProperty(value = "绑定用户uid", name = "uid")
    Long uid;

    @Null(message = "购买方手机号")
    @ApiModelProperty(value = "购买方手机号", name = "userPhone", required = true)
    String purchaserPhone;

    @Null(message = "销售方名称")
    @ApiModelProperty(value = "销售方名称", name = "dealer", required = true)
    String dealer;

    @ApiModelProperty(value = "爱车名称", name = "vehName", required = true)
    String vehName;

    @NotBlank(message = "车牌号不能为空")
    @Length(min = 5, max = 10, message = "车牌号长度不符合")
    @ApiModelProperty(value = "车牌号", name = "drivingCicPlate", required = true)
    String drivingCicPlate;
}
