package com.bnmotor.icv.tsp.ota.model.resp.v2;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaPlanObjListV2Vo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/24 9:56
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Deprecated
@Data
public class FotaPlanObjListV2Vo {
    
	@ApiModelProperty(value = "车辆数据库对象Id", example = "100076", dataType = "Long")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long otaObjectId;

    @ApiModelProperty(value = "vin", example = "vin", dataType = "String")
    private String vin;

    @ApiModelProperty(value = "当前区域", example = "当前区域", dataType = "String")
    private String currentArea;
}
