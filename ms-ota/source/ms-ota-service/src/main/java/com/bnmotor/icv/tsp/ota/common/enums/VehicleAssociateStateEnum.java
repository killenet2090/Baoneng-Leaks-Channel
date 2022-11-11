package com.bnmotor.icv.tsp.ota.common.enums;

import java.util.EnumSet;

import lombok.Getter;

/**
 * @ClassName: VehicleAssociateStateEnum.java 
 * @Description: 车辆待关联状态：1可以关联 2已在其它有效任务中 3无效车辆（vin不存在）4无效车辆（非指定设备下的车辆）
 * @author E.YanLonG
 * @since 2021-1-6 14:48:27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public  enum VehicleAssociateStateEnum {
	CAN_ASSOCIATE(1, "可以关联"), //
	IN_OTHER_PLANS(2, "已在其它有效任务中"), //
	NO_EXISTS_VEHICLE(3, "无效车辆（VIN不存在）"), //
	NO_IN_TARGET_TREE(4, "非指定设备下的车辆"), //
	OTHER_REASON(99, "其它状态"), //
	; //

	@Getter
	Integer type;

	@Getter
	String desc;

	private VehicleAssociateStateEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public VehicleAssociateStateEnum select(Integer type) {
		return EnumSet.allOf(VehicleAssociateStateEnum.class) //
				.stream().filter(it -> it.type == type.intValue()) //
				.findFirst().orElse(null); //
	}
	
}