/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.VehicleBrandMapper;
import gaea.vehicle.basic.service.models.domain.VehicleBrand;
import gaea.vehicle.basic.service.models.query.VehicleBrandQuery;
import gaea.vehicle.basic.service.service.VehicleBrandService;
import org.springframework.beans.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  车辆品牌表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleBrandService")
public class DefaultVehicleBrandService implements VehicleBrandService {
    /**
     * 车辆品牌表持久操作对象
     */
    private final VehicleBrandMapper vehicleBrandMapper;

    @Override
	public List<VehicleBrand> queryPage(VehicleBrandQuery query) {
		int count = vehicleBrandMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleBrandMapper.queryPage(query);
		}
	}

	@Override
	public List<VehicleBrand> queryNameAndId(VehicleBrandQuery query) {
		VehicleBrand vehicleBrand = new VehicleBrand();
		BeanUtils.copyProperties(query, vehicleBrand);
		return vehicleBrandMapper.queryNameAndId(vehicleBrand);
	}

	@Override
	public VehicleBrand queryById(Long vehicleBrandId) {
		return vehicleBrandMapper.queryById(vehicleBrandId);
	}

	@Override
	public int insertVehicleBrand(VehicleBrand vehicleBrand) {
		vehicleBrandMapper.insertVehicleBrand(vehicleBrand);
		return vehicleBrand.getId().intValue();
	}

	@Override
	public int updateVehicleBrand(VehicleBrand vehicleBrand) {
		return vehicleBrandMapper.updateVehicleBrand(vehicleBrand);
	}

	@Override
	public int deleteById(Long vehicleBrandId) {
		return vehicleBrandMapper.deleteById(vehicleBrandId);
	}

}
