/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 *  升级策略接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "升级固件策略")
public class UpgradeFirmwareReq extends AbstractBase {

	@ApiModelProperty(value = "项目Id", example = "1000", dataType = "Long")
	private String projectId;

	/**
	 * <pre>
	 *  升级计划ID;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级任务ID", example = "1000", dataType = "Long")
	private Long taskId;

	@ApiModelProperty(value = "升级任务(plan)ID", example = "1000", dataType = "Long")
	private Long planId;

	/**
	 * <pre>
	 *  关联固件ID;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "关联固件ID", example = "1000", dataType = "Long")
    private Long firmwareId;
	/**
	 * <pre>
	 *  回滚模式； 1 - 回滚， 0 - 不回滚;字段长度:4,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "回滚模式； 1 - 回滚， 0 - 不回滚", example = "1 - 回滚， 0 - 不回滚", dataType = "Integer")
    private Integer rollbackMode;

	@ApiModelProperty(value = "升级顺序 1,2,... 从小到大依次升级", example = "1", dataType = "Integer")
	private Integer upgradeSeq;
	/**
	 * <pre>
	 *  最大验证失败次数;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "固件版本ID", example = "1000", dataType = "Long")
    private Long firmwareVersionId;
	/**
	 * <pre>
	 *  最大并发数;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大并发数", example = "1", dataType = "Integer")
    private Integer maxConcurrent;


	@ApiModelProperty(value = "数据版本", example = "0", dataType = "Integer")
	private Integer version;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
