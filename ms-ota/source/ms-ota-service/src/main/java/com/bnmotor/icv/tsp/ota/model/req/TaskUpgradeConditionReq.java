/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.tsp.ota.model.validate.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * <pre>
 *  终端升级条件项目定义接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "任务升级条件")
public class TaskUpgradeConditionReq extends AbstractBase {
	/**
	 * <pre>
	 *  ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "taskId", example = "1000", dataType = "Long")
	@NotNull(message = "{TaskUpgradeConditionReq.taskId.notNull.message}", groups = { Update.class })
    private Long taskId;

	@ApiModelProperty(value = "otaPlanId", example = "1000", dataType = "Long")
	private Long otaPlanId;

	/**
	 * <pre>
	 *  ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ID", example = "1000", dataType = "Long")
	@NotNull(message = "{TaskUpgradeConditionReq.conditionId.notNull.message}", groups = { Update.class })
	private Long conditionId;

	@ApiModelProperty(value = "projectId", example = "bngrp", dataType = "String")
	private String projectId;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
