package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanDo
 * @Description: V2版本文件上传请求参数
 * @author E.YanLonG
 * @since 2020-11-30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class VehicleUploadFileV2Req extends Page {

	@ApiModelProperty(value = "设备树Id", example = "1278883228948725762", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long treeNodeId;
	
	@ApiModelProperty(value = "车辆上传文件Key", example = "vehicle-file-20210106101718", dataType = "String")
	String fileKey;
	
	@ApiModelProperty(value = "升级任务Id", example = "1278883228948725762", dataType = "Long")
	Long otaPlanId;

}