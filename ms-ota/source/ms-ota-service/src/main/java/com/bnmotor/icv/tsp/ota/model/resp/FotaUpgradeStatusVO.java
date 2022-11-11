package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: FotaUpgradeStatusVO
 * @Description: 车辆升级状态
 * @author: xuxiaochang1
 * @date: 2020/8/24 9:37
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class FotaUpgradeStatusVO {
    @ApiModelProperty(value = "状态值", example = "1", required = true)
    private Integer status;

    @ApiModelProperty(value = "状态说明", example = "TBOX端获取到版本检查", required = true)
    private String desc;
}
