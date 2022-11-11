/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import gaea.vehicle.basic.service.models.domain.AbstractReq;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import gaea.vehicle.basic.service.models.validate.Update;

/**
 * <pre>
 *  ECU信息表接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "ECU信息表")
public class VehicleEcuBomReq extends AbstractReq {
	/**
	 * <pre>
	 *  ecu系统id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ecu系统id", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleEcuBom.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  关联车辆vin号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "关联车辆vin号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.vin.notEmpty.message}")
	@Length(max = 50, message = "{VehicleEcuBom.vin.maxLength.message}")
    private String vin;
	/**
	 * <pre>
	 *  ecu名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ecu名称", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.ecuName.notEmpty.message}")
	@Length(max = 50, message = "{VehicleEcuBom.ecuName.maxLength.message}")
    private String ecuName;
	/**
	 * <pre>
	 *  ecuId;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ecuId", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.ecuId.notEmpty.message}")
	@Length(max = 50, message = "{VehicleEcuBom.ecuId.maxLength.message}")
    private String ecuId;
	/**
	 * <pre>
	 *  供应商名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "供应商名称", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.supplier.notEmpty.message}")
	@Length(max = 50, message = "{VehicleEcuBom.supplier.maxLength.message}")
    private String supplier;
	/**
	 * <pre>
	 *  序列号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "序列号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.serialNumbe.notEmpty.message}")
	@Length(max = 50, message = "{VehicleEcuBom.serialNumbe.maxLength.message}")
    private String serialNumbe;
	/**
	 * <pre>
	 *  软件版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "软件版本号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.firmwareVersionNumber.notEmpty.message}")
	@Length(max = 10, message = "{VehicleEcuBom.firmwareVersionNumber.maxLength.message}")
    private String firmwareVersionNumber;
	/**
	 * <pre>
	 *  固件版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "固件版本号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.hardwareVersionNumber.notEmpty.message}")
	@Length(max = 10, message = "{VehicleEcuBom.hardwareVersionNumber.maxLength.message}")
    private String hardwareVersionNumber;
	/**
	 * <pre>
	 *  boot版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "boot版本号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.bootVersionNumber.notEmpty.message}")
	@Length(max = 10, message = "{VehicleEcuBom.bootVersionNumber.maxLength.message}")
    private String bootVersionNumber;
	/**
	 * <pre>
	 *  诊断id号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "诊断id号", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleEcuBom.diagnosticId.notEmpty.message}")
	@Length(max = 10, message = "{VehicleEcuBom.diagnosticId.maxLength.message}")
    private String diagnosticId;
	/**
	 * <pre>
	 *  是否支持差分;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否支持差分", example = "1", dataType = "Integer")
	@NotNull(message = "{VehicleEcuBom.differentialSupport.notNull.message}")
    private Integer differentialSupport;
	/**
	 * <pre>
	 *  备注信息;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "备注信息", example = "测试", dataType = "String")
	@Length(max = 255, message = "{VehicleEcuBom.remark.maxLength.message}")
    private String remark;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{VehicleEcuBom.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
