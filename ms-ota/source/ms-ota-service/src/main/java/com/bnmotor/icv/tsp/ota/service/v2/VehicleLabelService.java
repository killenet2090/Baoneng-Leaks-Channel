package com.bnmotor.icv.tsp.ota.service.v2;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: VehicleLabelService.java VehicleLabelService
 * @Description: 根据标签选择车辆
 * @author E.YanLonG
 * @since 2020-12-1 11:51:54
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class VehicleLabelService {

	public List<?> selectVehicleByLabel(String label) {
		return Lists.newArrayList();
	}

}