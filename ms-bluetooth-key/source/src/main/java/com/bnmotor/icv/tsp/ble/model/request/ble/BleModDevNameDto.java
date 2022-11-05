package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleKeyModifyDateDto
 * @Description: 修改蓝牙钥匙有效期入口参数实体类
 * @author shuqi1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(description = "BleModDevNameDto")
public class BleModDevNameDto {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;


    @NotBlank(message = "車輛牌照")
    @ApiModelProperty(value = "車輛牌照",required = true,dataType = "String", example = "粤B3903333")
    @Length(min = 5,max = 20)
    private String deviceName;
}
