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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
@ApiModel(description = "升级策略")
public class UpgradeStrategyReq extends BasePo {
	/**
	 * <pre>
	 *  ;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "{UpgradeStrategy.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  升级计划id;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级计划id", example = "1000", dataType = "Long")
    private Long otaPlanId;
	/**
	 * <pre>
	 *  ;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1", dataType = "Integer")
    private Integer version;

	/**
	 * <pre>
	 *  关联的任务ID;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "关联的任务ID", example = "1000", dataType = "Long")
    private Long taskId;
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
	@ApiModelProperty(value = "回滚模式； 1 - 回滚， 0 - 不回滚", example = "1", dataType = "Integer")
    private Integer rollbackMode;
	/**
	 * <pre>
	 *  最大验证失败次数;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大验证失败次数", example = "1000", dataType = "Long")
    private Long firmwareVersionId;
	/**
	 * <pre>
	 *  升级前置条件ID;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级前置条件ID", example = "1000", dataType = "Long")
    private Long upgradeConditionId;
	/**
	 * <pre>
	 *  最大并发数;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大并发数", example = "1", dataType = "Integer")
    private Integer maxConcurrent;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
