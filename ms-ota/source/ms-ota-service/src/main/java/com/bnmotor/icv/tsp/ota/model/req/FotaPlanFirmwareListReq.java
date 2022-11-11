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
import lombok.ToString;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel(description = "该表用于定义一个OTA升级计划中需要升级哪几个软件 ")
public class FotaPlanFirmwareListReq extends AbstractBase {
	/**
	 * <pre>
	 *  ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ID", example = "1000", dataType = "Long")
	@NotNull(message = "{FotaPlanFirmwareList.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  项目ID;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "项目ID", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanFirmwareList.projectId.maxLength.message}")
    private String projectId;
	/**
	 * <pre>
	 *  升级计划ID;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级计划ID", example = "1000", dataType = "Long")
    private Long planId;
	/**
	 * <pre>
	 *  ;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
    private Long firmwareId;
	/**
	 * <pre>
	 *  目标版本ID
            即期望升级到的目标版本，
            如果不指定，则要求升级到最新版本;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "目标版本ID 即期望升级到的目标版本， 如果不指定，则要求升级到最新版本", example = "1000", dataType = "Long")
    private Long firmwareVersionId;
	/**
	 * <pre>
	 *  升级顺序
            1,2,...
            从小到大依次升级;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级顺序 1,2,... 从小到大依次升级", example = "1", dataType = "Integer")
    private Integer upgradeSeq;
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
	@Length(max = 50, message = "{FotaPlanFirmwareList.createBy.maxLength.message}")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanFirmwareList.updateBy.maxLength.message}")
    private String updateBy;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
