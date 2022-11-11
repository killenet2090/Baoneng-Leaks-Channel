package com.bnmotor.icv.tsp.ota.model.resp.v2;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleCountQuery.java 
 * @Description: 查询该升级计划下的车辆数量
 * @author E.YanLonG
 * @since 2020-12-26 19:28:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Data
@Accessors(chain = true)
public class VehicleCountVo {

	 Long fotaStrategyId;
	 Long fotaPlanId;
	 Integer batchSize;
	
}