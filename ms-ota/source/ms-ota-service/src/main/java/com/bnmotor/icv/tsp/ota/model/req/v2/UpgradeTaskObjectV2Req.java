package com.bnmotor.icv.tsp.ota.model.req.v2;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UpgradeTaskObjectV2Req.java UpgradeTaskObjectV2Req
 * @Description: 
 * @author E.YanLonG
 * @since 2020-12-14 15:28:38
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class UpgradeTaskObjectV2Req {

	@ApiModelProperty(value = "车辆vin", example = "OTA20200924BN0002", dataType = "String")
	@NotNull(message = "车辆vin码不能为空")
	String vin;
	
	/**
	 * tb_fota_object表主键
	 */
	@ApiModelProperty(value = "车辆objectId", example = "1331423744215764994", dataType = "Long")
	@NotNull(message = "车辆objectId不能为空")
	Long objectId;
}