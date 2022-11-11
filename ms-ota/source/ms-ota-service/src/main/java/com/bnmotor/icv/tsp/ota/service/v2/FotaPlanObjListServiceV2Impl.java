package com.bnmotor.icv.tsp.ota.service.v2;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.OtaCacheTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.event.MyOtaEventKit;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanObjListMapper;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeStrategyMapper;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeTaskConditionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaUpgradeFirmwareAssembly;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeObjectListV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.UpgradeTaskObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaPlanObjListServiceV2Impl.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2020-12-16 15:52:27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
//@Deprecated
public class FotaPlanObjListServiceV2Impl implements IFotaPlanObjListV2Service {

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IUpgradeStrategyDbService upgradeStrategyDbService;

    @Autowired
    private IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;
    
    @Autowired
    IFotaStrategyService iFotaStrategyService;
    
    /*@Autowired
    IFotaFirmwareVersionService iFotaFirmwareVersionService;*/
    
    @Autowired
    IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

    @Autowired
	MyOtaEventKit myOtaEventKit;
    
    public List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos(Long fotaStrategyId) {
    	List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(fotaStrategyId);
    	return fotaStrategyFirmwareListPos;
    }

    @Override
    public FotaPlanObjListPo findOneByObjectId(Long objectId) {
        if (Objects.isNull(objectId)) {
            log.warn("参数异常.objectId={}", objectId);
            return null;
        }
        //按照创建时间降序排列，获取最新的升级任务对象
        List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaObjectId(objectId);
        return (MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) ? fotaPlanObjListDos.get(0) : null;
    }

