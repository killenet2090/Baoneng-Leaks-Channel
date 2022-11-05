package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleKeyApplyDto
 * @Description: 车主蓝牙钥匙申请参数类定义
 * @author: liuyiwei
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel(description = "TspBleKeyinfoDto")
@Data
public class BleKeyApplyDto extends Model {
    private static final long serialVersionUID = -1349675218859428902L;
    /**
     * 车辆ID
     */
    @ApiModelProperty(value = "车辆ID",required = true, example = "20200712082200003")
    @NotBlank(message = "车辆ID不能为空")
    private String deviceId;

    /**
     * 移动设备ID
     */
    @ApiModelProperty(value = "移动设备ID", required = true, example = "15914176367")
    @NotBlank(message = "移动设备ID不能为空")
    private String mobileDeviceId;


    /**
     * 服务密码
     */
    @NotBlank(message = "蓝牙钥匙服务密码不能为空")
    @ApiModelProperty(value = "蓝牙钥匙服务密码",required = true, dataType = "String", example = "228611")
    @Length(max = 32, message = "[服务密码长度超出限制]")
    private String servicePwd;

    /**
     * 服务密码
     */
    @ApiModelProperty(value = "是否第一次申请，默认为1：表示非第一次申请，0表示第一次申请",required = false, dataType = "Integer", example = "1")
    @DecimalMax(value="1",message ="申请类型参数非法，必须小于2")
    private Integer applyType=1;
}
