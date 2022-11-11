package com.bnmotor.icv.tsp.ota.model.resp.v2;

import java.util.List;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.compose.GroupInfo;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通过treeNodeId返回过滤条件
 * @ClassName: FotaPlanConditionVo.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2020-12-26 13:28:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaPlanConditionVo {

//	@ApiModelProperty(value = "otaPlanId", example = "1316944117112332289", dataType = "Long")
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long otaPlanId;
//	
//	@ApiModelProperty(value = "fotaStrategyId", example = "1316944117112332289", dataType = "Long")
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long fotaStrategyId;
	
	@ApiModelProperty(value = "地域（北京市、深圳市）", example = "北京市", dataType = "String")
	List<RegionInfo> regions;
	
	@ApiModelProperty(value = "标签", example = "实验车", dataType = "String")
	List<LabelInfo> labels;
	
	@ApiModelProperty(value = "标签", example = "实验车", dataType = "String")
	List<GroupInfo> groups;
	
	@ApiModelProperty(value = "设备树Id", example = "1278883228948725762", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long treeNodeId;
	
}