    @Override
    public FotaPlanObjListPo findCurFotaPlanObjListByFotaObject(FotaObjectPo fotaObjectDo) {
        return findOneByObjectId(fotaObjectDo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddFotaPlanResultVo updateFotaPlanObjList(UpgradeObjectListV2Req upgradeObjectListReq) {
        return insertUpgradeTaskObjectList(upgradeObjectListReq);
    }

    /**
     * 前端不好做上一步和下一步的历史记录，调同一个接口，先删除，后覆盖
     *
     * @param upgradeObjectListReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddFotaPlanResultVo insertUpgradeTaskObjectList(UpgradeObjectListV2Req upgradeObjectListReq) {
        List<UpgradeTaskObjectV2Req> upgradeTaskObjectReqList = upgradeObjectListReq.getUpgradeTaskObjectReqList();
        if (MyCollectionUtil.isEmpty(upgradeTaskObjectReqList)) {
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
        }

        Long otaPlanId = upgradeObjectListReq.getFotaPlanId();
        Long fotaStrategyId = upgradeObjectListReq.getFotaStrategyId();
        FotaStrategyDto fotaStrategyDto = iFotaStrategyService.findOneFotaStrategy(fotaStrategyId);
        List<FotaStrategyFirmwareListPo>  fotaStrategyFirmwareListPoList = fotaStrategyFirmwareListPos(fotaStrategyId);
        //Map<Long, FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPoMap = fotaStrategyFirmwareListPoList.stream().collect(Collectors.toMap(FotaStrategyFirmwareListPo::getFirmwareId, Function.identity(), (x, y) -> x));
        
        List<FotaPlanObjListPo> fotaPlanObjListDos = Lists.newArrayList();
        FotaPlanPo fotaPlanDo = fotaPlanDbService.getById(otaPlanId);
        
        LocalDateTime now = LocalDateTime.now();
        upgradeTaskObjectReqList.forEach(upgradeTaskObjectV2 -> {
        	// MyAssertUtil.notNull(item.getObjectId(), OTARespCodeEnum.DATA_NOT_FOUND);
        	List<FotaPlanObjListPo> assemblyFotaPlanObjList = assemblyFotaPlanObjListDo(upgradeTaskObjectV2, fotaPlanDo, fotaStrategyDto, fotaStrategyFirmwareListPoList);
            fotaPlanObjListDos.addAll(assemblyFotaPlanObjList);
        });
        
        List<ExistValidPlanObjVo> existValidPlanObjVos = validate4AddFotaPlan(upgradeTaskObjectReqList);
        List<ExistValidPlanObjVo> exculdeOwnPlan = existValidPlanObjVos.stream().filter(it -> !otaPlanId.equals(it.getOtaPlanId())).collect(Collectors.toList());
        if (MyCollectionUtil.isNotEmpty(exculdeOwnPlan)) {
            log.warn("选择的车辆已经在有效的升级任务中，不能继续选择该车辆。existValidPlanObjs={}", exculdeOwnPlan.toString());
            return AddFotaPlanResultVo.builder().msg("选择的车辆已经在有效的升级任务中，不能继续选择该车辆").result(0).existValidPlanObjVos(exculdeOwnPlan).build();
        }

        // 删除升级计划中的所有车辆
        ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(otaPlanId);
        boolean saveBatch = fotaPlanObjListDbService.saveBatch(fotaPlanObjListDos);
        if (!saveBatch) {
            log.error("任务升级对象信息写入失败. 任务ID : [ {} ]", upgradeObjectListReq.getFotaPlanId());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }else{
			fotaPlanObjListDos.forEach(item ->{
				myOtaEventKit.triggerFotaCacheDelEvent(item.getVin(), OtaCacheTypeEnum.VIN_CACHE_INFO);
			});
		}
        
        // 填充FotaPlanFirmwareListPo
        FotaUpgradeFirmwareAssembly fotaUpgradeFirmwareAssembly = assemblyUpgradeFirmwareReq(fotaStrategyId, otaPlanId);
        saveOrUpdateFotaPlanFirmwareList(fotaUpgradeFirmwareAssembly);

        // 先删除可能已经存在的任务明细
        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListDos = fotaPlanFirmwareListDbService.listByOtaPlanId(otaPlanId);
        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaPlanFirmwareListDos), "任务ECU清单不存在，请检查");

        //TODO 此处代码逻辑有待考虑
        List<UpgradeStrategyPo> upgradeStrategyDos = upgradeStrategyDbService.listByOtaPlanId(otaPlanId);
        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(upgradeStrategyDos), "任务策略信息不存在，请检查");
        
        Map<Long, UpgradeStrategyPo> upgradeStrategyDoMap = upgradeStrategyDos.stream().collect(Collectors.toMap(UpgradeStrategyPo::getFirmwareId, Function.identity()));
        for (FotaPlanFirmwareListPo fotaPlanFirmwareListDo : fotaPlanFirmwareListDos) {
            if(Objects.nonNull(upgradeStrategyDoMap.get(fotaPlanFirmwareListDo.getId()))) {
                fotaPlanFirmwareListDo.setRollbackMode(upgradeStrategyDoMap.get(fotaPlanFirmwareListDo.getId()).getRollbackMode());
            }
        }
        fotaPlanFirmwareListDbService.saveOrUpdateBatch(fotaPlanFirmwareListDos);

        //fotaPlanDo.setPublishState(Enums.PlanStatusTypeEnum.AUDITED.getType());
        fotaPlanDo.setId(otaPlanId);
        fotaPlanDo.setUpdateTime(now);
        fotaPlanDo.setUpdateBy(upgradeObjectListReq.getUpdateBy());
        fotaPlanDbService.updateById(fotaPlanDo);
        return AddFotaPlanResultVo.builder().result(1).otaPlanId(Long.toString(otaPlanId)).build();
    }
    
    public List<FotaPlanObjListPo> assemblyFotaPlanObjListDo(UpgradeTaskObjectV2Req upgradeTaskObjectV2Req, FotaPlanPo fotaPlanDo, FotaStrategyDto fotaStrategyDto, List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPoList) {
    	Long fotaPlanId = fotaPlanDo.getId();
    	Long objectId = upgradeTaskObjectV2Req.getObjectId();
    	String vin = upgradeTaskObjectV2Req.getVin();
    	
    	List<FotaPlanObjListPo> fotaPlanObjListDoList = Lists.newArrayList();
    	FotaPlanObjListPo objListDo = new FotaPlanObjListPo();
    	objListDo.setId(IdWorker.getId());
    	objListDo.setOtaPlanId(fotaPlanId);
    	objListDo.setOtaObjectId(objectId);
    	objListDo.setVin(vin);
    	objListDo.setCurrentArea(null);
    	objListDo.setSaleArea(null);
    	objListDo.setVersion(0);
    	objListDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
    	objListDo.setResult(0);
    		
    	objListDo.setTargetVersion(fotaStrategyDto.getEntireVersionNo()); // 整车版本
    		
    	FotaObjectPo exitFotaObjectDo = fotaObjectDbService.getById(objectId);
    	objListDo.setSourceVersion(exitFotaObjectDo.getCurrentVersion()); // 临时可以不填
    	objListDo.setCurrentVersion(exitFotaObjectDo.getCurrentVersion()); // 临时可以不填
    	objListDo.setStatus(Enums.TaskObjStatusTypeEnum.CREATED.getType()); // 默认0状态（新创建）
    	objListDo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_UNDEFALT.getType()); // 初始化
    	fotaPlanObjListDoList.add(objListDo);
    		
    	String userId = CommonConstant.USER_ID_SYSTEM;
        CommonUtil.wrapBasePo(objListDo, userId, LocalDateTime.now(), true);
    	return fotaPlanObjListDoList;
    }
    
    
    /**
     * 设置列表任务固件升级列表
     * @param upgradeTaskObjectV2Req
     * @param fotaPlanDo
     * @param fotaStrategyDto
     * @param fotaStrategyFirmwareListPoList
     * @return
     */
    public List<FotaPlanFirmwareListPo> assemblyFotaPlanFirmwareListDo(UpgradeTaskObjectV2Req upgradeTaskObjectV2Req, FotaPlanPo fotaPlanDo, FotaStrategyDto fotaStrategyDto, List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPoList) {
    	Long fotaPlanId = fotaPlanDo.getId();
    	Long objectId = upgradeTaskObjectV2Req.getObjectId();
    	String vin = upgradeTaskObjectV2Req.getVin();
    	FotaObjectPo fotaObjectPo = fotaObjectDbService.getById(objectId);
    	String projectId = fotaObjectPo.getProjectId();
    	int upgradeSeq = 0;
    	List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPoList = Lists.newArrayList();
    	
    	// 升级排序 总体顺序(先组顺序再组内顺序)
    	fotaStrategyFirmwareListPoList = fotaStrategyFirmwareListPoList.stream().sorted(Comparator.comparing(FotaStrategyFirmwareListPo::getGroupSeq).thenComparing(FotaStrategyFirmwareListPo::getOrderNum)).collect(Collectors.toList());
    	for (FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo: fotaStrategyFirmwareListPoList) {
    		Long firmwareId = fotaStrategyFirmwareListPo.getFirmwareId();
    		FotaPlanFirmwareListPo fotaPlanFirmwareListPo = new FotaPlanFirmwareListPo();
    		fotaPlanFirmwareListPo.setId(IdWorker.getId());
    		fotaPlanFirmwareListPo.setProjectId(projectId);
    		fotaPlanFirmwareListPo.setFirmwareId(firmwareId);
    		fotaPlanFirmwareListPo.setPlanId(fotaPlanId);
    		fotaPlanFirmwareListPo.setVersion(0);
    		fotaPlanFirmwareListPo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
    		Long targetVersionId = fotaStrategyFirmwareListPo.getTargetVersionId();
    		fotaPlanFirmwareListPo.setFirmwareVersionId(targetVersionId);
    		fotaPlanFirmwareListPo.setUpgradeSeq(++ upgradeSeq);
    		fotaPlanFirmwareListPo.setGroupSeq(fotaStrategyFirmwareListPo.getGroupSeq());
    		fotaPlanFirmwareListPo.setRollbackMode(fotaStrategyDto.getRollbackMode()); // 回滚模式； 1 - 激进策略， 0 - 保守策略 默认 0保守策略
//    		FotaFirmwareVersionPo fotaFirmwareVersionDo = iFotaFirmwareVersionService.getById(targetVersionId);
    		fotaPlanFirmwareListPoList.add(fotaPlanFirmwareListPo);
    		String userId = CommonConstant.USER_ID_SYSTEM;
            CommonUtil.wrapBasePo(fotaPlanFirmwareListPo, userId, LocalDateTime.now(), true);
    	}
    	return fotaPlanFirmwareListPoList;
    }

    @Override
    public List<ExistValidPlanObjVo> validate4AddFotaPlan(List<UpgradeTaskObjectV2Req> upgradeTaskObjectReqList) {
        // 添加逻辑保证车辆只能存在于一个有效的升级任务中
        List<ExistValidPlanObjVo> existValidPlanObjs = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        for (UpgradeTaskObjectV2Req upgradeTaskObjectReq : upgradeTaskObjectReqList) {

            // 添加逻辑保证车辆只能存在于一个有效的升级任务中
        	Long otaObjectId = upgradeTaskObjectReq.getObjectId();
            List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaObjectId(upgradeTaskObjectReq.getObjectId());

            if (MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) {
                List<FotaPlanPo> fotaPlanDos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListDos, FotaPlanObjListPo::getOtaPlanId));
                if (MyCollectionUtil.isNotEmpty(fotaPlanDos)) {
                    for (FotaPlanPo fotaPlanDo : fotaPlanDos) {
                        boolean inRange = now.isBefore(MyDateUtil.uDateToLocalDateTime(fotaPlanDo.getPlanEndTime()));
                        boolean isInvalid = PublishStateEnum.inValid(fotaPlanDo.getPublishState());
                        if (inRange && !isInvalid) {
                            ExistValidPlanObjVo existValidPlanObj = new ExistValidPlanObjVo();
                            existValidPlanObj.setOtaPlanId(fotaPlanDo.getId());
                            existValidPlanObj.setOtaObjectId(otaObjectId);
                            existValidPlanObj.setOtaPlanName(fotaPlanDo.getPlanName());
                            //

                            FotaObjectPo fotaObectDo = fotaObjectDbService.getById(otaObjectId);
                            MyAssertUtil.notNull(fotaObectDo, "非法的车辆对象");
                            existValidPlanObj.setVin(fotaObectDo.getObjectId());
                            existValidPlanObjs.add(existValidPlanObj);
                        }
                    }
                }
            }
        }
        return existValidPlanObjs;
    }

