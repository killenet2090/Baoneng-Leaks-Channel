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
 *	<b>表名</b>：tb_vehicle_brand
 *  车辆品牌表，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("车辆品牌表")
public class VehicleBrand extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: 品牌id，自增长列;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("品牌id，自增长列")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: brand_name
	 * 描述: 品牌名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("品牌名称")
    private String brandName;
	/**
	 * <pre>
	 * 数据库字段: vehicle_oem_id
	 * 描述: 主机厂id，关联tb_vehicle_oem表;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("主机厂id，关联tb_vehicle_oem表")
    private Long vehicleOemId;
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

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
