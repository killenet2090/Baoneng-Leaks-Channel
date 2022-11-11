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

import javax.validation.constraints.NotNull;

/**
 * <pre>
 *  OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "OTA升级计划对象清单 定义一次升级中包含哪些需要升级的车辆")
public class FotaPlanObjListReq extends AbstractBase {
	/**
	 * <pre>
	 *  ;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "{FotaPlanObjList.id.notNull.message}", groups = { Update.class })
    private Long id;
	/**
	 * <pre>
	 *  升级计划id;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级计划id", example = "1000", dataType = "Long")
    private Long otaPlanId;
	/**
	 * <pre>
	 *  升级对象id;字段长度:16,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级对象id", example = "1000", dataType = "Long")
    private Long otaObjectId;
	/**
	 * <pre>
	 *  升级状态
            todo;字段长度:6,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "升级状态 todo", example = "1", dataType = "Integer")
    private Integer status;
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
	@Length(max = 50, message = "{FotaPlanObjList.createBy.maxLength.message}")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "{FotaPlanObjList.updateBy.maxLength.message}")
    private String updateBy;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
