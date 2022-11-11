package com.bnmotor.icv.tsp.ota.model.resp.feign;

import java.io.Serializable;

import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "流程定义查询业务实体", value = "流程定义查询业务实体")
public class ProcessDefinitionQueryVo extends PageRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 流程定义名称
	 */
	@ApiModelProperty(name = "name", notes = "流程定义名称", dataType = "String")
	private String name;

	/**
	 * 流程定义key
	 */
	@ApiModelProperty(name = "key", notes = "流程定义key", dataType = "String")
	private String key;

}