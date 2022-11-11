/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.VehicleEcuBomMapper;
import gaea.vehicle.basic.service.models.domain.VehicleEcuBom;
import gaea.vehicle.basic.service.models.query.VehicleEcuBomQuery;
import gaea.vehicle.basic.service.models.request.VehicleEcuBomRequest;
import gaea.vehicle.basic.service.service.VehicleEcuBomService;
import org.springframework.beans.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  ECU信息表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleEcuBomService")
public class DefaultVehicleEcuBomService implements VehicleEcuBomService {
    /**
     * ECU信息表持久操作对象
     */
    private final VehicleEcuBomMapper vehicleEcuBomMapper;

    @Override
	public List<VehicleEcuBom> queryPage(VehicleEcuBomQuery query) {
		int count = vehicleEcuBomMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleEcuBomMapper.queryPage(query);
		}
	}

	@Override
	public VehicleEcuBom queryById(Long vehicleEcuBomId) {
		return vehicleEcuBomMapper.queryById(vehicleEcuBomId);
	}

	@Override
	public Long insertVehicleEcuBom(VehicleEcuBom vehicleEcuBom) {
		vehicleEcuBomMapper.insertVehicleEcuBom(vehicleEcuBom);
		return vehicleEcuBom.getId();
	}

	@Override
	public int updateVehicleEcuBom(VehicleEcuBom vehicleEcuBom) {
		return vehicleEcuBomMapper.updateVehicleEcuBom(vehicleEcuBom);
	}

	@Override
	public int deleteById(Long vehicleEcuBomId) {
		return vehicleEcuBomMapper.deleteById(vehicleEcuBomId);
	}

	@Override
	public List<VehicleEcuBom> queryByVin(VehicleEcuBomRequest vehicleEcuBomReq) {
		VehicleEcuBom vehicleEcuBom = new VehicleEcuBom();
		BeanUtils.copyProperties(vehicleEcuBomReq, vehicleEcuBom);
		return vehicleEcuBomMapper.queryByVin(vehicleEcuBom);
	}

}
