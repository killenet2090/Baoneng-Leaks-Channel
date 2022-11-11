package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TaskFailStatisticsVo implements Serializable {

    private static final long serialVersionUID = -1925946345162355968L;

    @ApiModelProperty(value = "任务名字")
    private String taskName;

    @ApiModelProperty(value = "车型")
    private String modelName;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "ECU名字")
    private String ecuName;

    @ApiModelProperty(value = "任务详情")
    private String taskDesc;

    @ApiModelProperty(value = "失败错误码")
    private String failedCode;

    @ApiModelProperty(value = "日志报文链接")
    private String filePath;
}
