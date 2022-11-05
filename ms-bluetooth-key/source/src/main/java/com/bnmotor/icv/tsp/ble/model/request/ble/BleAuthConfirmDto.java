package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @ClassName: TspBleAuthDto
 * @Description: 蓝牙钥匙授权入参实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleAuthConfirmDto{
    /**
     * 车辆设备ID
     */
    @ApiModelProperty(value = "车辆设备ID",required = true, example = "20200712082200003")
    @NotBlank(message = "车辆设备ID不能为空")
    @Length(min = 17,max = 17)
    private String deviceId;
    /**
     * 被授权人手机ID
     */
    @ApiModelProperty(value = "被授权人手机ID",required = true, example = "2144343j4i343u98jewrjeiwur9u")
    @NotBlank(message = "被授权人移动设备ID不能为空")
    @Length(min = 20,max = 50)
    private String usedMobileDeviceId;
    /**
     * 被授权人手机号码
     */
    @NotBlank(message = "被授权人电话号码不能为空")
    @Length(max = 11, message = "[手机号码长度不正确]")
    public String phoneNumber;
    /**
     * 授权码
     */
    @NotBlank(message = "被授权人授权码不能为空")
    @Pattern(regexp = "\\d{6}")
    public String authCode;
}
