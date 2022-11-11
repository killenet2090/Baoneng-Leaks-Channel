package com.bnmotor.icv.tsp.ota.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(description="数据统计升级时间段查询对象")
public class UpgradeTimeStatisticsReq {

    @ApiModelProperty(value = "任务ID", example = "1000", dataType = "Long")
    private Long taskId;

    @ApiModelProperty(value = "截至时间", example = "2020-06-08", dataType = "Date")
    private String endTime;
}
