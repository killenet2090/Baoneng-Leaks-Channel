package com.bnmotor.icv.tsp.ota.model.req.web;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaComponentListDto
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/1/8 16:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaComponentListDto {
    @ApiModelProperty(value = "数据ID")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    /**
     * 零件Id
     */
    @ApiModelProperty(value = "零件数据ID")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long deviceComponentId;

    @ApiModelProperty(value = "零件代码code")
    private String componentCode;

    @ApiModelProperty(value = "零件型号")
    private String componentModel;

    @ApiModelProperty(value = "零件名称")
    private String componentName;
}
