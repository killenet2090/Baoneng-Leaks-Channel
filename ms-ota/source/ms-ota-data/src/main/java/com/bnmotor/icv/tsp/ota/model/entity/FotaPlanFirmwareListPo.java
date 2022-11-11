/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @ClassName: FotaPlanFirmwareListPo.java 
 * @Description: 废弃tb_fota_plan_firmware_list表，改用tb_fota_strategy_firmware_list表
 * 					V1版本任务使用tb_fota_plan_firmware_list
 * 					V2版本任务使用tb_fota_strategy_firmware_list
 * @author E.YanLonG
 * @since 2021-1-12 17:52:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Deprecated
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_plan_firmware_list")
@ApiModel(value="FotaObjectDo对象", description="用于定义一个OTA升级计划中需要升级哪几个软件 包含：1. 依赖的软件清单 -")
public class FotaPlanFirmwareListPo extends BasePo {

	@ApiModelProperty(value = "ID")
	@TableId(value = "id")
	private Long id;

	@ApiModelProperty(value = "项目Id")
	private String projectId;

	/*@ApiModelProperty(value = "任务Id")
	private Long taskId;*/

	@ApiModelProperty(value = "任务计划Id，意义同taskId",notes = "升级计划ID")
	private Long planId;

	@ApiModelProperty(value = "固件ID")
	private Long firmwareId;

	@ApiModelProperty(value = "目标版本ID")
	private Long firmwareVersionId;

	@ApiModelProperty(value = "升级顺序")
	private Integer upgradeSeq;

	@ApiModelProperty(value = "回滚模式",notes = "回滚标志 0=不回滚 1=回滚")
	private Integer rollbackMode;

	@ApiModelProperty(value = "版本")
	private Integer version;
	
	@ApiModelProperty(value = "升级组(分组升级顺序)")
	private Integer groupSeq;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
