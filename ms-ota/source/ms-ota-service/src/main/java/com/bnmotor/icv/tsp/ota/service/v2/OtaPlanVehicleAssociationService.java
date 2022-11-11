package com.bnmotor.icv.tsp.ota.service.v2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.ota.common.cache.CacheRedisProvider;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.VehicleAssociateStateEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanObjListMapper;
import com.bnmotor.icv.tsp.ota.model.compose.GroupInfo;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleLabelAreaInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectLabelPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanVehicleCountPo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaVehicleAssociateQuery;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanExtraData;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.QueryAssociatedObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.QueryUpgradeObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeObjectListV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeTaskObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.VehicleCountQuery;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaPlanObjListVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanConditionVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.QueryAssociatedObjectV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.VehicleCountVo;
import com.bnmotor.icv.tsp.ota.service.IFotaMiscVehicleAssociateDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectLabelDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyDbService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyDateUtil;
import com.bnmotor.icv.tsp.ota.util.OtaJsonUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: OtaPlanVehicleAssociationService.java
 *             OtaPlanVehicleAssociationService
 * @Description: 升级任务与车辆关联 需要考虑的问题是一辆车可以关联多个标签吗？
 * @author E.YanLonG
 * @since 2020-12-1 14:22:45
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class OtaPlanVehicleAssociationService {

	@Autowired
	IFotaObjectDbService fotaObjectDbService;

	@Autowired
	IFotaStrategyDbService fotaStrategyDbService;

//	@Autowired
//	FilterVehicleService filterVehicleService;

	@Autowired
	IFotaObjectLabelDbService fotaObjectLabelService;

	@Autowired
	IFotaPlanObjListDbService fotaPlanObjListDbService;
	
	@Autowired
	IFotaPlanObjListDbService iFotaPlanObjListDbService;
	
	@Autowired
	CacheRedisProvider cacheRedisProvider;
	
	@Autowired
	IFotaPlanObjListV2Service iFotaPlanObjListV2Service;
	
	@Autowired
	IFotaPlanDbService fotaPlanDbService;
	
	@Autowired
	IFotaMiscVehicleAssociateDbService fotaMiscVehicleAssociateDbService;
	
	@Autowired
	IFotaPlanV2Service fotaPlanV2Service;
	
//	@Deprecated
//	public List<FotaObjectPo> selectVehicleWithStrategy(Long fotaStrategyId) {
//		FotaStrategyPo fotaStrategyPo = selectFotaStrategy(fotaStrategyId);
//		Long treeNodeId = fotaStrategyPo.getTreeNodeId();
//		// 选择该treeNodeId下的所有车辆
//
//		List<FotaObjectPo> fotaObjectDoList = fotaObjectDbService.listAllByTreeNodeId(treeNodeId);
//		return fotaObjectDoList;
//	}

	public List<FotaObjectPo> selectVehicleWithTreeNodeId(Long treeNodeId) {
		// 选择该treeNodeId下的所有车辆
		List<FotaObjectPo> fotaObjectDoList = fotaObjectDbService.listAllByTreeNodeId(treeNodeId);
		return fotaObjectDoList;
	}
	
	public List<FotaObjectPo> selectVehicleWithVins(List<String> vins) {
		if (CollectionUtils.isEmpty(vins)) {
			return Lists.newArrayList();
		}
		return fotaObjectDbService.listByVins(vins);
	}

//	/**
//	 * 选择车辆的入口，车辆总体以fota_object里的数据为准（label中的车辆比较少，或者没有的情况下）
//	 * 策略会携带一个treeNodeId，查询此treeNodeId下的车辆
//	 *
//	 * @param queryUpgradeObjectV2Req
//	 * @return
//	 */
//	@Deprecated
//	public FotaAssociateVehicleV2RespVo pageCondQuery0(QueryUpgradeObjectV2Req queryUpgradeObjectV2Req) {
//		Long treeNodeId = queryUpgradeObjectV2Req.getTreeNodeId();
////		FotaStrategyPo fotaStrategyPo = selectFotaStrategy(fotaStrategyId);
////		Long treeNodeId = fotaStrategyPo.getTreeNodeId();
//
//		List<String> vins = queryUpgradeObjectV2Req.getVins();
//		List<String> labels = queryUpgradeObjectV2Req.getLabels();
//		List<String> regions = queryUpgradeObjectV2Req.getRegions();
//		
//		List<Long> fotaObjectIdList = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(labels)) { // 需要查询labels的数据
//			List<FotaObjectLabelPo> fotaObjectLabelList = fotaObjectLabelService.selectByLables(labels);
//			Set<Long> labelVehicle = fotaObjectLabelList.stream().map(FotaObjectLabelPo::getObjectId).collect(Collectors.toSet());
//			if ( CollectionUtils.isNotEmpty(fotaObjectIdList)) { //
//				fotaObjectIdList = intersection(fotaObjectIdList, labelVehicle);
//			} else {
//				fotaObjectIdList.addAll(labelVehicle);
//			}
//		}
//
//		if (CollectionUtils.isNotEmpty(regions)) { // 需要查询区域的车辆
//			List<FotaObjectPo> fotaObjectRegionList = fotaObjectDbService.listByRegionCodeAndTreeNodeId(regions, treeNodeId);
//			Set<Long> regionVehicles = fotaObjectRegionList.stream().map(FotaObjectPo::getId).collect(Collectors.toSet());
//			if ( CollectionUtils.isNotEmpty(fotaObjectIdList)) { // 需要与标签取交集
//				fotaObjectIdList = intersection(fotaObjectIdList, regionVehicles);
//			} else {
//				fotaObjectIdList.addAll(regionVehicles);
//			}
//			
//		}
//		
//		if (CollectionUtils.isNotEmpty(regions) && CollectionUtils.isNotEmpty(labels) && CollectionUtils.isEmpty(fotaObjectIdList)) {
//			log.info("经过标签和条件过滤后没有最终的结果");
//			FotaAssociateVehicleV2RespVo fotaAssociateVehicleVo = FotaAssociateVehicleV2RespVo.of();
//			fotaAssociateVehicleVo.setCurrent(1);
//			fotaAssociateVehicleVo.setPages(0);
//			fotaAssociateVehicleVo.setTotal(0);
//			fotaAssociateVehicleVo.setTreeNodeId(treeNodeId);
//			fotaAssociateVehicleVo.setTotalVehicleCount(0L);
//			fotaAssociateVehicleVo.setValidVehicleCount(0L);
//			fotaAssociateVehicleVo.setInvalidVehicleCount(0L);
//			return fotaAssociateVehicleVo;
//		}
//		
//		if (CollectionUtils.isNotEmpty(vins)) { // 需要查询指定VIN码车辆
//			List<FotaObjectPo> fotaObjectRegionList = fotaObjectDbService.listByVins(vins);
//			fotaObjectIdList.addAll(fotaObjectRegionList.stream().map(FotaObjectPo::getId).collect(Collectors.toSet()));
//		}
//
//		List<FotaObjectPo> fotaObjectList = Lists.newArrayList();
//		Page<FotaObjectPo> fotaObjectPoPage = null;
//		List<FotaObjectPo> full = Lists.newArrayList(); // 根据条件过滤后的全量数据，主要查询可用车辆和不可用车辆
//		if (Objects.nonNull(queryUpgradeObjectV2Req)) {
//			Page page = new Page(queryUpgradeObjectV2Req.getCurrent(), queryUpgradeObjectV2Req.getPageSize());
//			full.addAll(fotaObjectDbService.selectVehicleWithTreeNodeId(treeNodeId, fotaObjectIdList)); // 查询全量数据，需要判断出哪些车辆可以关联
//			fotaObjectPoPage = fotaObjectDbService.selectVehicleWithTreeNodeIdPage(treeNodeId, fotaObjectIdList, page);
//			fotaObjectList.addAll(fotaObjectPoPage.getRecords());
//		} else {
//			fotaObjectList.addAll(selectVehicleWithTreeNodeId(treeNodeId));
//		}
//		
//		List<FotaObjectLabelPo> fotaObjectLabelPoList = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(fotaObjectList)) {
//			List<Long> objectIds4Label = fotaObjectList.stream().map(FotaObjectPo::getId).collect(Collectors.toList());
//			fotaObjectLabelPoList.addAll(fotaObjectLabelService.selectByObjectIds(objectIds4Label));
//		}
//
//
//		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList = Lists.newArrayList();
//
//		return assembly(treeNodeId, fotaObjectPoPage, fotaAssociateVehicleVoList, fotaObjectLabelPoList, full);
//	}
	
	public FotaAssociateVehicleV2RespVo pageCondQuery(QueryUpgradeObjectV2Req queryUpgradeObjectV2Req) {
		Long treeNodeId = queryUpgradeObjectV2Req.getTreeNodeId();
		Long otaPlanId = queryUpgradeObjectV2Req.getOtaPlanId();
//		List<String> vins = queryUpgradeObjectV2Req.getVins();
		List<String> labels0 = queryUpgradeObjectV2Req.getLabels();
		List<String> regions0 = queryUpgradeObjectV2Req.getRegions();
		Integer current = queryUpgradeObjectV2Req.getCurrent();
		Integer pagesize = queryUpgradeObjectV2Req.getPageSize();
		
		List<RegionInfo> regions = regions0.stream().map(it -> RegionInfo.of().setRegionCode(it)).collect(Collectors.toList());
		List<LabelInfo> labels = labels0.stream().map(it -> LabelInfo.of().setLabelKey(it)).collect(Collectors.toList());
		
		FotaVehicleAssociateQuery fotaVehicleAssociateQuery = FotaVehicleAssociateQuery.of();
		fotaVehicleAssociateQuery.setRegions(regions);
		fotaVehicleAssociateQuery.setLabels(labels);
		fotaVehicleAssociateQuery.setCurrent(current);
		fotaVehicleAssociateQuery.setPageSize(pagesize);
		fotaVehicleAssociateQuery.setTreeNodeId(treeNodeId);
		
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList = Lists.newArrayList();
		PageResult<List<VehicleLabelAreaInfo>> pageResult = fotaMiscVehicleAssociateDbService.page(fotaVehicleAssociateQuery);
//		List<FotaAssociateVehicleVo> fotaAssociateVehicleRecords = Lists.newArrayList();
		List<String> targetRecords = pageResult.getData().stream().map(VehicleLabelAreaInfo::getVin).collect(Collectors.toList());
		
		// 为了查询可关联车辆数量，需要全量查询累加出可关联车辆
		for (int i = 1; i <= fotaMiscVehicleAssociateDbService.totalPages(pageResult); i++) {
			fotaVehicleAssociateQuery.setCurrent(i);
			PageResult<List<VehicleLabelAreaInfo>> tempResult = fotaMiscVehicleAssociateDbService.page(fotaVehicleAssociateQuery);
			assembly(treeNodeId, fotaAssociateVehicleVoList, tempResult, labels0, regions0);
		}
		
		FotaAssociateVehicleV2RespVo fotaAssociateVehicleVo = FotaAssociateVehicleV2RespVo.of();
		if (CollectionUtils.isEmpty(fotaAssociateVehicleVoList)) {
			log.info("经过标签和条件过滤后没有最终的结果");
			fotaAssociateVehicleVo.setCurrent(1);
			fotaAssociateVehicleVo.setPages(0);
			fotaAssociateVehicleVo.setTotal(0);
			fotaAssociateVehicleVo.setTreeNodeId(treeNodeId);
			fotaAssociateVehicleVo.setTotalVehicleCount(0L);
			fotaAssociateVehicleVo.setValidVehicleCount(0L);
			fotaAssociateVehicleVo.setInvalidVehicleCount(0L);
			return fotaAssociateVehicleVo;
		}
		
		filteredAssociateVehicle(fotaAssociateVehicleVo, fotaAssociateVehicleVoList, otaPlanId);
		
		List<FotaAssociateVehicleVo> fotaAssociateVehicleRecords = fotaAssociateVehicleVoList.stream().filter(it -> targetRecords.contains(it.getVin())).collect(Collectors.toList());
		
		fotaAssociateVehicleVo.setRecords(fotaAssociateVehicleRecords);
		fotaAssociateVehicleVo.setCurrent(current);
		fotaAssociateVehicleVo.setTotal(pageResult.getTotalItem());
		fotaAssociateVehicleVo.setSize(pageResult.getPageSize());
		fotaAssociateVehicleVo.setTreeNodeId(treeNodeId);
		
		return fotaAssociateVehicleVo;
	}
	
	public void assembly(Long treeNodeId, List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList, PageResult<List<VehicleLabelAreaInfo>> pageresults, List<String> labels, List<String> regions) {
		
		List<VehicleLabelAreaInfo> results = pageresults.getData();
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList0 = results.stream()
				.map((object) -> {
					FotaAssociateVehicleVo vo = new FotaAssociateVehicleVo();
					vo.setCanAssociate(VehicleAssociateStateEnum.OTHER_REASON.getType());
					vo.setObjectId(object.getObjectId());
					vo.setVin(object.getVin());
					
					Optional.ofNullable(object.getRegion()).ifPresent(region -> {
						vo.setRegionName(region.getRegionName());
						vo.setRegionCode(region.getRegionCode());
					});
					
					vo.setLabels(object.getLabels());
					vo.getLabels().stream().filter(label -> labels.contains(label.getLabelKey())).findAny().ifPresent((info) -> {
						vo.setLabelKey(info.getLabelKey());
						vo.setLabelVal(info.getLabelVal());
					});
					
					return vo;
				}).collect(Collectors.toList());

		fotaAssociateVehicleVoList.addAll(fotaAssociateVehicleVoList0);
//
//		FotaAssociateVehicleV2RespVo fotaAssociateVehicleVo = FotaAssociateVehicleV2RespVo.of();
//		fotaAssociateVehicleVo.setRecords(fotaAssociateVehicleVoList);
//		fotaAssociateVehicleVo.setCurrent(pageresults.getCurrent());
//		fotaAssociateVehicleVo.setTotal(pageresults.getTotalItem());
//		fotaAssociateVehicleVo.setSize(pageresults.getPageSize());
//		fotaAssociateVehicleVo.setTreeNodeId(treeNodeId);
		
//		filteredAssociateVehicle(fotaAssociateVehicleVo, null, fotaAssociateVehicleVoList0);
//		return fotaAssociateVehicleVo;
	}
	
	@Deprecated
	public <T> List<T> intersection(Collection<T> t1, Collection<T> t2) {
		return t1.stream().filter(i -> t2.contains(i)).distinct().collect(Collectors.toList());
	}
	
	@Deprecated
	public FotaAssociateVehicleV2RespVo assembly(Long treeNodeId, Page<FotaObjectPo> fotaObjectPoPage, List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList, List<FotaObjectLabelPo> fotaObjectLabelPoList, List<FotaObjectPo> fotaObjectPoFull) {

		Map<Long, String> labelKeyMap = fotaObjectLabelPoList.stream().collect(Collectors.toMap(FotaObjectLabelPo::getObjectId, FotaObjectLabelPo::getLabelValue, (x, y) -> x));
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList0 = fotaObjectPoPage.getRecords().stream()
				.map((object) -> {
					FotaAssociateVehicleVo vo = new FotaAssociateVehicleVo();
					vo.setCanAssociate(VehicleAssociateStateEnum.OTHER_REASON.getType());
					vo.setObjectId(object.getId());
					vo.setVin(object.getObjectId()); // 车辆vin
					vo.setRegionName(object.getCurrentArea());
					String labelValue = labelKeyMap.get(object.getId());
					vo.setLabelVal(Objects.isNull(labelValue) ? "-" : labelValue);
					return vo;
				}).collect(Collectors.toList());

		fotaAssociateVehicleVoList.addAll(fotaAssociateVehicleVoList0);

		FotaAssociateVehicleV2RespVo fotaAssociateVehicleVo = FotaAssociateVehicleV2RespVo.of();
		fotaAssociateVehicleVo.setRecords(fotaAssociateVehicleVoList);
		fotaAssociateVehicleVo.setCurrent(fotaObjectPoPage.getCurrent());
		fotaAssociateVehicleVo.setTotal(fotaObjectPoPage.getTotal());
		fotaAssociateVehicleVo.setSize(fotaObjectPoPage.getSize());
		fotaAssociateVehicleVo.setTreeNodeId(treeNodeId);
		
//		filteredAssociateVehicle(fotaAssociateVehicleVo, fotaObjectPoFull, fotaAssociateVehicleVoList0);
		return fotaAssociateVehicleVo;
	}
	
	/**
	 * 需要判断 是否存在于当前的任务中
	 * @param fotaAssociateVehicleV2RespVo
	 * @param fotaAssociateVehicleVoList
	 */
	public Integer filteredAssociateVehicle(FotaAssociateVehicleV2RespVo fotaAssociateVehicleV2RespVo, List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList, Long otaPlanId) {
		List<FotaVehicleMetaObj> fotaVehicleMetaObjList = fotaVehicleMetaObjList(fotaAssociateVehicleVoList);
		List<ExistValidPlanObjVo> existValidPlanObjVoList = validate4AddFotaPlan(fotaVehicleMetaObjList);
		// 过滤掉处在当前任务中的车辆
		
		Optional.ofNullable(otaPlanId).ifPresent( that -> {
			existValidPlanObjVoList.removeIf(it -> that.equals(it.getOtaPlanId()));
		});
		
		Integer totalTargetCount = fotaAssociateVehicleVoList.size();
		Set<String> inOtherValidPlanVins = existValidPlanObjVoList.stream().map(ExistValidPlanObjVo::getVin).collect(Collectors.toSet());
		fotaAssociateVehicleVoList.stream().forEach(it -> {
			if (inOtherValidPlanVins.contains(it.getVin())) {
				it.setCanAssociate(VehicleAssociateStateEnum.IN_OTHER_PLANS.getType()); // 存在在其它有效任务中
			} else {
				it.setCanAssociate(VehicleAssociateStateEnum.CAN_ASSOCIATE.getType()); // 可以进行当次的关联操作
			}
		});
		
		Integer validCount = totalTargetCount - inOtherValidPlanVins.size();
		Integer invalidCount = inOtherValidPlanVins.size();
		fotaAssociateVehicleV2RespVo.setTotalVehicleCount(totalTargetCount.longValue());
		fotaAssociateVehicleV2RespVo.setValidVehicleCount(validCount.longValue());
		fotaAssociateVehicleV2RespVo.setInvalidVehicleCount(invalidCount.longValue());
		return validCount;
	}

//	@Deprecated
//	public <T> Page<T> paddingPage(Page<T> page, Long current, Long size, Long total) {
//		Page<T> targetPage = new Page<>();
//		targetPage.setRecords(Lists.newArrayList());
//		targetPage.setCurrent(current);
//		targetPage.setTotal(total);
//		targetPage.setSize(size);
//		return targetPage;
//	}

//	/**
//	 * 按vin列表查
//	 * 
//	 * @param vinList
//	 * @param label
//	 * @return
//	 */
//	public List<FotaObjectLabelPo> findVehicleLabelList(List<String> vinList, String label) {
//		QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper();
//		queryWrapper.in("object_id", vinList);
//		if (StringUtils.isNotBlank(label)) {
//			queryWrapper.eq("label_key", label);
//		}
//		List<FotaObjectLabelPo> fotaObjectLabelPos = fotaObjectLabelService.list(queryWrapper);
//		return fotaObjectLabelPos;
//	}

//	public FotaStrategyPo selectFotaStrategy(Long fotaStrategyId) {
//		FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaStrategyId);
//		return fotaStrategyPo;
//	}

	/**
	 * 查询入口
	 * 
	 * @param region
	 * @param label
	 * @param vin
	 * @param fotaStrategyId
	 * @return
	 */
//	public List<VehicleInfo> findAvailableAssociationVehicle(String region, String label, String vin,
//			Long fotaStrategyId) {
//		FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaStrategyId);
//
//		VehicleInfo vehicleInfo = VehicleInfo.of().setVin(vin).setLabel(label).setRegion(region);
//		List<FotaObjectVo> fotaObjectVoList = listPageByTreeNodeId(fotaStrategyPo);
//		List<VehicleInfo> vehicleInfoList = mapping(fotaObjectVoList);
//		List<String> vinList = vinList(vehicleInfoList);
//		List<FotaObjectLabelPo> fotaObjectLabelPoList = findVehicleLabelList(vinList, label);
//		paddingLabelFotaObjectLabelPo(fotaObjectLabelPoList, vehicleInfoList);
//
//		vehicleInfoList = filterVehicleService.filter(vehicleInfoList, vehicleInfo);
//		return vehicleInfoList;
//	}

//	/**
//	 * 表fota_object对应的vin存在，默认lable存在
//	 * 
//	 * @param fotaObjectLabelPoList
//	 * @param vehicleInfoList
//	 */
//	public void paddingLabelFotaObjectLabelPo(List<FotaObjectLabelPo> fotaObjectLabelPoList,
//			List<VehicleInfo> vehicleInfoList) {
//		Map<String, FotaObjectLabelPo> fotaObjectLabelPoMap = fotaObjectLabelPoList.stream()
//				.collect(Collectors.toMap(FotaObjectLabelPo::getVin, Function.identity(), (x, y) -> x));
//
//		vehicleInfoList.stream().forEach(it -> {
//			FotaObjectLabelPo fotaObjectLabelPo = fotaObjectLabelPoMap.get(it.getVin());
//			if (Objects.nonNull(fotaObjectLabelPo)) {
//				it.setLabel(fotaObjectLabelPo.getLabelValue());
//			}
//		});
//	}

//	public List<String> vinList(List<VehicleInfo> vehicleInfoList) {
//		return MyCollectionUtil.map2NewList(vehicleInfoList, f -> f.vin);
//	}
//
//	public List<VehicleInfo> mapping(List<FotaObjectVo> fotaObjectVoList) {
//
//		return fotaObjectVoList.stream().map(it -> {
//			VehicleInfo vehicleInfo = VehicleInfo.of();
//			vehicleInfo.setLabel(null);
//			vehicleInfo.setRegion(it.getCurrentArea());
//			vehicleInfo.setVin(it.getVin());
//			return vehicleInfo;
//		}).collect(Collectors.toList());
//
//	}

//	/**
//	 * 分页查询升级对象
//	 */
//	public List<FotaObjectVo> listPageByTreeNodeId(FotaObjectQuery fotaObjectQuery) {
//		IPage<FotaObjectPo> iPage = fotaObjectDbService.listPageByTreeNodeId(fotaObjectQuery.getTreeNodeId(),
//				fotaObjectQuery.getCurrent(), fotaObjectQuery.getPageSize());
//		List<FotaObjectPo> list = iPage.getRecords();
//
//		AtomicBoolean flag = new AtomicBoolean(false);
//		List<FotaObjectVo> fotaObjectVos = MyCollectionUtil.map2NewList(list, item -> {
//			FotaObjectVo fotaObjectVO = new FotaObjectVo();
//			fotaObjectVO.setVin(item.getObjectId());
//			fotaObjectVO.setCurrentArea(item.getCurrentArea());
//			fotaObjectVO.setSaleArea(item.getSaleArea());
//			fotaObjectVO.setId(Long.toString(item.getId()));
//			fotaObjectVO.setDisabled(item.getDisabled());
//			flag.set(true);
//			return fotaObjectVO;
//		});
//
//		return fotaObjectVos;
//	}

//	public List<FotaObjectVo> listPageByTreeNodeId(FotaStrategyPo fotaStrategyPo) {
//		List<FotaObjectPo> list = fotaObjectDbService.listAllByTreeNodeId(fotaStrategyPo.getTreeNodeId());
//		List<FotaObjectVo> fotaObjectVos = MyCollectionUtil.map2NewList(list, item -> {
//			FotaObjectVo fotaObjectVO = new FotaObjectVo();
//			fotaObjectVO.setVin(item.getObjectId());
//			fotaObjectVO.setCurrentArea(item.getCurrentArea());
//			fotaObjectVO.setSaleArea(item.getSaleArea());
//			fotaObjectVO.setId(Long.toString(item.getId()));
//			fotaObjectVO.setDisabled(item.getDisabled());
//			return fotaObjectVO;
//		});
//
//		return fotaObjectVos;
//	}

	/**
	 * 查询已经关联的车辆
	 * 
	 * @param planId
	 */
	public void alreadyAvailableAssociationVehicle(Long planId) {
		List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaPlanId(planId);
		// 如果已经选择了车辆信息
		if (MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) {
			List<FotaObjectPo> fotaObjectDos = fotaObjectDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListDos, item -> item.getOtaObjectId()));
			Map<Long, FotaObjectPo> fotaObjectDoMap = fotaObjectDos.stream().collect(Collectors.toMap(item -> item.getId(), Function.identity(), (x, y) -> x));
			// 补充currentArea和saleArea属性字段
			fotaPlanObjListDos.forEach(item -> {
				FotaObjectPo fotaObjectDo = fotaObjectDoMap.get(item.getOtaObjectId());
				if (Objects.nonNull(fotaObjectDo)) {
					item.setCurrentArea(fotaObjectDo.getCurrentArea());
					item.setSaleArea(fotaObjectDo.getSaleArea());
				}
			});
		}

		List<FotaPlanObjListVo> fotaPlanObjListVos = MyCollectionUtil.map2NewList(fotaPlanObjListDos, item -> {
			FotaPlanObjListVo fotaPlanObjListVo = new FotaPlanObjListVo();
			BeanUtils.copyProperties(item, fotaPlanObjListVo);
			fotaPlanObjListVo.setOtaPlanId(Long.toString(item.getOtaPlanId()));
			fotaPlanObjListVo.setOtaObjectId(Long.toString(item.getOtaObjectId()));
			fotaPlanObjListVo.setObjectId(fotaPlanObjListVo.getOtaObjectId());
			return fotaPlanObjListVo;
		});
	}
	
	public List<LabelInfo> mappingLabelInfo(List<String> labels) {
		if (CollectionUtils.isEmpty(labels)) {
			return Lists.newArrayList();
		}
		
		return labels.stream().map(label -> {
			LabelInfo info = LabelInfo.of().setLabelKey(label);
			return info;
		}).collect(Collectors.toList());
	}
	
	/**
	 * 关联车辆入口
	 * @param fotaPlanV2Req
	 * @param fotaPlanPo
	 * @param strategyId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AddFotaPlanResultVo associate(FotaPlanV2Req fotaPlanV2Req, FotaPlanPo fotaPlanPo, Long strategyId) {
		
		List<LabelInfo> labels0 = mappingLabelInfo(fotaPlanV2Req.getLabels());
		fotaPlanV2Req.setLabels0(labels0);
		Integer operateType = fotaPlanV2Req.getOperateType();
		if (operateType.intValue() == 2) { // 文件关联
			return associate1(fotaPlanV2Req, fotaPlanPo, strategyId);
		} else 
			// 条件关联
		return associate0(fotaPlanV2Req, fotaPlanPo, strategyId);
	}
	
	/**
	 * 条件关联
	 * @param fotaPlanV2Req
	 * @param fotaPlanPo
	 * @param strategyId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AddFotaPlanResultVo associate0(FotaPlanV2Req fotaPlanV2Req, FotaPlanPo fotaPlanPo, Long strategyId) {
		Long fotaStrategyId = fotaPlanV2Req.getFotaStrategyId();
		Long treeNodeId = fotaPlanV2Req.getTreeNodeId();
		List<LabelInfo> labels = fotaPlanV2Req.getLabels0();
		List<String> regions = fotaPlanV2Req.getRegions();
		List<String> vins = fotaPlanV2Req.getVins();
		if (Objects.isNull(vins)) {
			vins = Lists.newArrayList();
		}
		
		// 新增时判断条件是否已经选择，如果没有选择则返回无效车辆
		boolean includeNewVehicle = fotaPlanV2Req.getIsIncludeNewVehicle(); // 更新操作是否包含新增加车辆 1包含新增加的车辆
		Integer operateType = fotaPlanV2Req.getOperateType(); // 当前操作是条件过滤还是文件过滤 1 条件过滤
		Integer restful = fotaPlanV2Req.getRestfulType(); // 当前操作是新增操作还是更新操作 2更新操作
		if (restful == 1) { // 新增操作
			if (CollectionUtils.isEmpty(labels) && CollectionUtils.isEmpty(regions)) {
				log.info("新增任务 按条件关联，条件为空");
				return AddFotaPlanResultVo.builder().msg("按条件关联车辆所选条件为空").result(0).build();
			}
		}

		List<FotaObjectPo> fotaObjectPoList = selectVehicleWithTreeNodeId(treeNodeId);
		List<FotaVehicleMetaObj> fotaVehicleMetaObjList = fotaVehicleMetaObjList0(fotaObjectPoList);

		List<String> vins0 = fotaVehicleMetaObjList.stream().map(FotaVehicleMetaObj::getVin).collect(Collectors.toList());
		paddingLabels(vins0, fotaVehicleMetaObjList);
		paddingRegion(vins0, fotaVehicleMetaObjList);

		// 根据条件过滤车辆
		VehicleFilterMachine vehicleFilterMachine = new VehicleFilterMachine(fotaVehicleMetaObjList, regions, labels, vins);
		List<FotaVehicleMetaObj> fotaVehicleMetaObjFiltered = vehicleFilterMachine.filter();
		log.info("根据条件过滤后的数据|{}", fotaVehicleMetaObjFiltered.size());

		List<String> targets = fotaVehicleMetaObjFiltered.stream().map(FotaVehicleMetaObj::getVin).collect(Collectors.toList());

		// 保留条件过滤后的车辆
		List<FotaObjectPo> fotaObjectPoListFiltered = fotaObjectPoList.stream().filter(it -> targets.contains(it.getObjectId())).collect(Collectors.toList());

		Long fotaPlanId = fotaPlanPo.getId();
		
		AddFotaPlanResultVo addFotaPlanResultVo = AddFotaPlanResultVo.builder().build();
		List<ExistValidPlanObjVo> existsInPlan = validate4AddFotaPlan(fotaVehicleMetaObjFiltered);
		
		// 如果车辆已关联在当前任务，需要排除掉当前车辆
		List<ExistValidPlanObjVo> exculdeExistsInPlan = existsInPlan.stream().filter(it -> !fotaPlanId.equals(it.getOtaPlanId())).collect(Collectors.toList());
		
		if (MyCollectionUtil.isNotEmpty(exculdeExistsInPlan)) {
			log.warn("选择的车辆已经在有效的升级任务中，不能继续选择该车辆。existValidPlanObjs={}", exculdeExistsInPlan.toString());
			return AddFotaPlanResultVo.builder().msg("选择的车辆已经在有效的升级任务中，不能继续选择该车辆").result(0).existValidPlanObjVos(existsInPlan).build();
		}
		
		UpgradeObjectListV2Req UpgradeObjectListV2Req = convertUpgradeObjectListV2Req(fotaObjectPoListFiltered, fotaStrategyId, fotaPlanId, "sys");
		
		boolean condNoChanged = false; // 条件过滤没变
		FotaPlanExtraData fotaPlanExtraData = reverseExtraData(fotaPlanPo);
		if (Objects.nonNull(fotaPlanExtraData) && operateType.intValue() == 1 && restful.intValue() == 2) {
//			List<LabelInfo> labels0 = fotaPlanExtraData.getLabels0();
			List<String> labels0 = fotaPlanExtraData.getLabels();
			List<String> regions0 = fotaPlanExtraData.getRegions();
			
			List<String> labels1 = fotaPlanV2Req.getLabels();
			List<String> regions1 = fotaPlanV2Req.getRegions();
			
			// 如果和前一次是文件操作，则直接condNoChanged = true;
			if (fotaPlanExtraData.getOperateType() == 2) {
				condNoChanged = false;
			} else {
				condNoChanged = collectionSame(labels0, labels1) && collectionSame(regions0, regions1);
			}
		}
		
		// 只有"更新操作" 且增量为真&根据条件过滤且过滤条件此次没有改变的情况下才进行更新操作
		int k = 0;
		if (restful.intValue() == 2 && !includeNewVehicle && operateType.intValue() == 1 && condNoChanged) {
			// DO NOTHING!
		} else {
			iFotaPlanObjListV2Service.insertUpgradeTaskObjectList(UpgradeObjectListV2Req);
		}
		
		// 设置此次批次任务的升级车辆数量
		if (k == 0) {
			fotaPlanPo.setBatchSize(fotaVehicleMetaObjFiltered.size());
		} else {
			// 文件更新且内容为空时，不需要更新任务车辆总数
		}
		
		fotaPlanV2Service.wrapExtraData(fotaPlanPo, fotaPlanV2Req);
		fotaPlanDbService.updateById(fotaPlanPo);
		
		addFotaPlanResultVo.setOtaPlanId(String.valueOf(fotaPlanId));
		addFotaPlanResultVo.setResult(1);
		return addFotaPlanResultVo;
	}
	
	/**
	 * 文件关联
	 * @param fotaPlanV2Req
	 * @param fotaPlanPo
	 * @param strategyId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AddFotaPlanResultVo associate1(FotaPlanV2Req fotaPlanV2Req, FotaPlanPo fotaPlanPo, Long strategyId) {
		Long fotaStrategyId = fotaPlanV2Req.getFotaStrategyId();
		Long treeNodeId = fotaPlanV2Req.getTreeNodeId();
//		List<String> labels = fotaPlanV2Req.getLabels();
//		List<String> regions = fotaPlanV2Req.getRegions();
		List<String> vins = fotaPlanV2Req.getVins();
		if (Objects.isNull(vins)) {
			vins = Lists.newArrayList();
		}

		List<FotaObjectPo> fotaObjectPoList = selectVehicleWithVins(vins);
		List<FotaVehicleMetaObj> fotaVehicleMetaObjList = fotaVehicleMetaObjList0(fotaObjectPoList);

		List<String> vins0 = fotaVehicleMetaObjList.stream().map(FotaVehicleMetaObj::getVin).collect(Collectors.toList());
		paddingLabels(vins0, fotaVehicleMetaObjList);
		paddingRegion(vins0, fotaVehicleMetaObjList);

		// 根据条件过滤车辆
		VehicleFilterMachine vehicleFilterMachine = new VehicleFilterMachine(fotaVehicleMetaObjList, treeNodeId);
		List<FotaVehicleMetaObj> fotaVehicleMetaObjFiltered = vehicleFilterMachine.filter();
		log.info("根据条件过滤后的数据|{}", fotaVehicleMetaObjFiltered.size());

		List<String> targets = fotaVehicleMetaObjList.stream().map(FotaVehicleMetaObj::getVin).collect(Collectors.toList());

		// 保留条件过滤后的车辆
		List<FotaObjectPo> fotaObjectPoListFiltered = fotaObjectPoList.stream().filter(it -> targets.contains(it.getObjectId())).collect(Collectors.toList());

		Long fotaPlanId = fotaPlanPo.getId();
		
		AddFotaPlanResultVo addFotaPlanResultVo = AddFotaPlanResultVo.builder().build();
		List<ExistValidPlanObjVo> existsInPlan = validate4AddFotaPlan(fotaVehicleMetaObjFiltered);
		
		// 如果车辆已关联在当前任务，需要排除掉当前车辆
		List<ExistValidPlanObjVo> exculdeExistsInPlan = existsInPlan.stream().filter(it -> !fotaPlanId.equals(it.getOtaPlanId())).collect(Collectors.toList());
		
		if (MyCollectionUtil.isNotEmpty(exculdeExistsInPlan)) {
			log.warn("选择的车辆已经在有效的升级任务中，不能继续选择该车辆。existValidPlanObjs={}", exculdeExistsInPlan.toString());
			return AddFotaPlanResultVo.builder().msg("选择的车辆已经在有效的升级任务中，不能继续选择该车辆").result(0).existValidPlanObjVos(existsInPlan).build();
		}
		
		UpgradeObjectListV2Req UpgradeObjectListV2Req = convertUpgradeObjectListV2Req(fotaObjectPoListFiltered, fotaStrategyId, fotaPlanId, "sys");
		
//		boolean includeNewVehicle = fotaPlanV2Req.getIsIncludeNewVehicle(); // 更新操作是否包含新增加车辆 1包含新增加的车辆
		Integer operateType = fotaPlanV2Req.getOperateType(); // 当前操作是条件过滤还是文件过滤 1 条件过滤
		Integer restful = fotaPlanV2Req.getRestfulType(); // 当前操作是新增操作还是更新操作 2更新操作
		
		boolean condNoChanged = false; // 条件过滤没变
		FotaPlanExtraData fotaPlanExtraData = reverseExtraData(fotaPlanPo);
		if (Objects.nonNull(fotaPlanExtraData) && restful.intValue() == 2) { // 文件更新操作
			condNoChanged = CollectionUtils.isEmpty(vins);
		}
		
		// 只有"更新操作" 且增量为真&根据条件过滤且过滤条件此次没有改变的情况下才进行更新操作
		if (restful.intValue() == 2 && condNoChanged) {
			// DO NOTHING!
		} else if (restful.intValue() == 2 && CollectionUtils.isEmpty(vins)) { // 文件更新且未上传文件
			// DO NOTHING!
		} else {
			iFotaPlanObjListV2Service.insertUpgradeTaskObjectList(UpgradeObjectListV2Req);
			// 设置此次批次任务的升级车辆数量
			fotaPlanPo.setBatchSize(fotaVehicleMetaObjFiltered.size());
		}
		
		// 文件更新且内容为空时，不需要更新任务车辆总数
		fotaPlanV2Service.wrapExtraData(fotaPlanPo, fotaPlanV2Req);
		fotaPlanDbService.updateById(fotaPlanPo);
		
		addFotaPlanResultVo.setOtaPlanId(String.valueOf(fotaPlanId));
		addFotaPlanResultVo.setResult(1);
		return addFotaPlanResultVo;
	}
	
	/**
	 * 判断两个集合是否相等
	 * @param <T>
	 * @param list1
	 * @param list2
	 * @return
	 */
	public <T> boolean collectionSame(List<T> list1, List<T> list2) {
		if (list1.size() != list2.size()) {
			return false;
		}
		
		return list1.containsAll(list2) ;
	}
	
	public FotaPlanExtraData reverseExtraData(FotaPlanPo fotaPlanPo) {
    	String extra = fotaPlanPo.getExtra();
		FotaPlanExtraData fotaPlanExtraData = null;
		try {
			fotaPlanExtraData = StringUtils.isBlank(extra) ? null : OtaJsonUtil.toObject(extra, FotaPlanExtraData.class);
		} catch (Exception e) {
			log.error("反转FotaPlanExtraData异常|{}", e.getMessage(), e);
			// TODO: 正常情况下输出到JSON不应有异常，如果有异常需要抛出异常，临时屏蔽
		}
		return fotaPlanExtraData;
	}
	
	/**
	 * 判断车辆是否已经在其它的升级计划中且任务正在进行中
	 */
	public UpgradeObjectListV2Req convertUpgradeObjectListV2Req(List<FotaObjectPo> fotaObjectPoList, Long fotaStrategyId, Long fotaPlanId, String updateBy) {
		
		UpgradeObjectListV2Req upgradeObjectListV2Req = UpgradeObjectListV2Req.of();
		upgradeObjectListV2Req.setFotaPlanId(fotaPlanId).setFotaStrategyId(fotaStrategyId).setUpdateBy(updateBy);
		
		Function<FotaObjectPo, UpgradeTaskObjectV2Req> funct = (po) -> {
			UpgradeTaskObjectV2Req upgradeTaskObjectV2Req = new UpgradeTaskObjectV2Req();
			upgradeTaskObjectV2Req.setVin(po.getObjectId());
			upgradeTaskObjectV2Req.setObjectId(po.getId());
			return upgradeTaskObjectV2Req;
		};
		upgradeObjectListV2Req.setUpgradeTaskObjectReqList(fotaObjectPoList.stream().map(funct).collect(Collectors.toList()));
		return upgradeObjectListV2Req;
	}

	@Deprecated
	public List<FotaVehicleMetaObj> validate4AddFotaPlan0(List<FotaVehicleMetaObj> fotaVehicleMetaObjList) {
		// 添加逻辑保证车辆只能存在于一个有效的升级任务中
		List<FotaVehicleMetaObj> existValidPlanObjs = Lists.newArrayList();
		LocalDateTime now = LocalDateTime.now();
		for (FotaVehicleMetaObj upgradeTaskObjectReq : fotaVehicleMetaObjList) {
			// 添加逻辑保证车辆只能存在于一个有效的升级任务中
			Long otaObjectId = upgradeTaskObjectReq.getObjectId();
			List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaObjectId(upgradeTaskObjectReq.getObjectId());
			if (MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) {
				List<FotaPlanPo> fotaPlanDos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListDos, FotaPlanObjListPo::getOtaPlanId));
				if (MyCollectionUtil.isNotEmpty(fotaPlanDos)) {
					for (FotaPlanPo fotaPlanDo : fotaPlanDos) {
						boolean inRange = now.isBefore(MyDateUtil.uDateToLocalDateTime(fotaPlanDo.getPlanEndTime()));
						boolean isInvalid = PublishStateEnum.inValid(fotaPlanDo.getPublishState()); // 是否已失效（非失效状态都为有效）
						if (inRange && !isInvalid) {
							FotaObjectPo fotaObectDo = fotaObjectDbService.getById(otaObjectId);
							MyAssertUtil.notNull(fotaObectDo, "非法的车辆对象");
							existValidPlanObjs.add(upgradeTaskObjectReq);
						}
					}
				}
			}
		}
		return existValidPlanObjs;
	}
	
    public List<ExistValidPlanObjVo> validate4AddFotaPlan(List<FotaVehicleMetaObj> upgradeTaskObjectReqList) {
        //添加逻辑保证车辆只能存在于一个有效的升级任务中
        List<ExistValidPlanObjVo> existValidPlanObjs = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        for (FotaVehicleMetaObj upgradeTaskObjectReq : upgradeTaskObjectReqList) {

            //添加逻辑保证车辆只能存在于一个有效的升级任务中
            List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.listByOtaObjectId(upgradeTaskObjectReq.getObjectId());
            if (MyCollectionUtil.isNotEmpty(fotaPlanObjListPos)) {
                List<FotaPlanPo> fotaPlanPos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListPos, FotaPlanObjListPo::getOtaPlanId));
                if (MyCollectionUtil.isNotEmpty(fotaPlanPos)) {
                    for (FotaPlanPo fotaPlanPo : fotaPlanPos) {
                        boolean inRange = now.isBefore(MyDateUtil.uDateToLocalDateTime(fotaPlanPo.getPlanEndTime()));
                        boolean isInvalid = PublishStateEnum.inValid(fotaPlanPo.getPublishState()); // 是否已失效（非失效状态都为有效）
                        if (inRange && !isInvalid) {
                            ExistValidPlanObjVo existValidPlanObj = new ExistValidPlanObjVo();
                            existValidPlanObj.setOtaPlanId(fotaPlanPo.getId());
                            existValidPlanObj.setOtaObjectId(upgradeTaskObjectReq.getObjectId());
                            existValidPlanObj.setOtaPlanName(fotaPlanPo.getPlanName());

                            FotaObjectPo fotaObectPo = fotaObjectDbService.getById(upgradeTaskObjectReq.getObjectId());
                            MyAssertUtil.notNull(fotaObectPo, "非法的车辆对象");
                            existValidPlanObj.setVin(fotaObectPo.getObjectId());
                            existValidPlanObjs.add(existValidPlanObj);
                        }
                    }
                }
            }
        }
        return existValidPlanObjs;
    }

	public List<FotaVehicleMetaObj> fotaVehicleMetaObjList0(List<FotaObjectPo> fotaObjectPoList) {
		Function<FotaObjectPo, FotaVehicleMetaObj> mapper = (object) -> {
			FotaVehicleMetaObj fotaVehicleMetaObj = FotaVehicleMetaObj.of();
			String vin = object.getObjectId();
			fotaVehicleMetaObj.setVin(vin);
			fotaVehicleMetaObj.setObjectId(object.getId());
			fotaVehicleMetaObj.setRegionName(object.getCurrentArea());
			fotaVehicleMetaObj.setRegionCode(object.getCurrentCode());
			fotaVehicleMetaObj.setTreeNodeId(object.getTreeNodeId());
			return fotaVehicleMetaObj;
		};
		List<FotaVehicleMetaObj> fotaVehicleMetaObjList = fotaObjectPoList.stream().collect(Collectors.mapping(mapper, Collectors.toList()));
		return fotaVehicleMetaObjList;
	}
    
	public List<FotaVehicleMetaObj> fotaVehicleMetaObjList(List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList) {
		Function<FotaAssociateVehicleVo, FotaVehicleMetaObj> mapper = (object) -> {
			FotaVehicleMetaObj fotaVehicleMetaObj = FotaVehicleMetaObj.of();
			String vin = object.getVin();
			fotaVehicleMetaObj.setVin(vin);
			fotaVehicleMetaObj.setObjectId(object.getObjectId());
			LabelInfo labelInfo = LabelInfo.of().setLabelGroupId(object.getLabelGroupId()).setLabelGroupName(object.getLabelGroupName()).setLabelKey(object.getLabelKey()).setLabelVal(object.getLabelVal());
			fotaVehicleMetaObj.setLabels(Arrays.asList(labelInfo));
			fotaVehicleMetaObj.setRegionName(object.getRegionName());
			fotaVehicleMetaObj.setRegionCode(object.getRegionCode());
			return fotaVehicleMetaObj;
		};
		List<FotaVehicleMetaObj> fotaVehicleMetaObjList = fotaAssociateVehicleVoList.stream().collect(Collectors.mapping(mapper, Collectors.toList()));
		return fotaVehicleMetaObjList;
	}

