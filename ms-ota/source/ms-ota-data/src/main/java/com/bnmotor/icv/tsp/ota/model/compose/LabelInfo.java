package com.bnmotor.icv.tsp.ota.model.compose;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: LabelInfo.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2020-12-27 13:36:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "标签参数")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class LabelInfo {

	@ApiModelProperty(value = "车辆id", example = "1310495342958923778", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long objectId;
	
	@ApiModelProperty(value = "标签分组id", example = "1310495342958923778", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long labelGroupId;
	
	@ApiModelProperty(value = "标签分组名称", example = "Label分组", dataType = "String")
	String labelGroupName;
	
	@ApiModelProperty(value = "标签键名", example = "1310495342958923778", dataType = "String")
	String labelKey;
	
	@ApiModelProperty(value = "标签键值", example = "实验车", dataType = "String")
	String labelVal;
	
}