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
public class DictionaryReq extends AbstractReq {
	/**
	 * <pre>
	 *  字典表id;字段长度:255,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "字典表id", example = "1000", dataType = "Long")
	@NotNull(message = "{Dictionary.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  字典键;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "字典键", example = "测试", dataType = "String")
	@NotEmpty(message = "{Dictionary.dicKey.notEmpty.message}")
	@Length(max = 50, message = "{Dictionary.dicKey.maxLength.message}")
    private String dicKey;
	/**
	 * <pre>
	 *  字典值;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "字典值", example = "测试", dataType = "String")
	@NotEmpty(message = "{Dictionary.dicValue.notEmpty.message}")
	@Length(max = 50, message = "{Dictionary.dicValue.maxLength.message}")
    private String dicValue;
	/**
	 * <pre>
	 *  创建时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{Dictionary.createTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**
	 * <pre>
	 *  最后一次修改时间;字段长度:,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "最后一次修改时间", example = "2020-04-07 15:17:58", dataType = "Date")
	@NotNull(message = "{Dictionary.updateTime.notNull.message}")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**
	 * <pre>
	 *  是否有效，1有效；0无效;字段长度:3,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "是否有效，1有效；0无效", example = "1", dataType = "Integer")
	@NotNull(message = "{Dictionary.isActive.notNull.message}")
    private Integer isActive;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
