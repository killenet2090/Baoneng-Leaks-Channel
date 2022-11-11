/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;
import gaea.vehicle.basic.service.dao.VehicleModelMapper;
import gaea.vehicle.basic.service.models.domain.VehicleModel;
import gaea.vehicle.basic.service.models.query.VehicleModelQuery;
import gaea.vehicle.basic.service.service.VehicleModelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleModelService")
public class DefaultVehicleModelService implements VehicleModelService {
    /**
     * 持久操作对象
     */
    private final VehicleModelMapper vehicleModelMapper;

    @Override
	public List<VehicleModel> queryPage(VehicleModelQuery query) {
		int count = vehicleModelMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleModelMapper.queryPage(query);
		}
	}

	@Override
	public VehicleModel queryById(Long vehicleModelId) {
		return vehicleModelMapper.queryById(vehicleModelId);
	}

	@Override
	public int insertVehicleModel(VehicleModel vehicleModel) {
		vehicleModelMapper.insertVehicleModel(vehicleModel);
		return vehicleModel.getId().intValue();
	}

	@Override
	public int updateVehicleModel(VehicleModel vehicleModel) {
		return vehicleModelMapper.updateVehicleModel(vehicleModel);
	}

	@Override
	public int deleteById(Long vehicleModelId) {
		return vehicleModelMapper.deleteById(vehicleModelId);
	}

}
