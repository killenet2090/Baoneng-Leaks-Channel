package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

/**
 * @ClassName: ApprovalTypeEnum.java
 * @Description: 审批类型 1策略 2任务
 * @author E.YanLonG
 * @since 2021-3-24 9:33:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ApprovalTypeEnum {

	UPGRADE_PLAN(2), // 任务审批
	UPGRADE_STRATEGY(1); // 策略审批

	@Getter
	int type;

	private ApprovalTypeEnum(int type) {
		this.type = type;
	}
	
	public static ApprovalTypeEnum select(int type) {
		for (ApprovalTypeEnum va: values()) {
			if (va.type == type) {
				return va;
			}
		}
		return null;
	}
	
	public static boolean isFotaStrategy(int type) {
		return UPGRADE_STRATEGY.type == type;
	}
	
	public static boolean isFotaPlan(int type) {
		return UPGRADE_PLAN.type == type;
	}

}