package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;
import org.assertj.core.util.Lists;

import java.util.*;

/**
 * @ClassName: PublishStateEnum.java 
 * @Description: 发布状态枚举
 * <p>
 * <SPAN style="TEXT-DECORATION: line-through">发布状态: 0待发布 1发布中 2延迟发布 3已失效</SPAN>
 * </p>
 * <p>
 * 发布状态: 1待发布  2发布中 4延迟发布 5已失效 6暂停发布
 * </p>
 * 
 * @author E.YanLonG
 * @since 2020-12-26 18:09:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum PublishStateEnum {

	UNPUBLISH("待发布", 1), //
	IN_PUBLISHING("发布中", 2), //
//	PUBLISHED("已发布", 3), // 已发布
	DELAY_PUBLISH("延迟发布", 4), //
	INVALID("已失效", 5), //
	SUSPEND("暂停发布", 6), //
	;

	@Getter
	String key;
	
	@Getter
	Integer state;

	PublishStateEnum(String key, Integer state) {
		this.key = key;
		this.state = state;
	}
	
	public static String selectKey(Integer state) {
		PublishStateEnum t = EnumSet.allOf(PublishStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return Objects.isNull(t) ? null : t.getKey();
	}

	public static PublishStateEnum select(Integer state) {
		PublishStateEnum t = EnumSet.allOf(PublishStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return t;
	}
	
	public static boolean inPublishState(Integer state) {
		List<Integer> target = Arrays.asList(IN_PUBLISHING.getState());
		return target.contains(state);
	}

	/**
	 * 是否已失效
	 * @param state
	 * @return
	 */
	public static boolean inValid(Integer state) {
		List<Integer> target = Arrays.asList(INVALID.getState());
		return target.contains(state);
	}


	public static PublishStateEnum getByState(int state) {
		return EnumSet.allOf(PublishStateEnum.class).stream().filter(item -> item.state == state).findFirst().orElse(null);
	}

	public static List<Map<String,Object>> typeDescList = Lists.newArrayList();

	static {
		EnumSet<PublishStateEnum> publishStateEnums = EnumSet.allOf(PublishStateEnum.class);
		for (PublishStateEnum publishStateEnum:publishStateEnums ){
			Map<String,Object> typeDescMap = new HashMap();
			typeDescMap.put("type",publishStateEnum.getState());
			typeDescMap.put("desc",publishStateEnum.getKey());
			typeDescList.add(typeDescMap);
		}
	}
}