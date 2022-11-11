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
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@ApiModel(description = "OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级 在创建升级计划时创建升级")
public class FotaPlanTaskDetailReq extends AbstractBase {
	/**
	 * <pre>
	 *  ;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  项目ID;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "项目ID", example = "测试", dataType = "String")
	@NotEmpty(message = "")
	@Length(max = 50, message = "")
    private String projectId;
	/**
	 * <pre>
	 *  升级计划对象ID
            ;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级计划对象ID ", example = "1000", dataType = "Long")
    private Long otaPlanObjId;
	/**
	 * <pre>
	 *  升级计划级固件ID;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级计划级固件ID", example = "1000", dataType = "Long")
    private Long otaPlanFirmwareId;
	/**
	 * <pre>
	 *  整个OTA开始时间
            从版本检查更新开始;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "整个OTA开始时间 从版本检查更新开始", example = "2020-06-08 17:07:14", dataType = "Date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
	/**
	 * <pre>
	 *  结束时间;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "结束时间", example = "2020-06-08 17:07:14", dataType = "Date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
	/**
	 * <pre>
	 *  失败时间;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "失败时间", example = "2020-06-08 17:07:14", dataType = "Date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date failedTime;
	/**
	 * <pre>
	 *  升级失败原因;字段长度:256,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级失败原因", example = "测试", dataType = "String")
	@Length(max = 256, message = "{FotaPlanTaskDetail.failedReason.maxLength.message}")
    private String failedReason;
	/**
	 * <pre>
	 *  重试次数;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "重试次数", example = "1", dataType = "Integer")
    private Integer retryCount;
	/**
	 * <pre>
	 *  重试时间;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "重试时间", example = "2020-06-08 17:07:14", dataType = "Date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date retyrnTime;
	/**
	 * <pre>
	 *  任务状态
            0 - 禁用
            1 - 启用
            ;字段长度:2,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "任务状态 0 - 禁用 1 - 启用 ", example = "1", dataType = "Integer")
    private Integer taskStatus;
	/**
	 * <pre>
	 *  备注;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "备注", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanTaskDetail.remarks.maxLength.message}")
    private String remarks;
	/**
	 * <pre>
	 *  ;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1", dataType = "Integer")
    private Integer version;
	/**
	 * <pre>
	 *  删除标志
            0 - 正常
            1 - 删除;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "删除标志 0 - 正常 1 - 删除", example = "1", dataType = "Integer")
    private Integer delFlag;
	/**
	 * <pre>
	 *  创建人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建人", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanTaskDetail.createBy.maxLength.message}")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanTaskDetail.updateBy.maxLength.message}")
    private String updateBy;
	/**
	 * <pre>
	 *  新版本提示语;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "新版本提示语", example = "测试", dataType = "String")
    private String newVersionTips;
	/**
	 * <pre>
	 *  下载提示语;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "下载提示语", example = "测试", dataType = "String")
    private String downloadTips;
	/**
	 * <pre>
	 *  免责声明;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "免责声明", example = "测试", dataType = "String")
    private String disclaimer;
	/**
	 * <pre>
	 *  ;字段长度:,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "测试", dataType = "String")
    private String taskTips;
	/**
	 * <pre>
	 *  升级模式； 1 - 静默升级， 2 - 提示用户;字段长度:10,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级模式； 1 - 静默升级， 2 - 提示用户", example = "测试", dataType = "String")
	@Length(max = 10, message = "{FotaPlanTaskDetail.upgradeMode.maxLength.message}")
    private String upgradeMode;
	/**
	 * <pre>
	 *  任务名称;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "任务名称", example = "测试", dataType = "String")
	@Length(max = 255, message = "{FotaPlanTaskDetail.name.maxLength.message}")
    private String name;
	/**
	 * <pre>
	 *  固件清单ID
;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "固件清单ID ", example = "1000", dataType = "Long")
    private Long firmwareListId;
	/**
	 * <pre>
	 *  升级对象清单ID;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级对象清单ID", example = "1000", dataType = "Long")
    private Long objectListId;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
