package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "BleUserAuthDelDto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BleUserAuthDelDto {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    private String deviceId;

    /**
     * 用户授权ID
     */
    @NotBlank(message = "用户授权ID")
    @ApiModelProperty(value = "用户授权ID",dataType = "String", example = "1288728448789463041")
    @Length(min = 1,max = 50)
    private String userAuthId;

    /**
     * 服务密码
     */
    @NotBlank(message = "蓝牙钥匙服务密码不能为空")
    @ApiModelProperty(value = "蓝牙钥匙服务密码",required = true, dataType = "String", example = "228611")
    @Length(max = 32, message = "[服务密码长度超出限制]")
    private String servicePwd;
}
