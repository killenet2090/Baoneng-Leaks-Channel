package com.bnmotor.icv.tsp.ota.schedule;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.feign.AreaInfo;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionPage;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionState;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionStateQuery;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.service.feign.MsCommonDataFeignService;
import com.bnmotor.icv.tsp.ota.service.feign.MsOpsVehicleMonitorFeignService;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Api(value = "车辆地区同步", tags = { "车辆地区同步" })
@Component
@EnableScheduling
@Slf4j
public class VehicleRegionSynchronizeTasks {

	@Autowired
	MsCommonDataFeignService commonDataFeignService;
	
	@Autowired
	MsOpsVehicleMonitorFeignService msOpsVehicleMonitorFeignService;
	
	@Autowired
	IFotaObjectDbService fotaObjectDbService;
	
	@Autowired
	MsCommonDataFeignService msCommonDataFeignService;
	
	public Map<String, FotaObjectPo> selectFotaObjectByVins(List<String> vins) {
		List<FotaObjectPo> fotaObjectDoList = fotaObjectDbService.listByVins(vins);
		Map<String, FotaObjectPo> fotaObjectDoMap = fotaObjectDoList.stream().collect(Collectors.toMap(FotaObjectPo::getObjectId, Function.identity(), (x, y) -> x));
		return fotaObjectDoMap;
	}
	
	public Collection<FotaObjectPo> paddingRegionInfo(List<VehicleRegionState> vehicleRegionStateList) {
		Set<String> cityCodes = vehicleRegionStateList.stream().collect(Collectors.mapping(VehicleRegionState::getCityCode, Collectors.toSet()));
		
		List<AreaInfo> areaInfoList = Lists.newArrayList();
		RestResponse<List<AreaInfo>> restResponse = msCommonDataFeignService.batchlist(null, Lists.newArrayList(cityCodes));
		if ("00000".equalsIgnoreCase(restResponse.getRespCode())) {
			areaInfoList.addAll(restResponse.getRespData());
		}
		
		Map<String, String> areacodes = areaInfoList.stream().collect(Collectors.toMap(AreaInfo::getCode, AreaInfo::getName, (x, y) -> x));
		
		// 填充cityname
		vehicleRegionStateList.forEach(it -> it.setCityName( areacodes.get(it.getCityCode())));
		Map<String, VehicleRegionState> vehicleRegionStateMap = vehicleRegionStateList.stream().collect(Collectors.toMap(VehicleRegionState::getVin, Function.identity(), (x, y) -> x));
		
		List<String> vinList = vehicleRegionStateList.stream().map(VehicleRegionState::getVin).distinct().collect(Collectors.toList());
		
		Map<String, FotaObjectPo> fotaObjectDoMap = selectFotaObjectByVins(vinList);
		
		// 填充车辆中的当前地区信息
		fotaObjectDoMap.forEach((vin, obj) -> {
			obj.setCurrentArea(vehicleRegionStateMap.get(vin).getCityName());
			obj.setCurrentCode(vehicleRegionStateMap.get(vin).getCityCode());
		});
		
		// 更新车辆表中的当前地区信息
		return fotaObjectDoMap.values();
		
	}
	
	/**
	 * 最后一次同步时间戳暂时保存在redis中 首次同步数据量比较大，采取分页同步的方式查询数据
	 * 每次同步100条
	 */
	String updatetime = "2020-11-30 19:32:33";
	
//	@Scheduled(cron = "0 0/1 * * * ?")
	@Scheduled(fixedDelay = 90000, initialDelay = 5000)
	public void execute() {
		VehicleRegionStateQuery vehicleRegionStateQuery = prepare(1, 100, updatetime);
		List<VehicleRegionState> vehicleRegionStateList = callservice(vehicleRegionStateQuery);
		if (CollectionUtils.isEmpty(vehicleRegionStateList)) {
			log.info("此次查询没有增量更新");
			return;
		}
		
		log.info("查询长度|{}", vehicleRegionStateList.size());
		VehicleRegionState loop = vehicleRegionStateList.stream().max(Comparator.comparing(VehicleRegionState::getUpdateTime)).orElse(null);
		
		while (next(vehicleRegionStateList, vehicleRegionStateQuery)) {
			loop = execute(vehicleRegionStateList, vehicleRegionStateQuery, loop);
			vehicleRegionStateList = callservice(vehicleRegionStateQuery);
		}
		
		updatetime = Objects.isNull(loop.getUpdateTime()) ? updatetime : loop.getUpdateTime();
		log.info("查询结束updatetime|{}", updatetime);
	}
	
	public List<VehicleRegionState> callservice(VehicleRegionStateQuery vehicleRegionStateQuery) {
		RestResponse<VehicleRegionPage> restResponse = msOpsVehicleMonitorFeignService.batchlist(vehicleRegionStateQuery);
		String respcode = restResponse.getRespCode();
		if ("00000".equalsIgnoreCase(respcode)) {
			return restResponse.getRespData().getData();
		}
		return Lists.newArrayList();
	}
	
	public VehicleRegionState execute(List<VehicleRegionState> vehicleRegionStateList, VehicleRegionStateQuery vehicleRegionStateQuery, VehicleRegionState loop) {
		// 获取最大时间戳 记录当前的updatetime
		loop = vehicleRegionStateList.stream().max(Comparator.comparing(VehicleRegionState::getUpdateTime)).orElse(loop);
		
		// 填充地区信息
		Collection<FotaObjectPo> collection = paddingRegionInfo(vehicleRegionStateList);

		// 更新表记录
		fotaObjectDbService.updateBatchById(collection);
		
		return loop;
	}
	
	/**
	 * 判断是否继续查询
	 * @param list
	 * @param vehicleRegionStateQuery
	 * @return
	 */
	public boolean next(List<VehicleRegionState> list, VehicleRegionStateQuery vehicleRegionStateQuery ) {
	
		Integer current = vehicleRegionStateQuery.getCurrent();
		Integer pagesize = vehicleRegionStateQuery.getPageSize();
		Integer startrow = vehicleRegionStateQuery.getStartRow();
		if (CollectionUtils.isNotEmpty(list)) {
			vehicleRegionStateQuery.setCurrent(++current);
			startrow += pagesize;
			vehicleRegionStateQuery.setStartRow(startrow);
			return true;
		} 
		return false;
	}
	
	public VehicleRegionStateQuery prepare(Integer current, Integer pageSize, String updatetime) {
		VehicleRegionStateQuery vehicleRegionStateQuery = VehicleRegionStateQuery.of();
		
		current = current < 0 ? 1 : current;
//		int pageSize= 10;
		int startRow = (current - 1) * pageSize;
		vehicleRegionStateQuery.setCurrent(current);
		vehicleRegionStateQuery.setPageSize(pageSize);
		vehicleRegionStateQuery.setStartRow(startRow);
		updatetime(vehicleRegionStateQuery, updatetime);
		return vehicleRegionStateQuery;
	}
	
	public void updatetime(VehicleRegionStateQuery vehicleRegionStateQuery, String updatetime) {
		if (StringUtils.isNotBlank(updatetime)) {
			vehicleRegionStateQuery.setUpdateTime(updatetime);
			return ;
		}
		
		updatetime = "2020-01-01 00:00:00";
		vehicleRegionStateQuery.setUpdateTime(updatetime);
	}
}