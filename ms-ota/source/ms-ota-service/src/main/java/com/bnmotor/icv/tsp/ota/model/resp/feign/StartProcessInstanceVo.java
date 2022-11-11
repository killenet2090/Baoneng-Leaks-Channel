package com.bnmotor.icv.tsp.ota.model.resp.feign;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "开启流程操作VO", value = "开启流程操作VO")
public class StartProcessInstanceVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 流程定义id 必填
     */
    @NotEmpty
    @ApiModelProperty(name = "processDefinitionId", notes = "流程定义主键,开启流程实例的时候会根据这个字段查询最新版本的流程定义开启一个实例", dataType = "String", required = true)
    private String processDefinitionId;

    /**
     * 流程定义key
     */
    @ApiModelProperty(name = "processDefinitionKey", notes = "流程定义key", dataType = "String")
    private String processDefinitionKey;
    /**
     * 业务系统id 这个流程实例的唯一key  必填
     */
    @NotEmpty
    @ApiModelProperty(name = "businessKey", notes = "设置该流程实例的businessKey 该项后面会作为查询条件查询流程", dataType = "String", required = true)
    private String businessKey;

    /**
     * 启动流程变量 选填
     */
    @ApiModelProperty(name = "variables", notes = "启动流程变量 非必填 自定义流程属性时候需要", dataType = "Map", required = false)
    private Map<String, Object> variables;
    /**
     * 表单显示名称 (当前实例名称) 必填
     */
    @NotEmpty
    @ApiModelProperty(name = "name", notes = "给这个流程实例命名", dataType = "String", required = true)
    private String name;
    
    @NotEmpty
    @ApiModelProperty(name = "account", notes = "用户id/唯一账号", dataType = "String", required = true)
    public String account;
    
    @ApiModelProperty(name = "formObject", notes = "业务的 form 参数")
    private String formObject;
    
    @ApiModelProperty(name = "comment", notes = "业务注释")
    private String comment;
}
