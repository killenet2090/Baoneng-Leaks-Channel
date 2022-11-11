/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.VehicleOemMapper;
import gaea.vehicle.basic.service.models.domain.VehicleOem;
import gaea.vehicle.basic.service.models.query.VehicleOemQuery;
import gaea.vehicle.basic.service.service.VehicleOemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  车辆主机厂表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleOemService")
public class DefaultVehicleOemService implements VehicleOemService {
    /**
     * 车辆主机厂表持久操作对象
     */
    private final VehicleOemMapper vehicleOemMapper;

    @Override
	public List<VehicleOem> queryPage(VehicleOemQuery query) {
		int count = vehicleOemMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleOemMapper.queryPage(query);
		}
	}

	@Override
	public VehicleOem queryById(Long vehicleOemId) {
		return vehicleOemMapper.queryById(vehicleOemId);
	}

	@Override
	public int insertVehicleOem(VehicleOem vehicleOem) {
		vehicleOemMapper.insertVehicleOem(vehicleOem);
		return vehicleOem.getId().intValue();
	}

	@Override
	public int updateVehicleOem(VehicleOem vehicleOem) {
		return vehicleOemMapper.updateVehicleOem(vehicleOem);
	}

	@Override
	public int deleteById(Long vehicleOemId) {
		return vehicleOemMapper.deleteById(vehicleOemId);
	}

}
