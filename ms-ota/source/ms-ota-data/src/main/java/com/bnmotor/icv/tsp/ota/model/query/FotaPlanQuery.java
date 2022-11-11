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
 * <pre>
 *  OTA升级计划表 查询条件,专门提供查询使用，请勿当成提交数据保存使用，
 *	如果有必要新增额外领域模型数据，可以单独创建，请勿使用查询对象做提交数据保存
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Data
@ApiModel(description = "OTA升级计划表")
public class FotaPlanQuery extends Page {
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "name", example = "刹车系统升级任务", dataType = "String")
    private String name;

    /**
     * 任务状态
     */
    @ApiModelProperty(value = "taskStatus", example = "1", dataType = "int")
    private Integer taskStatus;

    /**
     * 数据状态
     */
    @ApiModelProperty(value = "isEnable", example = "1", dataType = "int")
    private Integer isEnable;
    
    /**
     * 任务版本标识
     */
    @ApiModelProperty(value = "rebuildFlag", example = "1", dataType = "int")
    private Integer rebuildFlag;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
