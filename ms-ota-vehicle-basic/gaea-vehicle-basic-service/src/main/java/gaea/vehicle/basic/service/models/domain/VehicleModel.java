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
 *	<b>表名</b>：tb_vehicle_model
 *  ，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("")
public class VehicleModel extends AbstractBase {
	/**
	 * <pre>
	 * 数据库字段: id
	 * 描述: 车型id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("车型id")
    private Long id;
	/**
	 * <pre>
	 * 数据库字段: model_name
	 * 描述: 车型名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty("车型名称")
    private String modelName;
	/**
	 * <pre>
	 * 数据库字段: model_code
	 * 描述: 车型编码;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("车型编码")
    private String modelCode;
	/**
	 * <pre>
	 * 数据库字段: vehicle_series_id
	 * 描述: 车系id，关联tb_vehicle_series表;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("车系id，关联tb_vehicle_series表")
    private Integer vehicleSeriesId;
	/**
	 * <pre>
	 * 数据库字段: model_year
	 * 描述: 车型年款;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("车型年款")
    private String modelYear;
	/**
	 * <pre>
	 * 数据库字段: max_speed
	 * 描述: 最高车速，单位：km/h;字段长度:5,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("最高车速，单位：km/h")
    private Integer maxSpeed;
	/**
	 * <pre>
	 * 数据库字段: cruising_mileage
	 * 描述: 续航里程;字段长度:5,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("续航里程")
    private Integer cruisingMileage;
	/**
	 * <pre>
	 * 数据库字段: driving_mode
	 * 描述: 驱动方式，关联字典表id;字段长度:3,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("驱动方式，关联字典表id")
    private Integer drivingMode;
	/**
	 * <pre>
	 * 数据库字段: displacement
	 * 描述: 排量;字段长度:10,0,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("排量")
    private Double displacement;
	/**
	 * <pre>
	 * 数据库字段: emission_standard
	 * 描述: 排放标准，国6等，关联字典表id;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("排放标准，国6等，关联字典表id")
    private Long emissionStandard;
	/**
	 * <pre>
	 * 数据库字段: battery_capacity
	 * 描述: 电池容量;字段长度:10,0,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("电池容量")
    private Double batteryCapacity;
	/**
	 * <pre>
	 * 数据库字段: gearbox_type
	 * 描述: 变速箱类型，CVT等，关联字典表id;字段长度:10,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty("变速箱类型，CVT等，关联字典表id")
    private Integer gearboxType;
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
