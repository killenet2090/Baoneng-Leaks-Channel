package com.bnmotor.icv.tsp.ble.model.request.ble;

import com.bnmotor.icv.tsp.ble.model.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: BleKeyQueryDto
 * @Description: 蓝牙钥匙查询参数实体类
 * @author: liuyiwei
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(description = "BleKeyQueryDto")
public class BleKeyQueryDto extends Model {
    private static final long serialVersionUID = -6276487527182459141L;
    /**
     * 车辆ID
     */
    @NotBlank(message = "车辆ID不能为空")
    @ApiModelProperty(value = "车辆ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 19)
    private String deviceId;

}
