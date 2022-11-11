package com.bnmotor.icv.tsp.ota.model.req.v2;

import java.util.List;

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
 * @ClassName: QueryUpgradeObjectV2Req.java 
 * @Description: 查询升级任务待关联车辆请求参数
 * @author E.YanLonG
 * @since 2020-12-16 16:47:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "查询升级任务待关联车辆")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class QueryUpgradeObjectV2Req extends Page {

	@ApiModelProperty(value = "车辆vin", example = "['OTA20200924BN0002']", dataType = "List")
	List<String> vins;
	
	@ApiModelProperty(value = "车辆标签key", example = "['1310495342958923777']", dataType = "List")
	List<String> labels;
	
	@ApiModelProperty(value = "车辆当前所在地域编码", example = "['110000']", dataType = "List")
	List<String> regions;
	
	@ApiModelProperty(value = "设备树id", example = "1338701600839294978", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "设备树id不能为空")
	Long treeNodeId;
	
	@ApiModelProperty(value = "任务id", example = "1357578377380347905", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long otaPlanId;

}