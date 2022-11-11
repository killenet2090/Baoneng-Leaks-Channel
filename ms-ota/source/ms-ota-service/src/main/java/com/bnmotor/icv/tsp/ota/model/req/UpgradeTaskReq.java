/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * <pre>
 *  OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
在创建升级计划时创建升级接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "升级任务基本信息")
public class UpgradeTaskReq extends BasePo {

	@ApiModelProperty(value = "任务ID", example = "1000", dataType = "Long")
	private Long id;

	@ApiModelProperty(value = "任务关联车款ID", example = "1000", dataType = "Long")
	private String objectParentId;

	@ApiModelProperty(value = "是否启用", example = "1", dataType = "Long")
	private Integer isEnable;
	/**
	 * <pre>
	 *  整个OTA开始时间
            从版本检查更新开始;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "整个OTA开始时间 从版本检查更新开始", example = "2020-06-08 17:07:14", dataType = "Date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
	/**
	 * <pre>
	 *  结束时间;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "结束时间", example = "2020-06-08 17:07:14", dataType = "Date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

	/**
	 * <pre>
	 *  新版本提示语;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "新版本提示语", example = "新版本提示语测试", dataType = "String")
    private String newVersionTips;
	/**
	 * <pre>
	 *  下载提示语;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "下载提示语", example = "下载提示语测试", dataType = "String")
    private String downloadTips;
	/**
	 * <pre>
	 *  免责声明;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "免责声明", example = "免责声明测试", dataType = "String")
    private String disclaimer;
	/**
	 * <pre>
	 *  ;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "任务说明", example = "任务说明测试", dataType = "String")
    private String taskTips;
	/**
	 * <pre>
	 *  升级模式； 1 - 静默升级， 2 - 提示用户;字段长度:10,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级模式； 1 - 静默升级， 2 - 提示用户", example = "1", dataType = "String")
	@Length(max = 10, message = "{UpgradeTaskReq.upgradeMode.maxLength.message}")
    private String upgradeMode;
	/**
	 * <pre>
	 *  任务名称;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "任务名称", example = "测试", dataType = "String")
	@Length(max = 255, message = "{UpgradeTaskReq.name.maxLength.message}")
    private String name;

	@ApiModelProperty(value = "最大下载失败次数", example = "3", dataType = "int")
	private Integer maxDownloadFailed;

	@ApiModelProperty(value = "最大验证失败次数", example = "3", dataType = "int")
	private Integer maxVerifyFailed;

	@ApiModelProperty(value = "最大安装次数", example = "3", dataType = "int")
	private Integer maxInstallFailed;
}
