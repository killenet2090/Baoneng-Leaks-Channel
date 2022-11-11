package com.bnmotor.icv.tsp.vehstatus.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: SetVehStatusByMapDto
 * @Description: 根据map设置车辆状态
 * @author: huangyun1
 * @date: 2020/6/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "根据map设置车辆状态请求对象", description = "根据map设置车辆状态请求对象")
public class SetVehStatusByMapDto implements Serializable {
    /**
     * 车辆识别号
     */
    @ApiModelProperty(value = "车辆识别号", name = "vin", required = true, example = "T09988888888")
    private String vin;

    @ApiModelProperty(value = "更新状态map", name = "updateVehStatusMap", required = true)
    private Map<String, Number> updateVehStatusMap;
}
