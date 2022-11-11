package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
public class TaskTotalStatisticsVo implements Serializable {

    private static final long serialVersionUID = 1499846685779312416L;

    @ApiModelProperty(value = "升级失败数目")
    private int upgradeFail;

    @ApiModelProperty(value = "取消数目")
    private int cancel;

    @ApiModelProperty(value = "未升级数目")
    private int notUpgrade;

    @ApiModelProperty(value = "升级中数目")
    private int upgrading;

    @ApiModelProperty(value = "升级成功数目")
    private int upgradeSuccess;

    @ApiModelProperty(value = "日期")
    private String date;

}
