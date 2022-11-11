package com.bnmotor.icv.tsp.ota.model.req.v2;

import javax.validation.constraints.NotNull;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: QueryAssociatedObjectV2Req.java 
 * @Description: 查询指定的升级任务已关联的车辆（分页查询）
 * @author E.YanLonG
 * @since 2021-1-9 10:44:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "查询升级任务已关联车辆")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class QueryAssociatedObjectV2Req extends Page {

	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "任务ID不能为空")
	Long otaPlanId;
	
//	@ApiModelProperty(value = "设备树Id", example = "1328997695695290369", dataType = "Long", hidden = true)
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long treeNodeId;

}