package com.bnmotor.icv.tsp.operation.veh.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: QueryVehModelDto
 * @Description: 根据各种条件查询车型信息
 * @author: zhangwei2
 * @date: 2020/7/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "queryVehModelDto", description = "查询车型信息")
public class QueryVehProjectDto {
    @ApiModelProperty(value = "搜索关键词", name = "keyword")
    private String keyword;

    @ApiModelProperty(value = "车辆类型,1-纯电动，2-燃油", name = "vehType", example = "1")
    private Integer vehType;

    @JsonIgnore
    @ApiModelProperty(value = "当前页数", name = "current", example = "1")
    private Integer current = 1;

    @JsonIgnore
    @ApiModelProperty(value = "页面大小", name = "pageSize", example = "10000")
    private Integer pageSize = 10000;
}
