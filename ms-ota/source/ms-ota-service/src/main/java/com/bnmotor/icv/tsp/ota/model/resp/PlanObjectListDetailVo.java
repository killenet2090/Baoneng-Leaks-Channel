package com.bnmotor.icv.tsp.ota.model.resp;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlanObjectListDetailVo implements Serializable {

    private static final long serialVersionUID = -4761068317054089055L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    @ApiModelProperty(value = "任务名字")
    private String taskName;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "车辆原版本号")
    private String sourceVersion;

    @ApiModelProperty(value = "任务目标版本号")
    private String targetVersion;

    @ApiModelProperty(value = "当前车辆版本号")
    private String currentVersion;

    @ApiModelProperty(value = "任务状态")
    private int taskStatusCode;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "车辆当前状态码")
    private int executedStatusCode;

    @ApiModelProperty(value = "车辆当前状态")
    private String executedStatus;

    @ApiModelProperty(value = "升级结果")
    private String result;

    @ApiModelProperty(value = "升级结果码")
    private int resultCode;

    @ApiModelProperty(value = "任务ID")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long taskId;
}
