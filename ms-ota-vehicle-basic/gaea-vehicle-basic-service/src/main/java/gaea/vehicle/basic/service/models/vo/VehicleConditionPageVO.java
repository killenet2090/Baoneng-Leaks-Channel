/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import gaea.vehicle.basic.service.models.page.Page;

/**
 * <pre>
 *  车辆基础信息表 查询条件,专门提供查询使用，请勿当成提交数据保存使用，
 *	如果有必要新增额外领域模型数据，可以单独创建，请勿使用查询对象做提交数据保存
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆条件查询模型")
public class VehicleConditionPageVO extends Page {

    @ApiModelProperty("车辆vin号，17位唯一编号")
    private String vin;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("车系名称")
    private String seriesName;

    @ApiModelProperty("车型名称")
    private String modelName;

    @ApiModelProperty("车型配置年份")
    private String modelYear;

    @ApiModelProperty("车型配置名称")
    private String subModelName;

    @ApiModelProperty("当前区域")
    private String currentArea;

    @ApiModelProperty("销售区域")
    private String saleArea;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
