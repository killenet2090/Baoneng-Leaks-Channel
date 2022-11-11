package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="项目和treelevel的设备树节点查询对象")
public class DeviceTreeNodePartReq {
    @ApiModelProperty(value = "项目Id", example = "guanzhi001", dataType = "String", required = true)
    private String projectId;

    @ApiModelProperty(value = "层级", example = "0", dataType = "int", required = true )
    private int treeLevel;
}
