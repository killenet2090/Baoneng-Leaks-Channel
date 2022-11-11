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
public class VehDeviceBindDto {
    @NotBlank(message = "车架号不能为空")
    @ApiModelProperty(value = "车架号")
    @Length(max = 30, message = "车架号长度非法")
    private String vin;

    @NotNull(message = "设备类型不能为空")
    @ApiModelProperty(value = "设备类型:1-hu;2-tbox;")
    private Integer deviceType;

    @NotBlank(message = "生产序列号不能为空")
    @ApiModelProperty(value = "零部件生产序列号")
    @Length(max = 50, message = "生产序列号长度非法")
    private String productSn;

    @ApiModelProperty(value = "操作人")
    private String updateBy;
}
