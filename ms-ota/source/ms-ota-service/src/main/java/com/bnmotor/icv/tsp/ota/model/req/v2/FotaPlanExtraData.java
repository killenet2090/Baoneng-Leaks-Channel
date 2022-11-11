package com.bnmotor.icv.tsp.ota.model.req.v2;

import java.util.List;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(description = "升级计划附加参数")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaPlanExtraData {

	/**
	 * 操作类型：1根据条件过滤 2根据上传文件过滤
	 */
	Integer operateType;
	
	/**
	 * 上传的文件名称
	 */
	String fileName;

	List<String> regions;
	List<String> labels;
	List<LabelInfo> labels0;
	
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long treeNodeId;
//	
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long strategyId;
//	
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long otaPlanId;
}