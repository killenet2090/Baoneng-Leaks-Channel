/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 *	<b>表名</b>：tb_fota_upgrade_condition
 *  终端升级条件项目定义，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_upgrade_condition")
@ApiModel(value="FotaUpgradeConditionPo对象", description="终端升级条件项目定义")
public class FotaUpgradeConditionPo extends BasePo {

	@ApiModelProperty("ID")
    private Long id;

	@ApiModelProperty("项目ID 多租户，不同的租户具有不同的项目ID")
    private String projectId;

	@ApiModelProperty("终端升级条件代码 比如： 1- 速度 2- 电压 3- .... ")
    private String condCode;

	@ApiModelProperty("条件名称")
    private String condName;

	@ApiModelProperty("值来源方式 0-预定义枚举值 1-手工输入")
    private Integer valueSource;

	@ApiModelProperty("值类型 0 - 数值型 1-字符型")
    private Integer valueType;

	@ApiModelProperty("条件值")
	private String value;

	@ApiModelProperty("数据库版本字段")
    private Integer version;
	
	@ApiModelProperty("运算符类型:0=关系运算符(> < == != >= <=),1=逻辑运算符")
	private Integer operatorType;
	
	@ApiModelProperty("运算符")
	private Integer operatorValue;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
}
