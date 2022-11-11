package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlanFirmwareDetailVo implements Serializable {

    private static final long serialVersionUID = 3001419117845424915L;

    @ApiModelProperty(value = "ID")
    private String Id;

    @ApiModelProperty(value = "排序")
    private int rank;

    @ApiModelProperty(value = "零件号")
    private String componentName;

    @ApiModelProperty(value = "零件号")
    private String componentCode;

    @ApiModelProperty(value = "固件名字")
    private String firmwareName;

    @ApiModelProperty(value = "固件码")
    private String firmwareCode;

    @ApiModelProperty(value = "车辆原版本号")
    private String sourceVersion;

    @ApiModelProperty(value = "任务目标版本号")
    private String targetVersion;

    @ApiModelProperty(value = "当前车辆版本号")
    private String currentVersion;

    @ApiModelProperty(value = "回滚配置")
    private int rollbackMode;

    @ApiModelProperty(value = "升级组")
    private int group;

    @ApiModelProperty(value = "升级结果")
    private String status;

    @ApiModelProperty(value = "升级状态")
    private int statusCode;
}