    @Override
    public ExistValidPlanObjVo checkValidate4AddFotaPlan(Long otaObjectId) {
        //添加逻辑保证车辆只能存在于一个有效的升级任务中
        List<FotaPlanObjListPo> fotaPlanObjListDos = fotaPlanObjListDbService.listByOtaObjectId(otaObjectId);

        ExistValidPlanObjVo existValidPlanObj = null;
        if (MyCollectionUtil.isNotEmpty(fotaPlanObjListDos)) {
            List<FotaPlanPo> fotaPlanDos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListDos, FotaPlanObjListPo::getOtaPlanId));
            if (MyCollectionUtil.isNotEmpty(fotaPlanDos)) {
                LocalDateTime now = LocalDateTime.now();
                for (FotaPlanPo fotaPlanDo : fotaPlanDos) {
                    /**
                     * 判断是否可以添加：
                     * 1、当前日期已经在任务结束日期之后
                     */
                    boolean flag = now.isAfter(MyDateUtil.uDateToLocalDateTime(fotaPlanDo.getPlanEndTime()));
                    flag = flag || PublishStateEnum.inValid(fotaPlanDo.getPublishState());
                    if (!flag) {
                        existValidPlanObj = new ExistValidPlanObjVo();
                        existValidPlanObj.setOtaPlanId(fotaPlanDo.getId());
                        existValidPlanObj.setOtaObjectId(otaObjectId);
                        existValidPlanObj.setOtaPlanName(fotaPlanDo.getPlanName());
                        FotaObjectPo fotaObectDo = fotaObjectDbService.getById(otaObjectId);
                        MyAssertUtil.notNull(fotaObectDo, "非法的车辆对象");
                        existValidPlanObj.setVin(fotaObectDo.getObjectId());
                    }
                }
            }
        }
        return existValidPlanObj;
    }
    
    @Autowired
    IUpgradeTaskConditionDbService upgradeTaskConditionDbService;
	
	@Autowired
	IFotaFirmwareDbService fotaFirmwareDbService;

	@Autowired
	IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

	//	@Autowired
