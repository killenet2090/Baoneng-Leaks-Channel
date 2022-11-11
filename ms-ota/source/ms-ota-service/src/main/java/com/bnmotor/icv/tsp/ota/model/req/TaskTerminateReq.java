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
 *  任务终止条件接收参数，提供服务接口模型
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel(description = "任务终止条件")
public class TaskTerminateReq extends AbstractBase {
	/**
	 * <pre>
	 *  ;字段长度:16,是否必填:是。
	 * </pre>
	 */
	@ApiModelProperty(value = "", example = "1000", dataType = "Long")
	@NotNull(message = "{TaskTerminate.id.notNull.message}", groups = { Update.class })
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
	@Length(max = 50, message = "创建用户不能为空")
    private String createBy;
	/**
	 * <pre>
	 *  更新人;字段长度:50,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "更新人", example = "测试", dataType = "String")
	@Length(max = 50, message = "更新用户不能为空")
    private String updateBy;
	/**
	 * <pre>
	 *  关联的任务ID;字段长度:20,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "关联的任务ID", example = "1000", dataType = "Long")
    private Long taskId;
	/**
	 * <pre>
	 *  最大下载失败次数;字段长度:4,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大下载失败次数", example = "1", dataType = "Integer")
    private Integer maxDownloadFailed;
	/**
	 * <pre>
	 *  最大安装失败次数;字段长度:4,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大安装失败次数", example = "1", dataType = "Integer")
    private Integer maxInstallFailed;
	/**
	 * <pre>
	 *  最大验证失败次数;字段长度:4,是否必填:否。
	 * </pre>
	 */
	@ApiModelProperty(value = "最大验证失败次数", example = "1", dataType = "Integer")
    private Integer maxVerifyFailed;

	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
	}
	
}
