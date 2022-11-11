/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.resp;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;


@Getter
@Setter
@Api("该表用于定义一个OTA升级计划中需要升级哪几个软件 包含：1. 依赖的软件清单 -")
public class FotaPlanFirmwareListVo extends BasePo {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("ID")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: project_id
	 * 描述: 项目ID;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("项目ID")
    private String projectId;
	/**
	 * <pre>
	 * 数据库字段: plan_id
	 * 描述: 升级计划ID;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("升级计划ID")
    private Long planId;

	@ApiModelProperty("升级任务ID")
	private Long taskId;
	/**
	 * <pre>
	 * 数据库字段: firmware_id
	 * 描述: ;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("")
    private String firmwareId;
	/**
	 * <pre>
	 * 数据库字段: firmware_version_id
	 * 描述: 目标版本ID
            即期望升级到的目标版本，
            如果不指定，则要求升级到最新版本;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("目标版本ID 即期望升级到的目标版本， 如果不指定，则要求升级到最新版本")
    private String firmwareVersionId;
	/**
	 * <pre>
	 * 数据库字段: upgrade_seq
	 * 描述: 升级顺序
            1,2,...
            从小到大依次升级;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("升级顺序 从小到大依次升级")
    private Integer upgradeSeq;
	/**
	 * <pre>
	 * 数据库字段: version
	 * 描述: ;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("")
    private Integer version;


	@ApiModelProperty("固件名称")
	private String firmwareName;

	@ApiModelProperty("固件代码")
	private String firmwareCode;

	@ApiModelProperty("固件版本")
	private String firmwareVersion;

	@ApiModelProperty("零件名称")
	private String componentName;

	@ApiModelProperty("零件号")
	private String componentCode;

	@ApiModelProperty("ecuID")
	private String ecuId;

	@ApiModelProperty("最大并发数")
	private Integer maxConcurrent;

	@ApiModelProperty("回滚模式")
	private Integer rollbackMode;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
