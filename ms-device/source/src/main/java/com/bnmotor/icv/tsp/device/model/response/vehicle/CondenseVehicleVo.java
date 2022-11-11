package com.bnmotor.icv.tsp.device.model.response.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: CondenseVehicleVo
 * @Description: 精简的车辆数据，用于批量查询车辆信息，返回id和车架号
 * @author: zhangwei2
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CondenseVehicleVo {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "车架号")
    private String vin;
}
