/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.*;
import gaea.vehicle.basic.service.models.domain.*;
import gaea.vehicle.basic.service.models.query.VehicleQuery;
import gaea.vehicle.basic.service.models.request.VehicleReq;
import gaea.vehicle.basic.service.service.VehicleService;
import org.springframework.beans.BeanUtils;
import gaea.vehicle.basic.service.dao.*;
import gaea.vehicle.basic.service.models.domain.*;
import gaea.vehicle.basic.service.models.query.VehicleConditionQuery;
import gaea.vehicle.basic.service.models.vo.VehicleConditionPageVO;
import gaea.vehicle.basic.service.models.vo.VehicleConditionVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  车辆基础信息表默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("vehicleService")
public class DefaultVehicleService implements VehicleService {
    /**
     * 车辆基础信息表持久操作对象
     */
    @Resource
    private final VehicleMapper vehicleMapper;

	@Resource
	private final VehicleBrandMapper vehicleBrandMapper;

	@Resource
	private final VehicleSeriesMapper vehicleSeriesMapper;

	@Resource
	private final VehicleModelMapper vehicleModelMapper;

	@Resource
	private final VehicleSubModelMapper vehicleSubModelMapper;

	@Resource
	private final VehicleEcuBomMapper vehicleEcuBomMapper;




