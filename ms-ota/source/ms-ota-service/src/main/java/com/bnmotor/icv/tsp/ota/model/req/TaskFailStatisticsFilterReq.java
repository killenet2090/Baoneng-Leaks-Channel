package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="筛选精选对象")
public class TaskFailStatisticsFilterReq {

    @ApiModelProperty(value = "品牌Id", example = "1000", dataType = "Long")
    private Long brandId;

    @ApiModelProperty(value = "品牌", example = "1000", dataType = "String")
    private String brandName;

    @ApiModelProperty(value = "车系Id", example = "1000", dataType = "Long")
    private Long seriesId;

    @ApiModelProperty(value = "车系", example = "1000", dataType = "String")
    private String seriesName;

    @ApiModelProperty(value = "车型Id", example = "1000", dataType = "Long")
    private Long modelId;

    @ApiModelProperty(value = "车型", example = "1000", dataType = "String")
    private String modelName;

    @ApiModelProperty(value = "年款Id", example = "1000", dataType = "Long")
    private Long yearStyleId;

    @ApiModelProperty(value = "年款", example = "1000", dataType = "String")
    private String yearStyleName;

    @ApiModelProperty(value = "FilterECU", example = "1000", dataType = "String")
    private String filterECU;

    @ApiModelProperty(value = "FilterECUId", example = "1000", dataType = "Long")
    private Long filterECUId;

    @ApiModelProperty(value = "ecuId", example = "1000", dataType = "Long")
    private Long ecuId;

    @ApiModelProperty(value = "taskName", example = "任务0001", dataType = "String")
    private String taskName;

    @ApiModelProperty(value = "车架号", example = "任务0001", dataType = "String")
    private String vin;
}
