/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * <pre>
 *  OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "OTA升级计划对象 定义一次升级中包含哪些需要升级的车辆")
public class UpgradeTaskObjectReq extends BasePo {

	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "任务Id不能为空", groups = { Update.class })
    private Long taskId;

	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	private Long otaPlanId;

	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "升级车辆对象Id不能为空", groups = { Update.class })
	private Long objectId;

	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "升级车辆对象Id不能为空")
	private Long otaObjectId;

	@ApiModelProperty(value = "当前区域", example = "深圳", dataType = "String")
	private String currentArea;

	@ApiModelProperty(value = "销售区域", example = "广州", dataType = "String")
	private String saleArea;

	@ApiModelProperty(value = "vin码", example = "123v213qwe", dataType = "String")
	private String vin;

	@ApiModelProperty(value = "数据版本", example = "0", dataType = "Integer")
	private Integer version;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