//	 IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

//	@Autowired
//     IUpgradeStrategyDbService upgradeStrategyDbService;

	@Autowired
	 IFotaFirmwareVersionPathDbService firmwareVersionPathDbService;
	
	@Autowired
	IFotaStrategyDbService fotaStrategyDbService;
	
	@Autowired
	IFotaStrategyPreConditionDbService fotaStrategyPreConditionDbService;
	
	public FotaUpgradeFirmwareAssembly assemblyUpgradeFirmwareReq(Long fotaStrategyId, Long fotaPlanId) {
		
		FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(fotaPlanId);
		FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaStrategyId);
		
		List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListPos(fotaStrategyId);
		List<FotaStrategyPreConditionPo> fotaStrategyPreConditionPos = fotaStrategyPreConditionDbService.listByOtaStrategyId(fotaStrategyId);
		
		FotaUpgradeFirmwareAssembly fotaUpgradeFirmwareAssembly = FotaUpgradeFirmwareAssembly.of() //
				.setFotaPlanPo(fotaPlanPo) //
				.setFotaStrategyPo(fotaStrategyPo) //
				.setFotaStrategyFirmwareListPos(fotaStrategyFirmwareListPos) //
				.setFotaStrategyPreConditionPos(fotaStrategyPreConditionPos); //
