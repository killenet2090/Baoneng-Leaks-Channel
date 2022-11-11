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
 *	<b>表名</b>：tb_vehicle
 *  车辆基础信息表，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("车辆基础信息表")
public class Vehicle extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: 车辆id，自增长列;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("车辆id，自增长列")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: vehicle_model_id
	 * 描述: 车系id，关联车系表tb_vehicle_model表;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("车系id，关联车系表tb_vehicle_model表")
    private Long vehicleSubModelId;
	/**
	 * <pre>
	 * 数据库字段: vin
	 * 描述: 车辆vin号，17位唯一编号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("车辆vin号，17位唯一编号")
    private String vin;
	/**
	 * <pre>
	 * 数据库字段: plate_color
	 * 描述: 车牌颜色，关联字典表;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("车牌颜色，关联字典表")
    private Long plateColor;
	/**
	 * <pre>
	 * 数据库字段: plate_no
	 * 描述: 车牌号码;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("车牌号码")
    private String plateNo;
	/**
	 * <pre>
	 * 数据库字段: material_no
	 * 描述: 物料号;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("物料号")
    private String materialNo;
	/**
	 * <pre>
	 * 数据库字段: prod_area
	 * 描述: 生产区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("生产区域")
    private String prodArea;
	/**
	 * <pre>
	 * 数据库字段: plate_area
	 * 描述: 上牌区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("上牌区域")
    private String plateArea;
	/**
	 * <pre>
	 * 数据库字段: sale_area
	 * 描述: 销售区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("销售区域")
    private String saleArea;
	/**
	 * <pre>
	 * 数据库字段: current_area
	 * 描述: 当前区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("当前区域")
    private String currentArea;
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
