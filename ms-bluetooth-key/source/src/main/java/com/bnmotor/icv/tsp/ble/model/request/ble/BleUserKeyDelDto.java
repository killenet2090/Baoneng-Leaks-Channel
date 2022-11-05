package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "BleKeyDelDto")
@Data
public class BleUserKeyDelDto extends Model {
    private static final long serialVersionUID = -1349675218859428902L;
    /**
     * 蓝牙钥匙ID
     */
    @NotBlank(message = "借车人ID")
    @ApiModelProperty(value = "借车人ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 1,max = 20)
    public String usedUserId;

    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;

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


    @ApiModelProperty(value = "刪除类型(1:表示新增加：2表示手动删除；3：表示到期删除；4：离线删除；5：表示手动更新；6：表示卸载后更新)",required = false, dataType = "Integer", example = "0")
    private  Integer delType;
}
