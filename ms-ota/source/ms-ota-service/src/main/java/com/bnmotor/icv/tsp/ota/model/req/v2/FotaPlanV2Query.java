package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *  OTA升级计划表 查询条件,专门提供查询使用，请勿当成提交数据保存使用，
 *	如果有必要新增额外领域模型数据，可以单独创建，请勿使用查询对象做提交数据保存
 * </pre>
 *
 * @author E.YanLonG
 * @version 1.0.0
 */
@ApiModel(description = "OTA升级计划V2")
@Getter
@Setter
public class FotaPlanV2Query extends Page {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "/品牌/车系/车型/年款/配置（叶子节点Id）", example = "1278883228990668801", dataType = "Long", required = false)
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long treeNodeId;

}