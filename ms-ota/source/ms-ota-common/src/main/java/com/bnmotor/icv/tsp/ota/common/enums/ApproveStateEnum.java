package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: ApproveStateEnum.java 
 * @Description: 审批状态枚举:1 免审批，2待审批、3审批中、4已审批、5拒绝 6驳回 7撤回
 * @author E.YanLonG
 * @since 2020-12-26 18:10:04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ApproveStateEnum {

	NO_NEED_APPROVE("免审批", 1), //
	READY_FOR_APPROVE("待审批", 2), //
	IN_PROCESS_APPROVE("审批中", 3), //

	BE_APPROVED("审批通过", 4), //
	REJECTED_APPROVE("未通过", 5), //
	REVIEW_APPROVAL("驳回", 6), //
	REVOKE_APPROVAl("撤回", 7), //
	;

	@Getter
	String key;
	
	@Getter
	Integer state;

	ApproveStateEnum(String key, Integer state) {
		this.key = key;
		this.state = state;
	}
	
	public static String selectKey(Integer state) {
		ApproveStateEnum t = EnumSet.allOf(ApproveStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return Objects.isNull(t) ? null : t.getKey();
	}
	
	public static boolean approved(Integer state) {
		List<Integer> target = Arrays.asList(NO_NEED_APPROVE.getState(), BE_APPROVED.getState());
		return target.contains(state);
	}
	
//	public static boolean rejected(Integer state) {
//		List<Integer> target = Arrays.asList(REJECTED_APPROVE.getState());
//		return target.contains(state);
//	}
	
	public static ApproveStateEnum select(Integer state) {
		ApproveStateEnum t = EnumSet.allOf(ApproveStateEnum.class).stream().filter(item -> item.getState().intValue() == state.intValue()).findFirst().orElse(null);
		return t;
	}

}