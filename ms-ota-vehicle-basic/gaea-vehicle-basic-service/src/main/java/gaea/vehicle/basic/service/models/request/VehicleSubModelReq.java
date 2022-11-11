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
 *  车辆配置表接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆配置表")
public class VehicleSubModelReq extends AbstractReq {
	/**
	 * <pre>
	 *  配置id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "配置id", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleSubModel.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  配置名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "配置名称", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleSubModel.subModelName.notEmpty.message}")
	@Length(max = 50, message = "{VehicleSubModel.subModelName.maxLength.message}")
    private String subModelName;
	/**
	 * <pre>
	 *  关联车型id;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "关联车型id", example = "1000", dataType = "Long")
    private Long vehicleModelId;
	/**
	 * <pre>
	 *  备注信息;字段长度:255,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "备注信息", example = "测试", dataType = "String")
	@Length(max = 255, message = "{VehicleSubModel.remark.maxLength.message}")
    private String remark;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{VehicleSubModel.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
