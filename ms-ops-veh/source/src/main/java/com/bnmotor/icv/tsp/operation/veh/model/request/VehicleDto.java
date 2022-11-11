package com.bnmotor.icv.tsp.operation.veh.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: VehicleDto
 * @Description: 根据userId查询车辆信息入参
 * @author: zhoulong1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDto extends PageDto {

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID",name = "userId")
    private String userId;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "绑定状态",name = "status",example = "绑定--0 解绑--1")
    private Integer status;
}
