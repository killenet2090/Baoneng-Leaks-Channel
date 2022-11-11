/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeStrategyMapper;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeTaskConditionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareListReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareReq;
import com.bnmotor.icv.tsp.ota.model.resp.FotaFirmwarePkgVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.ExceptionUtil;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>
 *  该表用于定义一个OTA升级计划中需要升级哪几个软件
	包含：
     1. 依赖的软件清单-默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Service
@Slf4j
public class FotaPlanFirmwareListServiceImpl implements IFotaPlanFirmwareListService{
	@Autowired
	private IFotaFirmwareDbService fotaFirmwareDbService;

	@Autowired
	private IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

	@Autowired
	private IUpgradeTaskConditionDbService upgradeTaskConditionDbService;

	@Autowired
	private IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

	@Autowired
    private IUpgradeStrategyDbService upgradeStrategyDbService;

	@Autowired
	private IFotaFirmwareVersionPathDbService firmwareVersionPathDbService;
	
	@Autowired
	IFotaFirmwareService fotaFirmwareService;
	
	@Autowired
	IFotaPlanService fotaPlanService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean insertUpgradeFirmwareList(UpgradeFirmwareListReq upgradeFirmwareListReq) {
		return saveOrUpdateFotaPlanFirmwareList(upgradeFirmwareListReq);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateFotaPlanFirmwareList(UpgradeFirmwareListReq upgradeFirmwareListReq) {
		return saveOrUpdateFotaPlanFirmwareList(upgradeFirmwareListReq);
	}

	/**
	 * 保存或更新任务升级涉及到的ecu列表
	 * @param upgradeFirmwareListReq
	 * @return
	 */
	private Boolean saveOrUpdateFotaPlanFirmwareList(UpgradeFirmwareListReq upgradeFirmwareListReq) {
		List<UpgradeFirmwareReq> upgradeFirmwareList = upgradeFirmwareListReq.getUpgradeFirmwareList();
		if(MyCollectionUtil.isEmpty(upgradeFirmwareList)){
			log.warn("upgradeFirmwareList is empty,exit.");
			throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
		}
		
		// 关联固件前需要判断该固件是否已经完成做包服务
//		upgradeFirmwareList.forEach(it -> {
//			Long firmwareId = it.getFirmwareId();
//			Long firmwareVersionId = it.getFirmwareVersionId();
//			List<FotaFirmwarePkgVo> fotaFirmwarePkgVoList = fotaFirmwareService.listFirmwarePkgs(firmwareId, firmwareVersionId);
//			fotaFirmwarePkgVoList.stream().forEach(pkg -> {
//				Enums.BuildStatusEnum byType = Enums.BuildStatusEnum.getByType(pkg.getBuildPkgStatus());
//				if (Enums.BuildStatusEnum.TYPE_FINISH.getType() != byType.getType()) {
//					throw ExceptionUtil.buildAdamException(OTARespCodeEnum.BUILD_DATA_NOT_FINISH);
//				}
//			});
//		});
		
		int insert = 0;
		try {
			Long planId = upgradeFirmwareList.get(0).getTaskId();
			((FotaPlanFirmwareListMapper)fotaPlanFirmwareListDbService.getBaseMapper()).deleteByPlanIdPhysical(planId);

			LocalDateTime now = LocalDateTime.now();
			List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = upgradeFirmwareList.stream().map(item -> {
				FotaPlanFirmwareListPo fotaPlanFirmwareListPo = new FotaPlanFirmwareListPo();
				BeanUtils.copyProperties(item, fotaPlanFirmwareListPo);
				fotaPlanFirmwareListPo.setPlanId(item.getTaskId());
				FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(item.getFirmwareId());
				MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.DATA_NOT_FOUND);
				fotaPlanFirmwareListPo.setProjectId(fotaFirmwarePo.getProjectId());

				String userId = Objects.isNull(upgradeFirmwareListReq.getCreateBy())? CommonConstant.USER_ID_SYSTEM : upgradeFirmwareListReq.getCreateBy();
				CommonUtil.wrapBasePo(fotaPlanFirmwareListPo, userId, now, true);
				fotaPlanFirmwareListPo.setVersion(0);

				return fotaPlanFirmwareListPo;
			}).collect(Collectors.toList());
			insert = ((FotaPlanFirmwareListMapper) fotaPlanFirmwareListDbService.getBaseMapper()).saveAllInBatch(fotaPlanFirmwareListPos);
			if(insert < 1 ){
				log.error("任务固件信息更新写入失败. 任务ID : [ {} ]", planId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
			}
			
			// 判断firmwarePkg状态
            if(!isFirmwarePkgBuildedStatus(planId)){
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.BUILD_DATA_NOT_FINISH);
            }
            
			((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
			List<UpgradeTaskConditionPo> upgradeTaskConditionPos = upgradeFirmwareListReq.getUpgradeConditionList().stream().map(item -> {
				UpgradeTaskConditionPo upgradeTaskConditionPo = new UpgradeTaskConditionPo();
				BeanUtils.copyProperties(item, upgradeTaskConditionPo);
				upgradeTaskConditionPo.setOtaPlanId(item.getTaskId());
				CommonUtil.wrapBasePo(upgradeTaskConditionPo, fotaPlanFirmwareListPos.get(0).getCreateBy(), fotaPlanFirmwareListPos.get(0).getCreateTime(),true);
				return upgradeTaskConditionPo;
			}).collect(Collectors.toList());
			insert = ((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).saveAllInBatch(upgradeTaskConditionPos);
			if(insert < 1){
				log.error("任务前置信息更新写入失败. 任务ID : [ {} ]", planId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
			}
			((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
			List<UpgradeStrategyPo> upgradeStrategeDos = MyCollectionUtil.map2NewList(upgradeFirmwareList, item -> {
				UpgradeStrategyPo upgradeStrategyPo = new UpgradeStrategyPo();
				BeanUtils.copyProperties(item, upgradeStrategyPo);
				upgradeStrategyPo.setOtaPlanId(item.getTaskId());
				CommonUtil.wrapBasePo(upgradeStrategyPo, fotaPlanFirmwareListPos.get(0).getCreateBy(), fotaPlanFirmwareListPos.get(0).getCreateTime(),true);
				return upgradeStrategyPo;
			});
			insert = ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).saveAllInBatch(upgradeStrategeDos);
			if(insert < 1){
				log.error("任务策略信息更新写入失败. 任务ID : [ {} ]", planId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
			}
			return true;
		}catch (Throwable e){
			log.error("SYSTEM_DB_EXCEPTION : [ {} ]" , e.getMessage(), e);
			throw  e;
		}
	}

	/*@Override
	public List<FotaPlanFirmwareListPo> listByOtaPlanId(Long otaPlanId) {
		QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<FotaPlanFirmwareListPo>();
		queryWrapper.eq("plan_id", otaPlanId);
		return list(queryWrapper);
	}

	@Override
	public List<FotaPlanFirmwareListPo> getByPlanIdWithFirmwareIds(Long otaPlanId, List<Long> firmwareIds) {
		QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<FotaPlanFirmwareListPo>();
		queryWrapper.eq("plan_id", otaPlanId);
		queryWrapper.in("firmware_id", firmwareIds);
		return list(queryWrapper);
	}*/

	@Override
	public boolean isFirmwarePkgBuildedStatus(Long planId) {
		/*QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("plan_id", planId);
		List<FotaPlanFirmwareListPo> firmwareListDoslist = fotaPlanFirmwareListDbService.list(queryWrapper);*/

		List<FotaPlanFirmwareListPo> firmwareListDoslist = fotaPlanFirmwareListDbService.listByOtaPlanId(planId);
		if(!CollectionUtils.isEmpty(firmwareListDoslist)){
			Long firmwareVersionId = firmwareListDoslist.get(0).getFirmwareVersionId();
			/*QueryWrapper<FotaFirmwareVersionPathPo> queryPathWrapper = new QueryWrapper<FotaFirmwareVersionPathPo>();
			queryWrapper.eq("target_firmware_ver_id", firmwareVersionId);
			queryWrapper.isNotNull("firmware_pkg_id");
			List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = firmwareVersionPathDbService.list(queryPathWrapper);*/
			List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = firmwareVersionPathDbService.listByTargetVersionId(firmwareVersionId);



			List<Long> pkgIds = fotaFirmwareVersionPathPos.stream().map(item1 -> item1.getFirmwarePkgId()).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(pkgIds)){
				/*QueryWrapper<FotaFirmwarePkgPo> queryPkgWrapper = new QueryWrapper<>();
				queryPkgWrapper.in("id", pkgIds);
				queryPkgWrapper.ne("build_pkg_status", Enums.BuildStatusEnum.TYPE_FINISH.getType());
				List<FotaFirmwarePkgPo> list = fotaFirmwarePkgDbService.list(queryPkgWrapper);*/

				List<FotaFirmwarePkgPo> list = fotaFirmwarePkgDbService.listByIdsWithStatus(pkgIds, Enums.BuildStatusEnum.TYPE_FINISH.getType());
				if(!CollectionUtils.isEmpty(list)){
					log.info("升级未构建完成。planId={}", planId);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isFirmwarePkgBuildedStatusV2(Long planId) {
		/*QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<FotaPlanFirmwareListPo>();
		queryWrapper.eq("plan_id", planId);*/
		List<FotaPlanFirmwareListPo> firmwareListDoslist = fotaPlanFirmwareListDbService.listByOtaPlanId(planId);
		if(!CollectionUtils.isEmpty(firmwareListDoslist)){
			Long firmwareVersionId = firmwareListDoslist.get(0).getFirmwareVersionId();
			/*QueryWrapper<FotaFirmwareVersionPathPo> queryPathWrapper = new QueryWrapper<>();
			queryWrapper.eq("target_firmware_ver_id", firmwareVersionId);
			queryWrapper.isNotNull("firmware_pkg_id");*/
			List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathDos = firmwareVersionPathDbService.listByTargetVersionId(firmwareVersionId);
			List<Long> pkgIds = fotaFirmwareVersionPathDos.stream().map(item1 -> item1.getFirmwarePkgId()).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(pkgIds)){
				/*QueryWrapper<FotaFirmwarePkgPo> queryPkgWrapper = new QueryWrapper<>();
				queryPkgWrapper.in("id", pkgIds);
				queryPkgWrapper.ne("build_pkg_status", Enums.BuildStatusEnum.TYPE_FINISH.getType());*/
				List<FotaFirmwarePkgPo> list = fotaFirmwarePkgDbService.listByIdsWithStatus(pkgIds, Enums.BuildStatusEnum.TYPE_FINISH.getType());
				if(!CollectionUtils.isEmpty(list)){
					log.info("升级未构建完成。planId={}", planId);
					return false;
				}

				/*QueryWrapper<FotaFirmwarePkgPo> queryPkgWrapper1 = new QueryWrapper<>();*/
				List<FotaFirmwarePkgPo> list1 = fotaFirmwarePkgDbService.listByIdsWithEncryptStatus(pkgIds);
				if(!CollectionUtils.isEmpty(list1)){
					log.info("加密未构建完成。planId={}", planId);
					return false;
				}
			}
		}
		return true;
	}
}
