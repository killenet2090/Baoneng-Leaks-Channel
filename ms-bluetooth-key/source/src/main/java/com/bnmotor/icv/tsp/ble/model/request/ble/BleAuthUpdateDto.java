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
public class BleAuthUpdateDto implements Serializable {

    private static final long serialVersionUID = -5143751456933328940L;
    /**
     * 移动设备ID
     */
    @ApiModelProperty(value = "移动设备ID", required = true, example = "15914176367")
    @NotBlank(message = "移动设备ID不能为空")
    @Length(min = 5,max = 50)
    private String mobileDeviceId;

    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String deviceId;


}
