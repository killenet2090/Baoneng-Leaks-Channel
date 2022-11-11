package com.bnmotor.icv.tsp.ota.model.req.v2;

import java.util.List;

import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPreConditionPo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: AssemblyUpgradeFirmwareReq.java 
 * @Description: 组合对象
 * @author E.YanLonG
 * @since 2020-12-28 19:55:34
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaUpgradeFirmwareAssembly {
	
	FotaPlanPo fotaPlanPo;
	FotaStrategyPo fotaStrategyPo;
	
	List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos;
	List<FotaStrategyPreConditionPo> fotaStrategyPreConditionPos;
	
}