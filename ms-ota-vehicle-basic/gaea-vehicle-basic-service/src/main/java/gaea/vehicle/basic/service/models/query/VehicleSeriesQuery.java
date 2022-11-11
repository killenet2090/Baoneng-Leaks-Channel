/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.query;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import gaea.vehicle.basic.service.models.page.Page;

/**
 * <pre>
 *  车辆系列表 查询条件,专门提供查询使用，请勿当成提交数据保存使用，
 *	如果有必要新增额外领域模型数据，可以单独创建，请勿使用查询对象做提交数据保存
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆系列表")
public class VehicleSeriesQuery extends Page {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
