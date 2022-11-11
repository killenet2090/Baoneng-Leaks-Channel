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
 *	<b>表名</b>：tb_vehicle_sub_model
 *  车辆配置表，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("车辆配置表")
public class VehicleSubModel extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: 配置id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("配置id")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: sub_model_name
	 * 描述: 配置名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("配置名称")
    private String subModelName;
	/**
	 * <pre>
	 * 数据库字段: vehicle_model_id
	 * 描述: 关联车型id;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("关联车型id")
    private Long vehicleModelId;
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
