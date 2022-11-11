package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @ClassName: UpgradeLogReq.java UpgradeLogReq
 * @Description: 下载车辆TBOX升级日志文件
 * @author E.YanLonG
 * @since 2020-10-22 11:34:02
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "车辆TBOX升级日志下载请求参数")
@Data
public class DownloadUpgradeLogReq {

	@ApiModelProperty(value = "车辆vin", example = "LLNC6ADB5JA047666", dataType = "String", required = false)
	String vin;
	
	@ApiModelProperty(value = "transId", example = "1316984818185302017", dataType = "Long", required = true)
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long transId;
}