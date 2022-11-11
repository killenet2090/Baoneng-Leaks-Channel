/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.VehicleSeriesMapper;
import gaea.vehicle.basic.service.models.domain.VehicleSeries;
import gaea.vehicle.basic.service.service.VehicleSeriesService;
import gaea.vehicle.basic.service.models.query.VehicleSeriesQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  车辆系列表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleSeriesService")
public class DefaultVehicleSeriesService implements VehicleSeriesService {
    /**
     * 车辆系列表持久操作对象
     */
    private final VehicleSeriesMapper vehicleSeriesMapper;

    @Override
	public List<VehicleSeries> queryPage(VehicleSeriesQuery query) {
		int count = vehicleSeriesMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleSeriesMapper.queryPage(query);
		}
	}

	@Override
	public VehicleSeries queryById(Long vehicleSeriesId) {
		return vehicleSeriesMapper.queryById(vehicleSeriesId);
	}

	@Override
	public int insertVehicleSeries(VehicleSeries vehicleSeries) {
		vehicleSeriesMapper.insertVehicleSeries(vehicleSeries);
		return vehicleSeries.getId().intValue();
	}

	@Override
	public int updateVehicleSeries(VehicleSeries vehicleSeries) {
		return vehicleSeriesMapper.updateVehicleSeries(vehicleSeries);
	}

	@Override
	public int deleteById(Long vehicleSeriesId) {
		return vehicleSeriesMapper.deleteById(vehicleSeriesId);
	}

}
