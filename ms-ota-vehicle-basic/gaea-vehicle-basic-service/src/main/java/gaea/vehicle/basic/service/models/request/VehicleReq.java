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
 *  车辆基础信息表接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆基础信息表")
public class VehicleReq extends AbstractReq {
	/**
	 * <pre>
	 *  车辆id，自增长列;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车辆id，自增长列", example = "1000", dataType = "Long")
	@NotNull(message = "{Vehicle.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  车系id，关联车系表tb_vehicle_model表;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车系id，关联车系表tb_vehicle_model表", example = "1000", dataType = "Long")
	@NotNull(message = "{Vehicle.vehicleModelId.notNull.message}")
    private Long vehicleSubModelId;
	/**
	 * <pre>
	 *  车辆vin号，17位唯一编号;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车辆vin号，17位唯一编号", example = "测试", dataType = "String")
	@NotEmpty(message = "{Vehicle.vin.notEmpty.message}")
	@Length(max = 50, message = "{Vehicle.vin.maxLength.message}")
    private String vin;
	/**
	 * <pre>
	 *  车牌颜色，关联字典表;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "车牌颜色，关联字典表", example = "1000", dataType = "Long")
    private Long plateColor;
	/**
	 * <pre>
	 *  车牌号码;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "车牌号码", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.plateNo.maxLength.message}")
    private String plateNo;
	/**
	 * <pre>
	 *  物料号;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "物料号", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.materialNo.maxLength.message}")
    private String materialNo;
	/**
	 * <pre>
	 *  生产区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "生产区域", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.prodArea.maxLength.message}")
    private String prodArea;
	/**
	 * <pre>
	 *  上牌区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "上牌区域", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.plateArea.maxLength.message}")
    private String plateArea;
	/**
	 * <pre>
	 *  销售区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "销售区域", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.saleArea.maxLength.message}")
    private String saleArea;
	/**
	 * <pre>
	 *  当前区域;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "当前区域", example = "测试", dataType = "String")
	@Length(max = 50, message = "{Vehicle.currentArea.maxLength.message}")
    private String currentArea;
	/**
	 * <pre>
	 *  备注信息;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "备注信息", example = "测试", dataType = "String")
	@Length(max = 255, message = "{Vehicle.remark.maxLength.message}")
    private String remark;
	/**
	 * <pre>
	 *  创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{Vehicle.createTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**
	 * <pre>
	 *  最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "最后一次修改时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{Vehicle.updateTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{Vehicle.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