//	@Deprecated
//	public void paddingLabels(List<String> vins, List<FotaVehicleMetaObj> fotaVehicleMetaObjList) {
//		List<FotaObjectLabelPo> selectLable = fotaObjectLabelService.selectLableByVin(vins);
//		Map<String, FotaObjectLabelPo> map = selectLable.stream().collect(Collectors.toMap(FotaObjectLabelPo::getVin, Function.identity(), (x, y) -> x));
//		Set<String> labelvins = map.keySet();
//		fotaVehicleMetaObjList.forEach(it -> {
//			if (labelvins.contains(it.getVin())) {
//				it.setLabelVal(map.get(it.getVin()).getLabelValue());
//				it.setLabelKey(map.get(it.getVin()).getLabelKey());
//			}
//		});
//	}
	
	/**
	 * 增加了LabelGroup处理
	 * @param vins
	 * @param fotaVehicleMetaObjList
	 */
	public void paddingLabels(List<String> vins, List<FotaVehicleMetaObj> fotaVehicleMetaObjList) {
		List<FotaObjectLabelPo> selectLable = fotaObjectLabelService.selectLableByVin(vins);
		Map<String, List<FotaObjectLabelPo>> map = selectLable.stream().collect(Collectors.groupingBy(FotaObjectLabelPo::getVin, Collectors.toList()));
		Set<String> labelvins = map.keySet();
		fotaVehicleMetaObjList.forEach(it -> {
			if (labelvins.contains(it.getVin())) {
				List<FotaObjectLabelPo> pos = map.get(it.getVin());
				List<LabelInfo> labels = pos.stream().map(po -> LabelInfo.of().setLabelGroupId(po.getLabelGroupId()).setLabelGroupName(po.getLabelGroupName()).setLabelKey(po.getLabelKey()).setLabelVal(po.getLabelValue())).collect(Collectors.toList());
				it.setLabels(labels);
			} else {
				it.setLabels(Lists.newArrayList());
			}
		});
	}

	public void paddingRegion(List<String> vins, List<FotaVehicleMetaObj> fotaVehicleMetaObjList) {
		fotaVehicleMetaObjList.forEach(object -> {
			object.setRegionName(object.getRegionName());
			object.setRegionCode(object.getRegionCode());
		});
	}

	public List<RegionInfo> listRegion(Long treeNodeId) {
		List<RegionInfo> list = fotaObjectDbService.listRegion(treeNodeId);
		// 如果不排除空的话，前端显示会异常，主要原因json的key是空的
		return list.stream().filter(it -> Objects.nonNull(it) && StringUtils.isNotBlank(it.getRegionCode())).collect(Collectors.toList());
	}
	
	/**
	 * 只查询到labelKey
	 * @param treeNodeId
	 * @return
	 */
	@Deprecated
	public List<LabelInfo> listLabel0(Long treeNodeId) {
		List<FotaObjectPo> list = fotaObjectDbService.listAllByTreeNodeId(treeNodeId);
		List<Long> objectIds = list.stream().map(FotaObjectPo::getId).collect(Collectors.toList());
		List<LabelInfo> labels = fotaObjectLabelService.listLabels(objectIds);
		return labels;
	}
	
	/**
	 * 查询到groupId
	 * @param treeNodeId
	 * @return
	 */
	public List<LabelInfo> listLabel(Long treeNodeId) {
//		List<FotaObjectPo> list = fotaObjectDbService.listAllByTreeNodeId(treeNodeId);
//		List<Long> objectIds = list.stream().map(FotaObjectPo::getId).collect(Collectors.toList());
		List<LabelInfo> labels = fotaObjectLabelService.listFullLabels();
		return labels;
	}
	
	public FotaPlanConditionVo listConditionByTreeNodeId(Long treeNodeId) {
		List<RegionInfo> regions = listRegion(treeNodeId);
		List<LabelInfo> labels = listLabel(treeNodeId);
		List<GroupInfo> groups = buildGroupInfo(labels);
		
		FotaPlanConditionVo fotaPlanConditionVo = FotaPlanConditionVo.of();
		fotaPlanConditionVo.setTreeNodeId(treeNodeId);
		fotaPlanConditionVo.setRegions(regions);
		fotaPlanConditionVo.setLabels(labels);
		fotaPlanConditionVo.setGroups(groups);
		return fotaPlanConditionVo;
	}
	
	public List<GroupInfo> buildGroupInfo(List<LabelInfo> labels) {
		List<GroupInfo> groups = Lists.newArrayList();
		
		Map<Long, List<LabelInfo>> list = labels.stream().collect(Collectors.groupingBy(LabelInfo::getLabelGroupId));
		
		list.forEach((key, val) -> {
			List<LabelInfo> v0 = Objects.isNull(val) ? Lists.newArrayList() : val;
			GroupInfo info = GroupInfo.of().setGroupId(key);
			Optional<LabelInfo> optional = v0.stream().filter(it -> it.getLabelGroupId().equals(key)).findAny();
			optional.ifPresent( it -> {
				info.setGroupName(optional.get().getLabelGroupName()).setLabels(v0);
			});
			groups.add(info);
		});
		return groups;
	}
	
	/**
	 * 查询当前升级任务下包含车辆的数量
	 */
	public List<VehicleCountVo> strategyPlanVehicleBatchSize0(List<VehicleCountQuery> vehicleCountQueryList) {
		List<Long> fotaPlanIdList = vehicleCountQueryList.stream().map(VehicleCountQuery::getFotaPlanId).collect(Collectors.toList());
		
		List<FotaPlanVehicleCountPo> fotaPlanVehicleCountPoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(vehicleCountQueryList)) {
			fotaPlanVehicleCountPoList.addAll(((FotaPlanObjListMapper) iFotaPlanObjListDbService.getBaseMapper()).queryVehicleConunt(fotaPlanIdList));
		}
		
		List<VehicleCountVo> vehicleCountVoList = fotaPlanVehicleCountPoList.stream().map(it -> VehicleCountVo.of().setBatchSize(it.getBatchSize()).setFotaPlanId(it.getOtaPlanId())).collect(Collectors.toList());
		return vehicleCountVoList;
	}
	
	/**
	 * 填充batchSize
	 * @param fotaPlanV2VoList
	 */
	public void strategyPlanVehicleBatchSize(List<FotaPlanV2Vo> fotaPlanV2VoList) {
		
		Function<FotaPlanV2Vo, VehicleCountQuery> function = (it) -> VehicleCountQuery.of().setFotaPlanId(it.getId()).setFotaStrategyId(it.getFotaStrategyId());
		List<VehicleCountQuery> vehicleCountQueryList = fotaPlanV2VoList.stream().map(function).collect(Collectors.toList());
		
		List<VehicleCountVo> vehicleCountVoList = strategyPlanVehicleBatchSize0(vehicleCountQueryList);
		Map<Long, VehicleCountVo> vehicleCountVoMap = vehicleCountVoList.stream().collect(Collectors.toMap(VehicleCountVo::getFotaPlanId, Function.identity(), (x, y) -> x));
		fotaPlanV2VoList.forEach(it -> {
			Long fotaPlanId = it.getId();
			VehicleCountVo vehicleCountVo = vehicleCountVoMap.get(fotaPlanId);
			it.setBatchSize(Objects.isNull(vehicleCountVo) ? 0 : vehicleCountVo.getBatchSize());
		});
	}
	
