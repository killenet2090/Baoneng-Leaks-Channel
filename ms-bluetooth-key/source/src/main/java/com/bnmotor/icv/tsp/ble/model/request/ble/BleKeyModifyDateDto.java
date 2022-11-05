package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleKeyModifyDateDto
 * @Description: 修改蓝牙钥匙有效期入口参数实体类
 * @author shuqi1
 * @since 2020-08-13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class BleKeyModifyDateDto {
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    public String deviceId;
    /**
     * 蓝牙钥匙ID
     */
    @NotBlank(message = "蓝牙钥匙ID不能为空")
    @ApiModelProperty(value = "蓝牙钥匙ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    public String bleKeyId;
    /**
     * 蓝牙钥匙开始时间
     */
    @ApiModelProperty(value = "生效时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    @Length(min = 19,max = 19)
    private String bleEffectiveTime;
    /**
     * 蓝牙钥匙结束时间
     */
    @ApiModelProperty(value = "失效时间",required = true, dataType = "String", example = "2286-11-21 01:46:39")
    @Length(min = 19,max = 19)
    public String bleKeyExpireTime;


}
