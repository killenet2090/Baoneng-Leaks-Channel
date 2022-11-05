package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: TspBleCaPinDto
 * @Description: 蓝牙钥匙证书pin码入参实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "TspBleCaPinDto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BleDevicePinDto extends Model {
    /**
     * '项目ID'
     */
    @ApiModelProperty(value = "项目ID")
    @Length(min = 1,max = 19)
    private String projectId;

    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String  deviceId;

    /**
     * 设备MAC
     */
    @ApiModelProperty(value = "车辆MAC",required = true,dataType = "String", example = "12:88:72:84")
    private String  deviceMac;
}
