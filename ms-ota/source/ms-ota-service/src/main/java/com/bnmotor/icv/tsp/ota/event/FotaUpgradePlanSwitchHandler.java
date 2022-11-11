package com.bnmotor.icv.tsp.ota.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventPublisher;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectCacheInfoService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanPublishService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: PakcageBuildCompletedHandler.java PakcageBuildCompletedHandler
 * @Description: 任务开关切换时需要触发的动作
 *				 2021-01-23，审批状态、开关动作等都需要触发任务发布状态的变化
 * 				任务发布状态和开关，审批联动
 * @author E.YanLonG
 * @since 2020-11-16 14:53:58
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@OtaMessageAttribute(topics = OtaEventConstant.UPGRADE_PLAN_STATE_SWITCH, msgtype = FotaUpgradePlanSwitch.class)
@Slf4j
public class FotaUpgradePlanSwitchHandler implements OtaEventHandler.EventHandler<FotaUpgradePlanSwitch> {

	@Autowired
	OtaEventPublisher otaEventPublisher;

	@Autowired
	IFotaObjectCacheInfoService fotaObjectCacheInfoService;

	@Autowired
	IFotaPlanPublishService fotaPlanPublishService;

	@Autowired
	IFotaPlanDbService fotaPlanDbService;

	@Override
	public FotaUpgradePlanSwitch onMessage(FotaUpgradePlanSwitch fotaUpgradePlanSwitch) {
		log.info("FotaUpgradePlanSwitch|{}", fotaUpgradePlanSwitch);
		Integer isEnable = fotaUpgradePlanSwitch.getIsEnable();
		Long fotaPlanId = fotaUpgradePlanSwitch.getFotaPlanId();

		boolean opereateFlag = fotaObjectCacheInfoService.delFotaPlanCacheInfo(fotaPlanId);
		if (opereateFlag) {
			log.info("删除任务缓存成功|{}", fotaPlanId);
		} else {
			log.info("删除任务缓存失败|{}", fotaPlanId);
		}

//		if (EnableStateEnum.CLOSE.getState().intValue() == isEnable.intValue()) {
//			// 需要使用任务缓存失效
//		} else if (EnableStateEnum.OPEN.getState().intValue() == isEnable.intValue()) {
//			// DO NOTHING
//		}
		
		// 需要更新任务状态，审批状态等内容

		// 需要更新任务状态，审批状态等内容

		updatePlanState(fotaPlanId);
		return fotaUpgradePlanSwitch;
	}
	
	public void updatePlanState(Long otaPlanId) {
		// 更新任务发布状态
		FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(otaPlanId);
		String state0 = PublishStateEnum.selectKey(fotaPlanPo.getPublishState());
		fotaPlanPublishService.selectPublishState(fotaPlanPo);
		String state1 = PublishStateEnum.selectKey(fotaPlanPo.getPublishState());
		fotaPlanDbService.updateById(fotaPlanPo);
		log.info("更新后的升级任务发布状态|{}=>{}", state0, state1);
	}

	/*public void updatePlanState(Long otaPlanId) {
		// 更新任务发布状态
		FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(otaPlanId);
		String state0 = PublishStateEnum.selectKey(fotaPlanPo.getPublishState());
		fotaPlanPublishService.selectPublishState(fotaPlanPo);
		String state1 = PublishStateEnum.selectKey(fotaPlanPo.getPublishState());
		fotaPlanDbService.updateById(fotaPlanPo);
		log.info("更新后的升级任务发布状态|{}=>{}", state0, state1);
	}*/

}