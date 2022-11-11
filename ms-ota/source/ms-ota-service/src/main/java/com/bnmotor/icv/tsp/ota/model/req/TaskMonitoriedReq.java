package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskMonitoriedReq extends Page {


    private static final long serialVersionUID = -4149395081281476465L;

    @ApiModelProperty(value = "任务ID", example = "-2", dataType = "String")
    private String taskId;

    @ApiModelProperty(value = "车架号", example = "", dataType = "String")
    private String vin;

    @ApiModelProperty(value = "任务状态 0表示查所有", example = "-2", dataType = "Integer")
    private Integer taskStatus;

    @ApiModelProperty(value = "升级状态 0表示查所有", example = "-2", dataType = "Integer")
    private Integer executedStatus;

}
