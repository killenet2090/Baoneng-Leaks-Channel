package com.bnmotor.icv.tsp.ota.model.resp.v2;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "V2版本升级任务查询响应参数", description = "V2版本升级任务查询响应参数")
@Data
public class FotaPlanV2Vo extends AbstractBase {

//	@ApiModelProperty(value = "升级包状态是否已就绪")
//	Integer IsEnableOfStart;

	@ApiModelProperty(value = "任务名称", example = "任务名称：这是一个测试任务的名称", dataType = "String")
	@Length(max = 255, message = "{UpgradeTaskReq.name.maxLength.message}")
	private String planName;

	@ApiModelProperty(value = "目标版本ID：整车的目标版本")
	private String targetVersion;

	@ApiModelProperty(value = "审核状态： 1免审批，2待审批、3审批中、4已审批、5未通过", example = "0", dataType = "Integer")
	private Integer approveStatus;

	@ApiModelProperty(value = "参考approveStatus说明", example = "0", dataType = "String")
	private String approveStatusDesc;

	@ApiModelProperty(value = "发布状态： 1待发布 2发布中 3已发布 4已失效 5延迟发布", example = "0", dataType = "Integer")
	private Integer publishStatus;

	@ApiModelProperty(value = "参考publishStatus说明", example = "0", dataType = "Integer")
	private String publishStatusDesc;

	@ApiModelProperty(value = "参考planMode说明", example = "0", dataType = "String")
	private String planModeDesc;

	@ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)", example = "1", dataType = "Integer")
	private Integer upgradeMode;

	@ApiModelProperty(value = "参考upgradeMode说明", example = "1", dataType = "String")
	private String upgradeModeDesc;

	////////////////////////////////////////////////////////////////
	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long id;

	@ApiModelProperty(value = "项目ID", example = "bngrp", dataType = "String")
	private String projectId;

	@ApiModelProperty(value = "是否启用 0不启用 1启用", example = "1", dataType = "Integer")
	private Integer isEnable;

	@ApiModelProperty(value = "参考isEnable说明", example = "1", dataType = "String")
	private String isEnableDesc;

	@ApiModelProperty(value = "计划开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date planStartTime;

	@ApiModelProperty(value = "计划结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date planEndTime;

	@ApiModelProperty(value = "任务模式： 0测试任务 1正式任务", example = "0", dataType = "Integer")
	private Integer planMode;

//	@ApiModelProperty(value = "升级模式： 0静默升级 1正常模式 2工厂模式", example = "1", dataType = "Integer")
//	private Integer upgradeMode;

	@ApiModelProperty(value = "整车版本号", example = "V1.00.01", dataType = "String")
	private String entireVersionNo;

	@ApiModelProperty(value = "升级策略", example = "升级策略", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long fotaStrategyId;

//	@ApiModelProperty(value = "版本说明", example = "版本说明: 这是一个小范围批量测试的升级版本", dataType = "String")
//	private String versionTips;

	@ApiModelProperty(value = "任务车辆数")
	private Integer batchSize;

//	@ApiModelProperty(value = "免责声明", example = "免责声明：这里是免责声明的内容", dataType = "String")
//	private String disclaimer;
}
