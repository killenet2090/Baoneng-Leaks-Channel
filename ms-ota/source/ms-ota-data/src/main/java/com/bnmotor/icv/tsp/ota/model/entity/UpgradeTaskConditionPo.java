/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 *	<b>表名</b>：tb_fota_upgrade_condition_value
 *  终端升级条件枚举值，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 * 
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("终端升级条件枚举值")
@Accessors(chain = true)
@TableName("tb_upgrade_task_condition")
public class UpgradeTaskConditionPo extends BasePo {
	@ApiModelProperty("主键")
    private Long id;

	@ApiModelProperty("otaPlanId")
	private Long otaPlanId;

	@ApiModelProperty("项目ID多租户，不同的租户具有不同的项目ID")
    private String projectId;

	@ApiModelProperty("条件项ID")
    private Long conditionId;

	@ApiModelProperty("条件代码")
	@TableField(exist = false)
	private Long condCode;

	@ApiModelProperty("条件名称")
	@TableField(exist = false)
	private String condName;

	@ApiModelProperty("值来源方式")
	@TableField(exist = false)
	private Integer valueSource;

	@ApiModelProperty("值类型")
	@TableField(exist = false)
	private Integer valueType;

	@ApiModelProperty("数据记录版本")
    private Integer version;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
