package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;
import java.util.Objects;

import lombok.Getter;

/**
 * @ClassName: EnableStateEnum.java 
 * @Description: 任务开关状态
 * @author E.YanLonG
 * @since 2020-12-26 18:30:04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum EnableStateEnum {
	OPEN("打开", 1), //
	CLOSE("关闭", 0), //
	;

	@Getter
	String key;
	
	@Getter
	Integer state;

	EnableStateEnum(String key, Integer state) {
		this.key = key;
		this.state = state;
	}
	
	public static String selectKey(Integer state) {
		EnableStateEnum t = EnumSet.allOf(EnableStateEnum.class).stream().filter(item -> item.getState().equals(state)).findFirst().orElse(null);
		return Objects.isNull(t) ? null : t.getKey();
	}
	
	public static EnableStateEnum select(Integer state) {
		EnableStateEnum t = EnumSet.allOf(EnableStateEnum.class).stream().filter(item -> item.getState().equals(state)).findFirst().orElse(null);
		return t;
	}
}
