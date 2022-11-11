package com.bnmotor.icv.tsp.device.model.request.vehBind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: VehicleUnbindDto
 * @Description: 车辆解绑请求DTO
 * @author: huangyun1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "VehicleUnbindDto", description = "提交车辆解绑请求信息")
public class VehicleUnbindDto implements Serializable {
    private static final long serialVersionUID = 3356815490149109147L;

    /**
     * 车架号
     */
    @NotEmpty(message = "车架号不能为空！")
    @ApiModelProperty("车架号")
    private String vin;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;
}