	@Override
	public List<Vehicle> queryPage(VehicleQuery query) {
		int count = vehicleMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleMapper.queryPage(query);
		}
	}



	@Override
	public Vehicle queryById(Long vehicleId) {
		return vehicleMapper.queryById(vehicleId);
	}

	@Override
	public int insertVehicle(Vehicle vehicle) {
		vehicleMapper.insertVehicle(vehicle);
		return vehicle.getId().intValue();
	}

	@Override
	public int updateVehicle(Vehicle vehicle) {
		return vehicleMapper.updateVehicle(vehicle);
	}

	@Override
	public int deleteById(Long vehicleId) {
		return vehicleMapper.deleteById(vehicleId);
	}

	@Override
	public List<Vehicle> queryList(VehicleReq vehicleReq) {
		Vehicle vehicle = new Vehicle();
		BeanUtils.copyProperties(vehicleReq, vehicle);
		return vehicleMapper.queryNameAndId(vehicle);
	}

	@Override
	public VehicleConditionVO queryConditions() {
		VehicleConditionVO vehicleConditionVO = new VehicleConditionVO();
		List<VehicleBrand> vehicleBrands= vehicleBrandMapper.queryNameAndId(null);
		List<VehicleSeries> vehicleSeries = vehicleSeriesMapper.queryNameAndId(null);
		List<VehicleModel> vehicleModels = vehicleModelMapper.queryNameAndId(null);
		List<VehicleSubModel> vehicleSubModels = vehicleSubModelMapper.queryNameAndId(null);
		vehicleConditionVO.setVehicleBrands(vehicleBrands);
		vehicleConditionVO.setVehicleSeries(vehicleSeries);
		vehicleConditionVO.setVehicleModels(vehicleModels);
		vehicleConditionVO.setVehicleSubModels(vehicleSubModels);
		return vehicleConditionVO;
	}

	@Override
	public List<VehicleConditionPageVO> queryByConditions(VehicleConditionQuery vehicleConditionQuery) {
		//this.dataMaker();
		int count = vehicleMapper.countConditionsPage(vehicleConditionQuery);
		vehicleConditionQuery.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return vehicleMapper.queryByConditions(vehicleConditionQuery);
		}
	}

	public void dataMaker(){
		VehicleBrand vehicleBrand = new VehicleBrand();
		VehicleSeries vehicleSeries = new VehicleSeries();
		VehicleModel vehicleModel = new VehicleModel();
		VehicleSubModel vehicleSubModel = new VehicleSubModel();
		Vehicle vehicle = new Vehicle();
		VehicleEcuBom vehicleEcuBom = new VehicleEcuBom();


		for(int b = 1 ; b <= 2; b++) {
			if(b==1){
				vehicleBrand.setBrandName("宝能汽车");
			}else {
				vehicleBrand.setBrandName("观致汽车");
			}
			vehicleBrand.setVehicleOemId(Long.valueOf(b));
			vehicleBrandMapper.insertVehicleBrand(vehicleBrand);
			for(int a = 1 ; a <= 2; a++) {
				if(1==b){
					if((a%2!=0)){
						vehicleSeries.setVehicleBrandId(vehicleBrand.getId());
						vehicleSeries.setSeriesName(1+a+"宝能3系");
					}else{
						vehicleSeries.setVehicleBrandId(vehicleBrand.getId());
						vehicleSeries.setSeriesName(1+a+"宝能5系");
					}
				}else{
					if((a%2!=0)){
						vehicleSeries.setVehicleBrandId(vehicleBrand.getId());
						vehicleSeries.setSeriesName(1+a+"观致7系");
					}else{
						vehicleSeries.setVehicleBrandId(vehicleBrand.getId());
						vehicleSeries.setSeriesName(1+a+"观致suv系");
					}
				}
				vehicleSeriesMapper.insertVehicleSeries(vehicleSeries);
				for(int c = 1 ; c <= 4; c++) {
					if(1==b){
						if((c%2!=0)){
							vehicleModel.setModelName("GX16"+c);
							vehicleModel.setModelYear("201"+(c+1));

						}else{
							vehicleModel.setModelName("GX20"+c);
							vehicleModel.setModelYear("201"+(c+1));
						}
					}else{
						if((c%2!=0)){
							vehicleModel.setModelName("观致320"+c);
							vehicleModel.setModelYear("201"+(c+1));

						}else{
							vehicleModel.setModelName("观致520"+c);
							vehicleModel.setModelYear("201"+(c+1));
						}
					}
					vehicleModel.setVehicleSeriesId(vehicleSeries.getId().intValue());
					vehicleModelMapper.insertVehicleModel(vehicleModel);

					for(int d = 1 ; d <= 4; d++) {
						String vin = System.currentTimeMillis()+"";
						if(1==b){
							if((d%2!=0)){
								vehicleSubModel.setSubModelName("运动V8_"+vin+"版");
							}else{
								vehicleSubModel.setSubModelName("尊贵W12_"+vin+"版");
							}
							vehicle.setSaleArea("110000");
							vehicle.setCurrentArea("430100");
						}else{
							if((d%2!=0)){
								vehicleSubModel.setSubModelName("新能源400km_"+vin+"版");
							}else{
								vehicleSubModel.setSubModelName("高性能续航600km_"+vin+"版");
							}
							vehicle.setSaleArea("220100");
							vehicle.setCurrentArea("330700");
						}
						vehicleSubModel.setVehicleModelId(Long.valueOf(vehicleModel.getId()));
						vehicleSubModelMapper.insertVehicleSubModel(vehicleSubModel);
						vehicle.setVehicleSubModelId(Long.valueOf(vehicleSubModel.getId()));
						vehicle.setVin(vin);
						vehicleMapper.insertVehicle(vehicle);
						vehicleEcuBom.setEcuName("ecu_name_"+d);
						vehicleEcuBom.setEcuId("ecu_id_"+d);
						vehicleEcuBom.setSupplier("'供应商'"+d);
						vehicleEcuBom.setSerialNumbe(vin);
						vehicleEcuBom.setFirmwareVersionNumber("'软件'"+d);
						vehicleEcuBom.setBootVersionNumber("boot"+d);
						vehicleEcuBom.setHardwareVersionNumber("'固件'"+d);
						vehicleEcuBom.setDiagnosticId("'诊断id'"+d);

						if(a%2!=0){
							vehicleEcuBom.setDifferentialSupport(0);
						}else {
							vehicleEcuBom.setDifferentialSupport(1);
						}
						vehicleEcuBom.setVin(vin);
						vehicleEcuBomMapper.insertVehicleEcuBom(vehicleEcuBom);
					}
				}
			}
		}
	}

}
