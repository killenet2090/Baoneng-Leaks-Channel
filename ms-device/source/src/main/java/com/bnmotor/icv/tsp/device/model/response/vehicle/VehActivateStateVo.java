package com.bnmotor.icv.tsp.device.model.response.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: VehicleQRCodeScanVo
 * @Description: 车辆信激活二维码扫码返回实体
 * @author: huangyun1
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@EqualsAndHashCode
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehActivateStateVo {

    @ApiModelProperty(value = "车机状态 0-未激活，1-成功，2-失败，3-进行中")
    private Integer state;

}
