package com.bnmotor.icv.tsp.device.model.request.vehDevice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: VehDeviceRebindDto
 * @Description: 车辆零部件重新绑定对象
 * @author: zhangwei2
 * @date: 2020/12/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehDeviceUnbindDto {
    @NotBlank(message = "车架号不能为空")
    @ApiModelProperty(value = "车架号")
    @Length(max = 30, message = "车架号长度非法")
    private String vin;

    @NotNull(message = "设备类型不能为空")
    @ApiModelProperty(value = "设备类型:1-hu;2-tbox;")
    private Integer deviceType;

    @ApiModelProperty(value = "设备唯一标识符")
    @Length(max = 30, message = "设备id长度非法")
    private String deviceId;

    @ApiModelProperty(value = "更换原因")
    @Length(max = 256, message = "换件原因长度非法")
    private String replaceReason;

    @ApiModelProperty(value = "更换渠道")
    private String replaceChannel;

    @ApiModelProperty(value = "操作人")
    private String updateBy;
}
