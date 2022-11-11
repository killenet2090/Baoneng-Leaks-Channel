/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.VehicleSubModelMapper;
import gaea.vehicle.basic.service.models.domain.VehicleSubModel;
import gaea.vehicle.basic.service.models.query.VehicleSubModelQuery;
import gaea.vehicle.basic.service.service.VehicleSubModelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  车辆配置表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleSubModelService")
public class DefaultVehicleSubModelService implements VehicleSubModelService {
    /**
     * 车辆配置表持久操作对象
     */
    private final VehicleSubModelMapper vehicleSubModelMapper;

    @Override
	public List<VehicleSubModel> queryPage(VehicleSubModelQuery query) {
		int count = vehicleSubModelMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleSubModelMapper.queryPage(query);
		}
	}

	@Override
	public VehicleSubModel queryById(Long vehicleSubModelId) {
		return vehicleSubModelMapper.queryById(vehicleSubModelId);
	}

	@Override
	public Long insertVehicleSubModel(VehicleSubModel vehicleSubModel) {
		vehicleSubModelMapper.insertVehicleSubModel(vehicleSubModel);
		return vehicleSubModel.getId();
	}

	@Override
	public int updateVehicleSubModel(VehicleSubModel vehicleSubModel) {
		return vehicleSubModelMapper.updateVehicleSubModel(vehicleSubModel);
	}

	@Override
	public int deleteById(Long vehicleSubModelId) {
		return vehicleSubModelMapper.deleteById(vehicleSubModelId);
	}

}
