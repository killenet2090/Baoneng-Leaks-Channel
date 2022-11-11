package com.bnmotor.icv.tsp.ota.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanVehicleCountPo.java 
 * @Description: 查询的临时返回对象
 * @author E.YanLonG
 * @since 2020-12-26 21:44:21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaPlanVehicleCountPo {

	Integer batchSize;
	Long otaPlanId;
}
