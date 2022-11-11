package com.bnmotor.icv.tsp.ota.model.req.v2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaPlanObjListV2Req
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/24 9:56
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaPlanObjListV2Req {
    @ApiModelProperty(value = "车辆数据库对象Id", example = "100076", dataType = "Long")
    private Long objectId;

    @ApiModelProperty(value = "vin", example = "vin", dataType = "String")
    private String vin;

    @ApiModelProperty(value = "当前区域", example = "当前区域", dataType = "String")
    private String currentArea;
}