//		saveOrUpdateFotaPlanFirmwareList(fotaStrategyFirmwareListPoList, fotaStrategyPreConditionPoList, fotaStrategyPo, fotaPlanPo);
		return fotaUpgradeFirmwareAssembly;
	}
	
	@ Transactional(rollbackFor = Exception.class)
	public Boolean saveOrUpdateFotaPlanFirmwareList(FotaUpgradeFirmwareAssembly fotaUpgradeFirmwareAssembly) {
		List<FotaStrategyFirmwareListPo> upgradeFirmwareList = fotaUpgradeFirmwareAssembly.getFotaStrategyFirmwareListPos();
		List<FotaStrategyPreConditionPo> taskUpgradeConditionReqList = fotaUpgradeFirmwareAssembly.getFotaStrategyPreConditionPos();
		FotaStrategyPo fotaStrategyPo = fotaUpgradeFirmwareAssembly.getFotaStrategyPo();
		FotaPlanPo fotaPlanPo = fotaUpgradeFirmwareAssembly.getFotaPlanPo();
		if(MyCollectionUtil.isEmpty(upgradeFirmwareList)){
			log.warn("upgradeFirmwareList is empty,exit.");
			throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
		}
		
		Long fotaPlanId = fotaPlanPo.getId();
		int insert = 0;
		try {
			((FotaPlanFirmwareListMapper) fotaPlanFirmwareListDbService.getBaseMapper()).deleteByPlanIdPhysical(fotaPlanId);

			LocalDateTime now = LocalDateTime.now();
			List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = upgradeFirmwareList.stream().map(item -> {
				FotaPlanFirmwareListPo fotaPlanFirmwareListPo = new FotaPlanFirmwareListPo();
//				BeanUtils.copyProperties(item, fotaPlanFirmwareListPo);
				fotaPlanFirmwareListPo.setPlanId(fotaPlanId);
				fotaPlanFirmwareListPo.setFirmwareId(item.getFirmwareId());
				fotaPlanFirmwareListPo.setGroupSeq(item.getDbGroupSeq());
				fotaPlanFirmwareListPo.setUpgradeSeq(item.getOrderNum());
				fotaPlanFirmwareListPo.setFirmwareVersionId(item.getTargetVersionId());
				fotaPlanFirmwareListPo.setProjectId(item.getProjectId());
				fotaPlanFirmwareListPo.setCreateTime(LocalDateTime.now());
				fotaPlanFirmwareListPo.setRollbackMode(Objects.isNull(fotaStrategyPo.getRollbackMode()) ? 0 : fotaStrategyPo.getRollbackMode()); // 回滚模式； 1激进策略， 0保守策略
				FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(item.getFirmwareId());
				MyAssertUtil.notNull(fotaFirmwarePo, OTARespCodeEnum.DATA_NOT_FOUND);
				fotaPlanFirmwareListPo.setProjectId(fotaFirmwarePo.getProjectId());

				String userId = "sys";
				CommonUtil.wrapBasePo(fotaPlanFirmwareListPo, userId, now, true);
				fotaPlanFirmwareListPo.setVersion(0);

				return fotaPlanFirmwareListPo;
			}).collect(Collectors.toList());
			insert = ((FotaPlanFirmwareListMapper) fotaPlanFirmwareListDbService.getBaseMapper()).saveAllInBatch(fotaPlanFirmwareListPos);
			if(insert < 1 ){
				log.error("任务固件信息更新写入失败. 任务ID : [ {} ]", fotaPlanId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
			}
			((UpgradeTaskConditionMapper) upgradeTaskConditionDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(fotaPlanId);
			List<UpgradeTaskConditionPo> upgradeTaskConditionPos = taskUpgradeConditionReqList.stream().map(item -> {
				UpgradeTaskConditionPo upgradeTaskConditionPo = new UpgradeTaskConditionPo();
//				BeanUtils.copyProperties(item, upgradeTaskConditionPo);
				upgradeTaskConditionPo.setConditionId(item.getId());
				upgradeTaskConditionPo.setOtaPlanId(fotaPlanId);
				upgradeTaskConditionPo.setCondCode(NumberUtils.toLong(item.getCondCode()));
				upgradeTaskConditionPo.setCondName(item.getCondName());
				upgradeTaskConditionPo.setValueSource(item.getValueType());
				upgradeTaskConditionPo.setValueType(NumberUtils.toInt(item.getValue()));
				CommonUtil.wrapBasePo(upgradeTaskConditionPo, fotaPlanFirmwareListPos.get(0).getCreateBy(), fotaPlanFirmwareListPos.get(0).getCreateTime(),true);
				return upgradeTaskConditionPo;
			}).collect(Collectors.toList());
			insert = ((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).saveAllInBatch(upgradeTaskConditionPos);
			if(insert < 1){
				log.error("任务前置信息更新写入失败. 任务ID : [ {} ]", fotaPlanId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
			}
			((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(fotaPlanId);
			List<UpgradeStrategyPo> upgradeStrategeDos = MyCollectionUtil.map2NewList(upgradeFirmwareList, item -> {
				UpgradeStrategyPo upgradeStrategyPo = new UpgradeStrategyPo();
//				BeanUtils.copyProperties(item, upgradeStrategyPo);
				upgradeStrategyPo.setOtaPlanId(fotaPlanId);
				upgradeStrategyPo.setFirmwareId(item.getFirmwareId());
				upgradeStrategyPo.setMaxConcurrent(3);
				upgradeStrategyPo.setFirmwareVersionId(item.getTargetVersionId());
				upgradeStrategyPo.setRollbackMode(fotaStrategyPo.getRollbackMode());
				upgradeStrategyPo.setCreateTime(LocalDateTime.now());
				CommonUtil.wrapBasePo(upgradeStrategyPo, "sys", LocalDateTime.now(), true);
				return upgradeStrategyPo;
			});
			insert = ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).saveAllInBatch(upgradeStrategeDos);
			if(insert < 1){
				log.error("任务策略信息更新写入失败. 任务ID : [ {} ]", fotaPlanId);
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
			}
			return true;
		}catch (Exception e){
			log.error("SYSTEM_DB_EXCEPTION : [ {} ]" , e.getMessage(), e);
			throw e;
		}
	}
}
