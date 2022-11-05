package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author shuqi1
 * @ClassName: BleAuthCancelPo
 * @Description: 撤销权限DTO
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-20
 */
@Data
public class BleAuthCancelDto implements Serializable {
    /**
     * 车辆设备ID
     */
    @ApiModelProperty(value = "授权ID",required = true, example = "20200712082200003")
    @NotNull(message = "授权ID必填")
    @Length(min = 18,max = 20)
    private String bleAuthId;
    /**
     * 车辆设备ID
     */
    @ApiModelProperty(value = "车辆设备ID",required = true, example = "20200712082200003")
    @NotBlank(message = "车辆设备ID不能为空")
    @Length(min = 17,max = 17)
    private String deviceId;
    /**
     * 授权手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty(value = "手机号码",required = true, example = "13899999999")
    @Length(max = 11, message = "[手机号码长度不正确]")
    private String phoneNumber;
}
