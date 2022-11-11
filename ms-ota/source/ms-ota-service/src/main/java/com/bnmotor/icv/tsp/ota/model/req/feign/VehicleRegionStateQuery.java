package com.bnmotor.icv.tsp.ota.model.req.feign;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: VehicleState.java VehicleState
 * @Description: 查询车辆地区状态
 * @author E.YanLonG
 * @since 2020-12-8 21:14:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class VehicleRegionStateQuery {

	Integer current = 1;
	Integer pageSize = 100;
	Integer startRow = 0;
	String updateTime;

}