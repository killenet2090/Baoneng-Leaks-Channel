/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.tsp.ota.model.validate.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <pre>
 *  终端升级条件项目定义接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "终端升级条件项目定义")
public class FotaUpgradeConditionReq extends AbstractBase {
	/**
	 * <pre>
	 *  ID;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "ID", example = "1000", dataType = "Long")
	@NotNull(message = "{FotaUpgradeCondition.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  项目ID
            多租户，不同的租户具有不同的项目ID;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "项目ID 多租户，不同的租户具有不同的项目ID", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaUpgradeCondition.projectId.maxLength.message}")
    private String projectId;
	/**
	 * <pre>
	 *  终端升级条件代码
            比如：
            1- 速度
            2- 电压
            3- ....
            ;字段长度:50,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "终端升级条件代码 比如： 1- 速度 2- 电压 3- .... ", example = "测试", dataType = "String")
	@NotEmpty(message = "{FotaUpgradeCondition.condCode.notEmpty.message}")
	@Length(max = 50, message = "{FotaUpgradeCondition.condCode.maxLength.message}")
    private String condCode;
	/**
	 * <pre>
	 *  条件名称;字段长度:100,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "条件名称", example = "测试", dataType = "String")
	@Length(max = 100, message = "{FotaUpgradeCondition.condName.maxLength.message}")
    private String condName;
	/**
	 * <pre>
	 *  值来源方式
            0-预定义枚举值
            1-手工输入;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "值来源方式 0-预定义枚举值 1-手工输入", example = "1", dataType = "Integer")
    private Integer valueSource;
	/**
	 * <pre>
	 *  值类型
            0 - 数值型
            1-字符型;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "值类型 0 - 数值型 1-字符型", example = "1", dataType = "Integer")
    private Integer valueType;
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
	@Length(max = 50, message = "{FotaUpgradeCondition.createBy.maxLength.message}")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaUpgradeCondition.updateBy.maxLength.message}")
    private String updateBy;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
