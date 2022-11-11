package com.bnmotor.icv.tsp.ota.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaUpgradePlanSwitch.java 
 * @Description: 升级任务状态变化
 * @author E.YanLonG
 * @since 2021-1-18 10:39:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaUpgradePlanSwitch {

	Long fotaPlanId;
//	Long strategyId;
	Integer isEnable;
}