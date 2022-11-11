/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.query;

import com.bnmotor.icv.tsp.ota.model.req.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaStrategyQuery
 * @Description: 策略查询参数
 * @author: xuxiaochang1
 * @date: 2020/6/5 13:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(description = "升级策略查询参数")
public class FotaStrategyQuery extends Page {
    /**
     * 设备树节点Id
     */
    @ApiModelProperty(value = "设备树节点Id", example = "1000", dataType = "String")
    private String treeNodeId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称", example = "1000", dataType = "String")
    private String brand;

    /**
     * 系列名称
     */
    @ApiModelProperty(value = "系列名称", example = "1000", dataType = "String")
    private String series;

    /**
     * 车型名称
     */
    @ApiModelProperty(value = "车型名称", example = "1000", dataType = "String")
    private String model;

    /**
     * 年款名称
     */
    @ApiModelProperty(value = "年款名称", example = "1000", dataType = "String")
    private String year;

    /**
     * 配置名称
     */
    @ApiModelProperty(value = "配置名称", example = "1000", dataType = "String")
    private String conf;

    @ApiModelProperty(value = "状态", example = "1000", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "任务模式:1=正式任务，0=测试任务", example = "1", dataType = "Integer")
    private Integer planMode;

    @ApiModelProperty(value = "是否开启:1=开启，0=不开启,其他不区分", example = "1", dataType = "Integer")
    private Integer isEnable;
}
