package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;
import java.util.Objects;

import lombok.Getter;

/**
 * @ClassName: PlanModeStateEnum.java
 * @Description: 任务模式： 0测试任务 1正式任务
 * @author E.YanLonG
 * @since 2020-12-26 18:51:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum PlanModeStateEnum {
	TEST("测试任务", 0), //
	FORMAL("正式任务", 1), //
	;

	@Getter
	String key;

	@Getter
	Integer state;

	PlanModeStateEnum(String key, Integer state) {
		this.key = key;
		this.state = state;
	}

	public static String selectKey(Integer state) {
		PlanModeStateEnum t = EnumSet.allOf(PlanModeStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return Objects.isNull(t) ? null : t.getKey();
	}
	
	public static PlanModeStateEnum select(Integer state) {
		PlanModeStateEnum t = EnumSet.allOf(PlanModeStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return Objects.isNull(t) ? t : t;
	}
	
	public static boolean isTestsPlan(Integer state) {
		return TEST.state.equals(state);
	}
}
