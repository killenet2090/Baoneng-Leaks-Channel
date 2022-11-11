/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * <pre>
 *  该表用于定义一个OTA升级计划中需要升级哪几个软件
包含：
     1. 依赖的软件清单
                                               -接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Data
@ToString(callSuper = true)
@ApiModel(description = "任务固件升级清单")
public class UpgradeFirmwareListReq extends BasePo {

	@ApiModelProperty(value = "升级固件清单", example = "[{\"firmwareId\":1,\"firmwareVersionId\":1,\"maxConcurrent\":1,\"rollbackMode\":1,\"taskId\":1,\"upgradeSeq\":1}]", dataType = "UpgradeFirmwareReq")
	private List<UpgradeFirmwareReq> upgradeFirmwareList;

	@ApiModelProperty(value = "升级前置条件清单", example = "[{\"taskId\":1,\"conditionId\":1}]", dataType = "TaskUpgradeConditionReq")
	private List<TaskUpgradeConditionReq> upgradeConditionList;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
