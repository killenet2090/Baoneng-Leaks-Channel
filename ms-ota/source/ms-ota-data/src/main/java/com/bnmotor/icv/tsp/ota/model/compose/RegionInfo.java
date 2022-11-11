package com.bnmotor.icv.tsp.ota.model.compose;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: RegionInfo.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2021-2-18 10:48:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "地域参数")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class RegionInfo {

	@ApiModelProperty(value = "地域编码", example = "110000", dataType = "String")
	String regionCode;
	
	@ApiModelProperty(value = "地域名称", example = "北京市", dataType = "String")
	String regionName;
}