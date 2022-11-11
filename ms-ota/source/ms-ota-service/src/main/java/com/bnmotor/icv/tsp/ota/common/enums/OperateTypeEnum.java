package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;

import lombok.Getter;

/**
 * @ClassName: OperateTypeEnum.java
 * @Description: 升级任务使用 操作类型，1按条件过滤 2按上传文件过滤
 * @author E.YanLonG
 * @since 2021-1-5 9:36:17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum OperateTypeEnum {

	BY_COND(1, "按条件过滤"), //
	BY_FILE(2, "按上传文件过滤"), //
	; //

	@Getter
	Integer type;

	@Getter
	String desc;

	private OperateTypeEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public OperateTypeEnum select(Integer type) {
		OperateTypeEnum t = EnumSet.allOf(OperateTypeEnum.class) //
				.stream().filter(it -> it.type == type.intValue()) //
				.findFirst().orElse(null); //
//		return Objects.isNull(t) ? null : t.getDesc();
		return t;
	}
	
	public static boolean isCond(Integer type) {
		return BY_COND.type.intValue() == type;
	}
	
	public static boolean isFile(Integer type) {
		return BY_FILE.type.intValue() == type;
	}

}