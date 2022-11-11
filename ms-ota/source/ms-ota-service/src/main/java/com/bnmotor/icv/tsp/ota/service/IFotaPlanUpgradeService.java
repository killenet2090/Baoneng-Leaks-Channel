package com.bnmotor.icv.tsp.ota.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.util.MyBusinessUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: IFotaPlanUpgradeService.java
 * @Description: 版本检查时获取有效任务
 * @author E.YanLonG
 * @since 2021-2-4 8:49:15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class IFotaPlanUpgradeService {

	@Autowired
	IFotaPlanObjListDbService fotaPlanObjListDbService;
	
	@Autowired
	IFotaObjectCacheInfoService fotaObjectCacheInfoService;

	public FotaPlanPo selectPlan4Upgrade(Long otaObjectId, List<FotaPlanObjListPo> fotaPlanObjListPos) {
		
		// 查询出截止时间大于当前时间的任务 未来使用缓存，任务变更时同步推送更新缓存
		List<FotaPlanObjListPo> fotaPlanObjListPoList = fotaPlanObjListDbService.queryPlanObjectListByTime(otaObjectId, new Date());

		List<Long> planIds = fotaPlanObjListPoList.stream().map(FotaPlanObjListPo::getOtaPlanId).collect(Collectors.toList());
		List<FotaPlanPo> FotaPlanPoList = planIds.stream().map((planId) -> fotaObjectCacheInfoService.getFotaPlanCacheInfo(planId)).collect(Collectors.toList());
		List<FotaPlanPo> fotaPlanList = FotaPlanPoList.stream().filter(planPo -> MyBusinessUtil.validPlanPo(planPo)).collect(Collectors.toList());
		
		// 最早的有效任务
		fotaPlanList = fotaPlanList.stream().sorted(Comparator.comparing(FotaPlanPo::getPlanStartTime)).collect(Collectors.toList());
		FotaPlanPo fotaPlanPo = fotaPlanList.stream().findFirst().orElse(null);
		Optional.ofNullable(fotaPlanPo).ifPresent(plan -> {
			fotaPlanObjListPos.addAll(fotaPlanObjListPoList.stream().filter(it -> it.getOtaPlanId().equals(plan.getId())).collect(Collectors.toList()));
		}); 
		return fotaPlanPo;
	}
	
}