//	/**
//	 * 取消关联 暂不实现
//	 * @param otaPlanId
//	 */
//	@Deprecated
//	public void disassociate(Long otaPlanId) {
//		// TODO: 待定
//	}
	
	final String CACHE_PREFIX = "ms-ota::cache-";
	public void cacheVehicleUploadFile(String key, Object value) {
		String cacheKey = CACHE_PREFIX + key;
		try {
			cacheRedisProvider.setObject(cacheKey, value, 30, TimeUnit.MINUTES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> T cacheVehicleUploadFile(String key, Class<T> clazz) {
		String cacheKey = CACHE_PREFIX + key;
		try {
			return cacheRedisProvider.getObject(cacheKey, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String formatFileName(String prefix) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = sdf.format(new Date());
		return StringUtils.isBlank(prefix) ? "vehicle-file-" + format : prefix + format;
 	}
	
	/**
	 * 查询某升级任务下关联的车辆列表
	 * @param queryAssociatedObjectV2Req
	 */
	public QueryAssociatedObjectV2RespVo queryAssociateObjectList(QueryAssociatedObjectV2Req queryAssociatedObjectV2Req) {
		
		Integer pagesize = queryAssociatedObjectV2Req.getPageSize();
		Integer current = queryAssociatedObjectV2Req.getCurrent();
		Long otaPlanId = queryAssociatedObjectV2Req.getOtaPlanId();
		
		//分页查询结果集
        IPage<FotaPlanObjListPo> iPageFotaPlanObjList = fotaPlanObjListDbService.queryPage(otaPlanId, current, pagesize);
        List<Long> obejctIdList = iPageFotaPlanObjList.getRecords().stream().map(FotaPlanObjListPo::getOtaObjectId).collect(Collectors.toList());
        List<FotaObjectPo> fotaObjectPoList = CollectionUtils.isEmpty(obejctIdList) ? Lists.newArrayList() : fotaObjectDbService.listByIds(obejctIdList);
        
        QueryAssociatedObjectV2RespVo queryAssociatedObjectV2RespVo = QueryAssociatedObjectV2RespVo.of();
        queryAssociatedObjectV2RespVo.wrap(iPageFotaPlanObjList);
        queryAssociatedObjectV2RespVo.setRecords(MyCollectionUtil.newCollection(fotaObjectPoList, it -> {
        	FotaAssociateVehicleVo fotaPlanObjListV2Vo = new FotaAssociateVehicleVo();
            fotaPlanObjListV2Vo.setObjectId(it.getId());
            fotaPlanObjListV2Vo.setVin(it.getObjectId()); // VIN
            fotaPlanObjListV2Vo.setRegionName(it.getCurrentArea());
            fotaPlanObjListV2Vo.setCanAssociate(VehicleAssociateStateEnum.CAN_ASSOCIATE.getType());
            return fotaPlanObjListV2Vo;
        }));
        
        List<FotaPlanObjListPo> fotaPlanObjListPoList = iPageFotaPlanObjList.getRecords();
//		List<String> vins = fotaPlanObjListPoList.stream().map(FotaPlanObjListPo::getVin).collect(Collectors.toList());
//		List<FotaObjectLabelPo> fotaObjectLabelPoList = CollectionUtils.isEmpty(vins) ? Lists.newArrayList() : fotaObjectLabelService.selectLableByVin(vins);
//		Map<Long, String> labelKeyMap = fotaObjectLabelPoList.stream().collect(Collectors.toMap(FotaObjectLabelPo::getObjectId, FotaObjectLabelPo::getLabelValue, (x, y) -> x));
		
		List<Long> objectIds = fotaPlanObjListPoList.stream().map(FotaPlanObjListPo::getOtaObjectId).collect(Collectors.toList());
		List<LabelInfo> labelInfos = fotaMiscVehicleAssociateDbService.listLabels(objectIds);
		Map<Long, List<LabelInfo>> labelMap = labelInfos.stream().collect(Collectors.groupingBy(LabelInfo::getObjectId));
		
        
		List<FotaAssociateVehicleVo> records = queryAssociatedObjectV2RespVo.getRecords();
		records.forEach(it -> {
			Long objectId = it.getObjectId();
			List<LabelInfo> labels = labelMap.getOrDefault(objectId, Lists.newArrayList());
			it.setLabels(labels);
			
			labels.stream().findFirst().ifPresentOrElse((first) -> {
				it.setLabelVal(first.getLabelVal());
			}, ()-> {
				it.setLabelVal("-");
			});
		});
		return queryAssociatedObjectV2RespVo;
	}
}