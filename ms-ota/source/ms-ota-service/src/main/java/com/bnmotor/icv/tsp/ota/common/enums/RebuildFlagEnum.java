package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;

import lombok.Getter;

/**
 * @ClassName: RebuildFlagEnum.java 
 * @Description: 版本标识
 * @author E.YanLonG
 * @since 2021-1-13 11:18:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum RebuildFlagEnum {

	V1(0, "V1"),
	V2(1, "V2"),
	;

	@Getter
	Integer flag;

	@Getter
	String desc;

	private RebuildFlagEnum(Integer flag, String desc) {
		this.flag = flag;
		this.desc = desc;
	}

	public static RebuildFlagEnum select(Integer flag, RebuildFlagEnum defFlag) {
		RebuildFlagEnum t = EnumSet.allOf(RebuildFlagEnum.class) //
				.stream().filter(it -> it.flag == flag.intValue()) //
				.findFirst().orElse(defFlag); //
		return t;
	}
	
	public static boolean isV1(RebuildFlagEnum flag) {
		return V1.flag.intValue() == flag.getFlag().intValue();
	}
	
	public static boolean isV1(Integer flag) {
		return V1.flag.intValue() == flag;
	}
	
	public static boolean isV2(Integer flag) {
		return V2.flag.intValue() == flag;
	}

}