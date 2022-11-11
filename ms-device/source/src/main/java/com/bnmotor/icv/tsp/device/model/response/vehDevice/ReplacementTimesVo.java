package com.bnmotor.icv.tsp.device.model.response.vehDevice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReplacementTimesVo
 * @Description: 换件次数对象
 * @author: zhangwei2
 * @date: 2020/12/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ReplacementTimesVo {
    @ApiModelProperty("设备类型")
    private Integer deviceType;

    @ApiModelProperty("换件次数")
    private Integer replacementTimes;
}
