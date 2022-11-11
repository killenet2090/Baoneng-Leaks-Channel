package com.bnmotor.icv.tsp.device.model.request.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VehModelListQueryDto {
    @ApiModelProperty("车系名称")
    private String vehSeriesName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("车型ID")
    private Long orgId;

    @ApiModelProperty("车型名")
    private String vehModelName;

    @ApiModelProperty("年款")
    private String yearStyle;

    @ApiModelProperty("配置名称")
    private String configName;
}
