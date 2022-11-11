/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import javax.validation.constraints.NotNull;

import com.bnmotor.icv.tsp.ota.model.validate.Update;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *  终端升级条件枚举值接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "终端升级条件枚举值")
public class FotaUpgradeConditionValueReq extends AbstractBase {
	/**
	 * <pre>
	 *  主键;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "主键", example = "1000", dataType = "Long")
	@NotNull(message = "", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  项目ID
            多租户，不同的租户具有不同的项目ID;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "项目ID 多租户，不同的租户具有不同的项目ID", example = "测试", dataType = "String")
	@Length(max = 50, message = "")
    private String projectId;
	/**
	 * <pre>
	 *  条件项ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "条件项ID", example = "1000", dataType = "Long")
	@NotNull(message = "")
    private Long conditionId;
	/**
	 * <pre>
	 *  条件值;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "条件值", example = "测试", dataType = "String")
	@Length(max = 50, message = "")
    private String value;
	/**
	 * <pre>
	 *  ;字段长度:11,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1", dataType = "Integer")
    private Integer version;
	/**
	 * <pre>
	 *  删除标志
            0 - 正常
            1 - 删除;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "删除标志 0 - 正常 1 - 删除", example = "1", dataType = "Integer")
    private Integer delFlag;
	/**
	 * <pre>
	 *  创建人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "创建人", example = "测试", dataType = "String")
	@Length(max = 50, message = "")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "")
    private String updateBy;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
