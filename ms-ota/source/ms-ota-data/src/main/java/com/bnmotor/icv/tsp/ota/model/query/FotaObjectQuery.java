/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.query;

import com.bnmotor.icv.tsp.ota.model.req.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * FotaObjectQuery
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Data
@ApiModel(description = "OTA升级计划对象清单 定义一次升级中包含哪些需要升级的车辆")
public class FotaObjectQuery extends Page {

    @ApiModelProperty(value = "treeNodeId", example = "1", dataType = "long")
    private Long treeNodeId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
