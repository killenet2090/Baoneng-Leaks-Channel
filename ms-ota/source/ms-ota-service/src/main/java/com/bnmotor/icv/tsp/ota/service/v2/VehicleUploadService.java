package com.bnmotor.icv.tsp.ota.service.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.ota.common.enums.VehicleAssociateStateEnum;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectLabelPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.v2.ExcelDataVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleVo;
import com.bnmotor.icv.tsp.ota.service.IFotaMiscVehicleAssociateDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectLabelDbService;
import com.bnmotor.icv.tsp.ota.util.ExcelReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: VehicleUploadService.java
 * @Description: 处理文件上传vin关联任务
 * @author E.YanLonG
 * @since 2020-12-29 10:37:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class VehicleUploadService {

	@Autowired
	IFotaObjectDbService fotaObjectDbService;
	
	@Autowired
	IFotaObjectLabelDbService fotaObjectLabelService;
	
	@Autowired
	OtaPlanVehicleAssociationService otaPlanVehicleAssociationService;
	
	public List<FotaObjectPo> process(MultipartFile multipartFile, List<String> invalidVehicle) {
		// 更新操作且更新类型为文件更新
		if (Objects.isNull(multipartFile)) {
			return Lists.newArrayList();
		}
		try {
			String filename = multipartFile.getOriginalFilename();
			InputStream inputStream = multipartFile.getInputStream();
			String filetype = FilenameUtils.getExtension(filename);
			return resolveExcel(inputStream, filetype, invalidVehicle);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Lists.newArrayList();
	}
	
	@Autowired
	IFotaMiscVehicleAssociateDbService fotaMiscVehicleAssociateDbService;
	
	public Page<FotaAssociateVehicleVo> assemblyResult(List<FotaObjectPo> fotaObjectPoList, Long treeNodeId, List<String> invalidVehicle) {
		
		// 需要查询labels的数据
//		List<String> vins = fotaObjectPoList.stream().map(FotaObjectPo::getObjectId).collect(Collectors.toList());
//		List<FotaObjectLabelPo> fotaObjectLabelList = fotaObjectLabelService.selectLableByVin(vins);
		
		List<Long> objectIds = fotaObjectPoList.stream().map(FotaObjectPo::getId).collect(Collectors.toList());
		
		List<LabelInfo> labelInfos = fotaMiscVehicleAssociateDbService.listLabels(objectIds);
		Map<Long, List<LabelInfo>> labelMap = labelInfos.stream().collect(Collectors.groupingBy(LabelInfo::getObjectId));
		
//		List<Long> fotaObjectIdList = Lists.newArrayList();
//		fotaObjectIdList.addAll(fotaObjectLabelList.stream().map(FotaObjectLabelPo::getObjectId).collect(Collectors.toSet()));
		
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList = Lists.newArrayList();

		return assembly(fotaObjectPoList, fotaAssociateVehicleVoList, labelMap, treeNodeId, invalidVehicle);
	}
	
	public Page<FotaAssociateVehicleVo> assembly(List<FotaObjectPo> fotaObjectPoList, List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList,Map<Long, List<LabelInfo>> labelMap, Long treeNodeId, List<String> invalidVehicle) {

//		Map<String, String> labelKeyMap = fotaObjectLabelPoList.stream().collect(Collectors.toMap(FotaObjectLabelPo::getVin, FotaObjectLabelPo::getLabelValue, (x, y) -> x));
//		Map<String, String> labelKeyMap = mapNoNPE(fotaObjectLabelPoList);
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList0 = fotaObjectPoList.stream()
				.map((object) -> {
					FotaAssociateVehicleVo vo = new FotaAssociateVehicleVo();
					Integer flag = !treeNodeId.equals(object.getTreeNodeId()) ? VehicleAssociateStateEnum.NO_IN_TARGET_TREE.getType() : VehicleAssociateStateEnum.OTHER_REASON.getType(); // 4非叶子节点车辆 99需要继续判断
					vo.setCanAssociate(flag);
					vo.setObjectId(object.getId());
					vo.setVin(object.getObjectId()); // 车辆vin
					vo.setRegionName(object.getCurrentArea());
					
					Long objectId = object.getId();
					List<LabelInfo> labels = labelMap.getOrDefault(objectId, Lists.newArrayList());
					vo.setLabels(labels);
					labels.stream().findFirst().ifPresentOrElse((first) -> {
						vo.setLabelVal(first.getLabelVal());
					}, ()-> {
						vo.setLabelVal("-");
					});
					
					return vo;
				}).collect(Collectors.toList());

		fotaAssociateVehicleVoList.addAll(fotaAssociateVehicleVoList0);
		
		// 不存在的数据 1可以关联 2已在其它有效任务中 3无效车辆（vin不存在）4无效车辆（非指定设备下的车辆）
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList1 = invalidVehicle.stream().map(vin -> {
			FotaAssociateVehicleVo vo = new FotaAssociateVehicleVo();
			vo.setVin(vin);
			vo.setCanAssociate(VehicleAssociateStateEnum.NO_EXISTS_VEHICLE.getType()); // 无效数据 不存在的数据
			return vo;
		}).collect(Collectors.toList());
		
		fotaAssociateVehicleVoList.addAll(fotaAssociateVehicleVoList1);

		Page<FotaAssociateVehicleVo> fotaAssociateVehicleVoPage = new Page<>();
		fotaAssociateVehicleVoPage.setRecords(fotaAssociateVehicleVoList);
		fotaAssociateVehicleVoPage.setCurrent(1);
		fotaAssociateVehicleVoPage.setTotal(fotaAssociateVehicleVoList.size());
		fotaAssociateVehicleVoPage.setSize(fotaAssociateVehicleVoList.size());
		return fotaAssociateVehicleVoPage;
	}
	
	/**
	 * 解决转map val是空的问题
	 * @param fotaObjectLabelPoList
	 */
	public Map<String, String> mapNoNPE(List<FotaObjectLabelPo> fotaObjectLabelPoList) {
		Map<String, String> keyvalmap = Maps.newHashMap();
		Map<String, List<FotaObjectLabelPo>> map2list = fotaObjectLabelPoList.stream().collect(Collectors.groupingBy(FotaObjectLabelPo::getVin, Collectors.toList()));
		map2list.forEach((key, val) -> {
			FotaObjectLabelPo po = val.stream().findFirst().orElse(null);
			keyvalmap.put(key, Objects.nonNull(po) ? po.getLabelValue(): null);
		});
		return keyvalmap;
	}
	
	public List<ExcelDataVo> resolveExcel0(InputStream inputStream, String filetype, List<String> invalidVehicle) throws IOException {
		List<ExcelDataVo> excelDataList = ExcelReader.resolve(inputStream, filetype);

		// 过滤非vin数据
		List<ExcelDataVo> excelDataVo0 = excelDataList.stream().filter(it -> isVehicleVin(it.getVin())).collect(Collectors.toList());

		return excelDataVo0;
		
	}
	
	public List<FotaObjectPo> resolveExcel(InputStream inputStream, String filetype, List<String> invalidVehicle) throws IOException {
		List<ExcelDataVo> excelDataList = ExcelReader.resolve(inputStream, filetype);

		// 1过滤非vin数据 2过滤重复vin号
		List<ExcelDataVo> validdata = excelDataList.stream().filter(it -> isVehicleVin(it.getVin())).collect(Collectors.toList());
		
		excelDataList.removeAll(validdata);
		List<String> invaliddata = excelDataList.stream().map(ExcelDataVo::getVin).collect(Collectors.toList());
		log.info("无效数据|{}", invaliddata);
		
		List<String> vins = validdata.stream().map(ExcelDataVo::getVin).collect(Collectors.toList());
		List<FotaObjectPo> fotaObjectPoList = fotaObjectDbService.listByVins(vins);
		List<String> existVins = fotaObjectPoList.stream().map(FotaObjectPo::getObjectId).collect(Collectors.toList());
		
		// 前端页面要求取消去重
		Map<String, FotaObjectPo> vinMapping = fotaObjectPoList.stream().collect(Collectors.toMap(FotaObjectPo::getObjectId, Function.identity(), (x, y) -> x));
		List<FotaObjectPo> result = Lists.newArrayList();
		vins.stream().forEach(vin -> { // 显示重复数据
			FotaObjectPo po = vinMapping.get(vin);
			Optional.ofNullable(po).ifPresent(x -> {
				result.add(po);
			});
		});
		
		invalidVehicle.addAll(vins.stream().filter(vin -> !existVins.contains(vin)).collect(Collectors.toList()));
		return result;
	}

	/**
	 * 是否是vin号码
	 * 
	 * @param vin
	 * @return
	 */
	public static boolean isVehicleVin(String vin) {
		if (StringUtils.isBlank(vin)) {
			return false;
		}

		if (StringUtils.length(vin) != 17) {
			return false;
		}

		return true;

//		String pattern = "^[A-HJ-NPR-Z\\d]{17}$";
//		return Pattern.matches(pattern, vin);
	}

}