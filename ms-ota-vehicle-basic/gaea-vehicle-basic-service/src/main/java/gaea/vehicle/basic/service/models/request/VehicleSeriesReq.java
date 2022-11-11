/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.request;

import java.util.Date;
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
 *  车辆系列表接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "车辆系列表")
public class VehicleSeriesReq extends AbstractReq {
	/**
	 * <pre>
	 *  车系id，自增长id;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "车系id，自增长id", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleSeries.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  系列名称;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "系列名称", example = "测试", dataType = "String")
	@Length(max = 50, message = "{VehicleSeries.seriesName.maxLength.message}")
    private String seriesName;
	/**
	 * <pre>
	 *  品牌id，关联tb_vehcile_brand表;字段长度:20,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "品牌id，关联tb_vehcile_brand表", example = "1000", dataType = "Long")
	@NotNull(message = "{VehicleSeries.vehicleBrandId.notNull.message}")
    private Long vehicleBrandId;
	/**
	 * <pre>
	 *  创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleSeries.createTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**
	 * <pre>
	 *  最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "最后一次修改时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{VehicleSeries.updateTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{VehicleSeries.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
