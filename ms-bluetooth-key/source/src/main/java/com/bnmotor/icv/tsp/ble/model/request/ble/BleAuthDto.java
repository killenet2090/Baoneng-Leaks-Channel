package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TspBleAuthDto
 * @Description: 蓝牙钥匙授权入参实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleAuthDto implements Serializable {
    @NotBlank(message = "授权人ID")
    @Length(max = 20, message = "[授权人ID长度超出限制]")
    private String ownerId;
    /**
     * 设备Id(车辆ID)
     */
    @NotBlank(message = "车辆设备ID不能为空")
    @Length(max = 32, message = "[车辆识别号长度超出限制]")
    private String deviceId;
    /**
     * 被授权人手机号
     */
    @NotBlank(message = "被授权人电话号码不能为空")
    @Length(max = 11, message = "[手机号码长度不正确]")
    private String phoneNumber;
    /**
     * 蓝牙钥匙生效时间（启始时间）
     */
    @NotBlank(message = "蓝牙钥匙生效时间")
    @Length(max = 32, message = "[车辆识别号长度超出限制]")
    private String bleKeyEffectiveTime;
    /**
     * 蓝牙钥匙失效时间，时间格式YYYY-MM-dd hh:mm:ss
     */
    @NotBlank(message = "蓝牙钥匙失效时间")
    private String bleKeyExpireTime;
    /**
     * 授予的权限
     */
    private List<Long> authList;
    /**
     *用户类型：1-车主，2-家人，3-朋友，4-其他
     */
    private Integer userType;

    /**
     * 用户别名，授权时输入
     */
    private String usedUserName;

    /**
     * 服务密码
     */
    @NotBlank(message = "蓝牙钥匙服务密码不能为空")
    @ApiModelProperty(value = "蓝牙钥匙服务密码",required = true, dataType = "String", example = "228611")
    @Length(max = 32, message = "[服务密码长度超出限制]")
    private String servicePwd;

    /**
     * 车辆授权ID(由车辆授权服务传入)
     */
    private String authId;

    /**
     * 被授权人手机ID
     */
    @ApiModelProperty(value = "被授权人手机ID",required = true, example = "2144343j4i343u98jewrjeiwur9u")
    @NotBlank(message = "被授权人移动设备ID不能为空")
    @Length(min = 20,max = 50)
    private String usedMobileDeviceId;

    /**
     * 蓝牙钥匙别名(由车辆授权服务传入)
     */
    private String bleKeyName;
}
