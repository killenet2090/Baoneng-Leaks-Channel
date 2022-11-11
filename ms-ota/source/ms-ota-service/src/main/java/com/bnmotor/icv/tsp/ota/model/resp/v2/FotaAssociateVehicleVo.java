package com.bnmotor.icv.tsp.ota.model.resp.v2;

import java.util.List;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaAssociateVehicleVo.java 
 * @Description: 查询升级任务V2版本待关联车辆
 * @Modify: 2021-04-06 修改内容：页面显示车辆的多个标签
 * @author E.YanLonG
 * @since 2020-12-16 17:31:36
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "升级任务待关联车辆响应参数")
@Accessors(chain = true)
@Data
public class FotaAssociateVehicleVo {

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "otaPlanId", example = "1316944117112332289")
	Long otaPlanId;
	
	@ApiModelProperty(value = "车辆vin", example = "OTA20200924BN0002", dataType = "String")
	String vin;
	
	/**
	 * tb_fota_object表主键
	 */
	@ApiModelProperty(value = "车辆objectId", example = "1331423744215764994", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long objectId;
	
	@ApiModelProperty(value = "车辆是否可以关联当前任务", example = "1可以关联 2已在其它有效任务中 3无效车辆（vin不存在）4无效车辆（非指定设备下的车辆）", dataType = "Integer")
	Integer canAssociate;
	
	@Deprecated
	@ApiModelProperty(value = "标签分组Id", example = "1357506773592428545", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long labelGroupId;
	
	@Deprecated
	@ApiModelProperty(value = "标签分组名称", example = "分组1", dataType = "String")
	String labelGroupName;
	
	@Deprecated
	@ApiModelProperty(value = "标签key", example = "1357506773592428545", dataType = "String")
	String labelKey;
	
	@Deprecated
	@ApiModelProperty(value = "标签名称", example = "实验车", dataType = "String")
	String labelVal;
	
	@ApiModelProperty(value = "地域名称", example = "北京市", dataType = "String")
	String regionName;
	
	@ApiModelProperty(value = "地域编码", example = "110000", dataType = "String")
	String regionCode;
	
	/**
	 * 一国车可能有多个标签，返回前端显示多个标签
	 */
	@ApiModelProperty(value = "标签集合", example = "[{'labelGroupId': '1310495342958923778', 'labelGroupName':'分组1', 'labelKey':'1310495342958923778', 'labelVal':'测试车辆'}]", dataType = "List")
	List<LabelInfo> labels;
}