package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskMonitoriedECUReq{

    private static final long serialVersionUID = 2439848748930289268L;
    @ApiModelProperty(value = "ID", example = "-2", dataType = "String")
    private String Id;
}
