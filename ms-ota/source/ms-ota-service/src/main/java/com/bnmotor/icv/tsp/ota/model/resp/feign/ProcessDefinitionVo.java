package com.bnmotor.icv.tsp.ota.model.resp.feign;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ProcessDefinitionVo.java 
 * @Description: 审批平台流程定义响应参数
 * @author E.YanLonG
 * @since 2021-3-23 16:47:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ProcessDefinitionVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "流程定义id", notes = "流程定义id", dataType = "String")
	private String id;

	@ApiModelProperty(name = "流程定义key", notes = "流程定义key", dataType = "String")
	private String key;

	@ApiModelProperty(name = "流程定义名称", notes = "流程定义名称", dataType = "String")
	private String name;

	@ApiModelProperty(name = "流程定义版本", notes = "流程定义版本", dataType = "Integer")
	private int version;

	@ApiModelProperty(name = "流程定义分类", notes = "流程定义分类(暂时没用)", dataType = "String")
	private String category;

	@ApiModelProperty(name = "流程部署生成流程定义的部署id", notes = "流程部署生成流程定义的部署id", dataType = "String")
	private String deploymentId;

	@ApiModelProperty(name = "流程资源名称", notes = "流程资源名称", dataType = "String")
	private String resourceName;

	@ApiModelProperty(name = "dgrm资源名称", notes = "dgrm资源名称", dataType = "String")
	private String dgrmResourceName;

	@ApiModelProperty(name = "流程定义状态", notes = "状态: 1激活 2挂起", dataType = "Integer")
	private int suspensionState;

	@ApiModelProperty(name = "所属系统", notes = "所属系统", dataType = "String")
	private String tenantId;

	@ApiModelProperty(name = "account", notes = "用户id/唯一账号", dataType = "String", required = true)
	private String account;
}