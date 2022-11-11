package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="数据统计任务进度查询对象")
public class TaskProcessStatisticsReq {
    @ApiModelProperty(value = "任务ID", example = "1000", dataType = "Long")
    private Long taskId;

    @ApiModelProperty(value = "统计方式  1日报 2周报 3月报", example = "1", dataType = "Integer")
    private Integer statisticalMethod;

    @ApiModelProperty(value = "开始时间", example = "2020-06-08", dataType = "String")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2020-08-08", dataType = "String")
    private String endTime;

}

