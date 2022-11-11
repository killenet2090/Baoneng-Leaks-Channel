package com.bnmotor.icv.tsp.vehstatus.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SetDoorDto
 * @Description: 设置车门请求对象
 * @author: huangyun1
 * @date: 2020/6/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "设置车门请求对象", description = "设置车门请求对象")
public class SetVehStatusDto implements Serializable {
    /**
     * 车辆识别号
     */
    @ApiModelProperty(value = "车辆识别号", name = "vin", required = true, example = "T09988888888")
    private String vin;

    @ApiModelProperty(value = "需更新字段名", name = "columnName", required = true)
    private List<String> columnName;

    @ApiModelProperty(value = "需更新字段值", name = "columnValue", required = true)
    private List<Number> columnValue;
}
