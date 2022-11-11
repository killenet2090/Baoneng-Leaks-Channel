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
 *  车辆主机厂表接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆主机厂表")
public class VehicleOemReq extends AbstractReq {
	/**
	 * <pre>
	 *  主机厂id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "主机厂id", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleOem.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  主机厂名称;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "主机厂名称", example = "测试", dataType = "String")
	@NotEmpty(message = "{VehicleOem.oemName.notEmpty.message}")
	@Length(max = 50, message = "{VehicleOem.oemName.maxLength.message}")
    private String oemName;
	/**
	 * <pre>
	 *  创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleOem.createTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**
	 * <pre>
	 *  最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "最后一次修改时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleOem.updateTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
