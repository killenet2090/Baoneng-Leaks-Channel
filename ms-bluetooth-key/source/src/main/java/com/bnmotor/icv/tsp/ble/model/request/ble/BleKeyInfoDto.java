package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
/**
 * @ClassName: TspBleKeyinfoDto
 * @Description: 蓝牙钥匙申请入参实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "TspBleKeyInfoDto")
@Data
@Builder
public class BleKeyInfoDto extends Model {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;

    /**
     * 移动设备ID
     */
    @NotBlank(message = "移动设备ID不能为空")
    @ApiModelProperty(value = "移动设备ID",dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 50)
    private String mobileDeviceId;


    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆名字",dataType = "String", example = "1288728448789463041")
    @Length(min = 1,max = 50)
    private String deviceName;

    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号",dataType = "String", example = "1288728448789463041")
    @Length(min = 1,max = 50)
    private String deviceModel;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    @Length(min = 19,max = 19)
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    @Length(min = 19,max = 19)
    private String endTime;

}
