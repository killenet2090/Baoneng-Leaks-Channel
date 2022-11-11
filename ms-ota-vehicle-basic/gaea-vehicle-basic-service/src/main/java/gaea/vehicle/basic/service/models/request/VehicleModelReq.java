/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.request;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 *  接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "")
public class VehicleModelReq extends AbstractReq {
	/**
	 * <pre>
	 *  车型id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车型id", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleModel.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  车型名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车型名称", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleModel.modelName.notEmpty.message}")
	@Length(max = 50, message = "{VehicleModel.modelName.maxLength.message}")
    private String modelName;
	/**
	 * <pre>
	 *  车型编码;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "车型编码", example = "测试", dataType = "String")
	@Length(max = 50, message = "{VehicleModel.modelCode.maxLength.message}")
    private String modelCode;
	/**
	 * <pre>
	 *  车系id，关联tb_vehicle_series表;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "车系id，关联tb_vehicle_series表", example = "1", dataType = "Integer")
    private Integer vehicleSeriesId;
	/**
	 * <pre>
	 *  车型年款;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "车型年款", example = "测试", dataType = "String")
	@Length(max = 50, message = "{VehicleModel.modelYear.maxLength.message}")
    private String modelYear;
	/**
	 * <pre>
	 *  最高车速，单位：km/h;字段长度:5,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最高车速，单位：km/h", example = "1", dataType = "Integer")
    private Integer maxSpeed;
	/**
	 * <pre>
	 *  续航里程;字段长度:5,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "续航里程", example = "1", dataType = "Integer")
    private Integer cruisingMileage;
	/**
	 * <pre>
	 *  驱动方式，关联字典表id;字段长度:3,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "驱动方式，关联字典表id", example = "1", dataType = "Integer")
    private Integer drivingMode;
	/**
	 * <pre>
	 *  排量;字段长度:10,0,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "排量", example = "1.00", dataType = "Double")
    private Double displacement;
	/**
	 * <pre>
	 *  排放标准，国6等，关联字典表id;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "排放标准，国6等，关联字典表id", example = "1000", dataType = "Long")
    private Long emissionStandard;
	/**
	 * <pre>
	 *  电池容量;字段长度:10,0,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "电池容量", example = "1.00", dataType = "Double")
    private Double batteryCapacity;
	/**
	 * <pre>
	 *  变速箱类型，CVT等，关联字典表id;字段长度:10,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "变速箱类型，CVT等，关联字典表id", example = "1", dataType = "Integer")
    private Integer gearboxType;
	/**
	 * <pre>
	 *  备注信息;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "备注信息", example = "测试", dataType = "String")
	@Length(max = 255, message = "{VehicleModel.remark.maxLength.message}")
    private String remark;
	/**
	 * <pre>
	 *  创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleModel.createTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**
	 * <pre>
	 *  最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "最后一次修改时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleModel.updateTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{VehicleModel.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
