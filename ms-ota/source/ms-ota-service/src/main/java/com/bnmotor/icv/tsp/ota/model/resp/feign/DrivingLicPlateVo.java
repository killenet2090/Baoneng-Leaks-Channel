package com.bnmotor.icv.tsp.ota.model.resp.feign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DrivingLicPlateVo
 * @Description: 车牌号码查询
 * @author: zhangwei2
 * @date: 2020/11/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class DrivingLicPlateVo {
    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;
}
