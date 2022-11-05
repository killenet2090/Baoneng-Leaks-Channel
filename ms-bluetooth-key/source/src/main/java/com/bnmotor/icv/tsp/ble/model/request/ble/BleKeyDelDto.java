package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BleKeyDelDto  {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;

    /**
     * 蓝牙钥匙ID
     */
    @NotBlank(message = "蓝牙钥匙ID不能为空")
    @ApiModelProperty(value = "蓝牙钥匙ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    private String bleKeyId;

    /**
     * 蓝牙钥匙注销时间
     */
    @NotBlank(message = "蓝牙钥匙注销时间不能为空")
    @ApiModelProperty(value = "蓝牙注销时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    private  String bleKeyDestroyTime;


    /**
     * 服务密码
     */
    @ApiModelProperty(value = "蓝牙钥匙服务密码",required = false, dataType = "String", example = "228611")
    private  String servicePwd;


    @ApiModelProperty(value = "刪除类型(2->手动删除触发 3->到期自动删除触发,默认为2)",required = false, dataType = "Integer", example = "2")
    private  Integer delType=2;
}
