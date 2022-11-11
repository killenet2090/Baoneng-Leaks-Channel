package com.bnmotor.icv.tsp.ota.model.compose;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleInfo.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2021-2-18 10:48:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
@ApiModel(value = "车辆对象", description = "车辆对象")
public class VehicleInfo {
	String vin;
	Long objectId;
}