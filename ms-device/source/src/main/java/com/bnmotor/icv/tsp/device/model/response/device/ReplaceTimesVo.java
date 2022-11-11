package com.bnmotor.icv.tsp.device.model.response.device;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReplaceTimesVo
 * @Description: 换件次数响应对象
 * @author: zhangwei2
 * @date: 2020/8/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ReplaceTimesVo {
    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("换件次数")
    private Integer num;
}
