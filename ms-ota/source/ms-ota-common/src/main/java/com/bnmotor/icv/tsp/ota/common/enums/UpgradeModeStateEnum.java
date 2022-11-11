package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;
import java.util.Objects;

import lombok.Getter;

/**
 * @ClassName: PlanModeStateEnum.java
 * @Description: 升级模式： 0静默升级 1正常模式 2工厂模式
 * 					升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)
 * @author E.YanLonG
 * @since 2020-12-26 18:51:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum UpgradeModeStateEnum {
	SILENCE("工厂模式", 1), // 静默下载 + 静默升级
	NORMAL("正常模式", 2), //
	FACTORY("静默模式", 3), // 静默下载
	;

	@Getter
	String key;

	@Getter
	Integer state;

	UpgradeModeStateEnum(String key, Integer state) {
		this.key = key;
		this.state = state;
	}

	public static String selectKey(Integer state) {
		UpgradeModeStateEnum t = EnumSet.allOf(UpgradeModeStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return Objects.isNull(t) ? null : t.getKey();
	}
}
