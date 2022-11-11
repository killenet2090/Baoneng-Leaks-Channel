package com.bnmotor.icv.tsp.ota.model.resp.feign;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SubmitProcessVo.java 
 * @Description: 发起人提交表单
 * @author E.YanLonG
 * @since 2021-3-24 17:03:04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(description = "流程发起人提交表单操作", value = "流程发起人提交表单操作")
public class SubmitProcessVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 流程实例主键 必填
	 */
	@NotEmpty
	@ApiModelProperty(name = "processInstanceId", notes = "流程实例主键", dataType = "String", required = true)
	private String processInstanceId;

	@NotEmpty
	@ApiModelProperty(name = "account", notes = "用户id/唯一账号", dataType = "String", required = true)
	public String account;

	/**
	 * 业务form 参数 必填
	 */
	@ApiModelProperty(name = "formObject", notes = "业务的 form 参数")
	private Object formObject;
	
	@ApiModelProperty(name = "comment", notes = "备注")
	private String comment;
}