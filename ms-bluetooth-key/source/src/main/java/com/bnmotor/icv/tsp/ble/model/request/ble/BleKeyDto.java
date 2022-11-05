package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author shuqi1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BleKeyDto implements Serializable {

    private static final long serialVersionUID = -5143751456933328940L;
    /**
     * 蓝牙钥匙ID
     */
    @NotBlank(message = "蓝牙钥匙ID不能为空")
    @ApiModelProperty(value = "蓝牙钥匙ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    private String bleKeyId;

    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;


}
