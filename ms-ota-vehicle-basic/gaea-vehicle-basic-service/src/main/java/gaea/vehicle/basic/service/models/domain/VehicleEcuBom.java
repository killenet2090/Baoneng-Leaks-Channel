/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.domain;

import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 *	<b>表名</b>：tb_vehicle_ecu_bom
 *  ECU信息表，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("ECU信息表")
public class VehicleEcuBom extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: ecu系统id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("ecu系统id")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: vin
	 * 描述: 关联车辆vin号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("关联车辆vin号")
    private String vin;
	/**
	 * <pre>
	 * 数据库字段: ecu_name
	 * 描述: ecu名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("ecu名称")
    private String ecuName;
	/**
	 * <pre>
	 * 数据库字段: ecu_id
	 * 描述: ecuId;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("ecuId")
    private String ecuId;
	/**
	 * <pre>
	 * 数据库字段: supplier
	 * 描述: 供应商名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("供应商名称")
    private String supplier;
	/**
	 * <pre>
	 * 数据库字段: serial_numbe
	 * 描述: 序列号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("序列号")
    private String serialNumbe;
	/**
	 * <pre>
	 * 数据库字段: firmware_version_number
	 * 描述: 软件版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("软件版本号")
    private String firmwareVersionNumber;
	/**
	 * <pre>
	 * 数据库字段: hardware_version_number
	 * 描述: 固件版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("固件版本号")
    private String hardwareVersionNumber;
	/**
	 * <pre>
	 * 数据库字段: boot_version_number
	 * 描述: boot版本号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("boot版本号")
    private String bootVersionNumber;
	/**
	 * <pre>
	 * 数据库字段: diagnostic_id
	 * 描述: 诊断id号;字段长度:10,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("诊断id号")
    private String diagnosticId;
	/**
	 * <pre>
	 * 数据库字段: differential_support
	 * 描述: 是否支持差分;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("是否支持差分")
    private Integer differentialSupport;
	/**
	 * <pre>
	 * 数据库字段: remark
	 * 描述: 备注信息;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("备注信息")
    private String remark;
	/**
	 * <pre>
	 * 数据库字段: create_time
	 * 描述: 创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("创建时间")
    private Date createTime;
	/**
	 * <pre>
	 * 数据库字段: update_time
	 * 描述: 最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("最后一次修改时间")
    private Date updateTime;
	/**
	 * <pre>
	 * 数据库字段: is_active
	 * 描述: 是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("是否有效，1有效；0无效")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
