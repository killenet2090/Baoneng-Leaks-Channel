package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.*;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.common.event.MyOtaEventKit;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanUpgradeService;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.ExceptionUtil;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaVersionCheckServiceImpl
 * @Description:  IFotaVersionCheckService服务实现类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service("FotaVersionCheckStrategyV2")
@Slf4j
public class FotaVersionCheckStrategyV2Impl extends AbstractFotaVersionCheckStrategy {

	@Autowired
	IFotaPlanUpgradeService fotaPlanUpgradeService;

	@Autowired
    MyOtaEventKit myOtaEventKit;
	
    /**
     * 逻辑流程：
     * 1、检查升级对象是否存在
     * 2、检查请求参数中是否有非法的固件信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OtaProtocol checkVersion(OtaProtocol req) {
        OtaMessage otaMessage = req.getBody();
        OtaCommonPayload otaCommonPayload = otaMessage.getOtaCommonPayload();
        Integer checkSourceType = req.getBody().getOtaCommonPayload().getSourceType();
        MyAssertUtil.notNull(checkSourceType, TBoxRespCodeEnum.OTA_RESP_CODE_PARAM_ERROR_SOURCE_TYPE);

        String vin = req.getOtaMessageHeader().getVin();
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        //校验车辆是否存在
        MyAssertUtil.notNull(fotaVinCacheInfo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);

        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
        //校验车辆是否存在
        MyAssertUtil.notNull(fotaObjectPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);

        //校验固件清单是否存在
        List<FotaFirmwareListPo> fotaFirmwareListPos = fotaFirmwareListDbService.listAllByObjectId(fotaObjectPo.getId());
        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaFirmwareListPos), TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);

        MyAssertUtil.isTrue(!org.springframework.util.StringUtils.isEmpty(req.getBody().getOtaUpVersionCheck().getConfVersion()), TBoxRespCodeEnum.OTA_RESP_CODE_PARAM_ERROR_CONF_VERSION);
        //检查是否有固件配置信息更新
        if(!fotaObjectPo.getConfVersion().equals(req.getBody().getOtaUpVersionCheck().getConfVersion())){
            log.warn("OTA云端配置信息有更新.vin={}", vin);
            TboxAdamException tboxAdamException = ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_CONF_VERSION_NOT_MATCH);
            //设置该消息不需要推送回APP
            tboxAdamException.setPush2App(false);
            return failCheckVersion(req, tboxAdamException);
        }

        List<Long> firmwareIds = MyCollectionUtil.map2NewList(fotaFirmwareListPos, FotaFirmwareListPo::getFirmwareId);
        List<FotaFirmwarePo> fOtaFirmwarePos = fotaFirmwareDbService.listByIds(firmwareIds);

        log.info("req.getBody().getEcuModules().size={}, fotaFirmwarePos.size={}", req.getBody().getOtaUpVersionCheck().getEcuModules().size(), fOtaFirmwarePos.size());
        String projectId = fotaObjectPo.getProjectId();

        List<com.bnmotor.icv.adam.sdk.ota.domain.EcuModule> ecuModules = req.getBody().getOtaUpVersionCheck().getEcuModules();
        //检查是否匹配到后台固件管理平台列表
        Function<FotaFirmwarePo, String> mapper1 = (FotaFirmwarePo fotaFirmware) -> fotaFirmware.getFirmwareCode();
        Set<String> fotaFirmwareKeys = fOtaFirmwarePos.stream().collect(Collectors.mapping(mapper1, Collectors.toSet()));
        
        List<EcuModule> invalidEcuModule = ecuModules.stream().filter(ecuModule -> !fotaFirmwareKeys.contains(ecuModule.getFirmwareCode())).collect(Collectors.toList());
        if (MyCollectionUtil.isNotEmpty(invalidEcuModule)) { // ECU不存在于列表中，直接返回失败
        	log.warn("升级请求固件参数不合法.ecuModule={}", invalidEcuModule);
        	return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }

        //检查当前车辆是否存在有效的任务
//        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.findOneByObjectId(fotaObjectPo.getId());
//        if(Objects.isNull(fotaPlanObjListPo)){
//            log.warn("获取升级任务对象失败.fotaObjectDo.getId()={}", fotaObjectPo.getId());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
        //检查是否存在有效的升级任务
//        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaPlanObjListPo.getOtaPlanId());
        final List<FotaPlanObjListPo> fotaPlanObjListPos = Lists.newArrayList();
        FotaPlanPo fotaPlanPo = fotaPlanUpgradeService.selectPlan4Upgrade(fotaObjectPo.getId(), fotaPlanObjListPos);
        if(Objects.isNull(fotaPlanPo)){
            log.info("获取升级任务失败.fotaObjectPo={}", fotaObjectPo.getId());
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }
        
        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListPos.stream().findFirst().orElse(null);
		if(Objects.isNull(fotaPlanObjListPo)){
			log.warn("获取升级任务对象失败.fotaObjectDo.getId()={}", fotaObjectPo.getId());
			return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
		}

		//设置新版本通知回复
		if(Objects.nonNull(req.getBody().getOtaCommonPayload().getSourceType())){
            if(AppEnums.ClientSourceTypeEnum.FROM_OTA.getType() == req.getBody().getOtaCommonPayload().getSourceType() && (Objects.isNull(fotaPlanObjListPo.getNotifyStatus()) || fotaPlanObjListPo.getNotifyStatus() != 2)){
                myOtaEventKit.triggerFotaCheckVersionFromTboxEvent(vin, fotaPlanObjListPo.getId());
            }
        }
//        boolean validPlanPo = MyBusinessUtil.validPlanPo(fotaPlanPo);
//        if(!validPlanPo){
//            log.info("升级任务已经失效.fotaPlanObjListPo={}", fotaPlanObjListPo.toString());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
        //检查是否存在有效的升级任务固件列表
        List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(fotaPlanPo.getFotaStrategyId());
        if(MyCollectionUtil.isEmpty(fotaStrategyFirmwareListPos)){
            log.info("获取升级任务策略固件清单列表失败.otaPlanDo.getId()={}", fotaPlanPo.getId());
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }

        //判断是否存在匹配的固件清单
        List<TempAggregationInfo> tempAggregationInfos = Lists.newArrayList();
        tempAggregationInfos.addAll(assemblyAggregateFirmwareInfoList(fOtaFirmwarePos, ecuModules, fotaStrategyFirmwareListPos));

        //不存在匹配到任务的升级固件清单列表
        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
            log.info("不存在匹配到任务的升级固件清单列表.fotaPlanFirmwareListPos={}", fotaStrategyFirmwareListPos.toString());
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }

        long checkReqId = IdWorker.getId();
        //保存请求记录
        LocalDateTime now = LocalDateTime.now();
        boolean saveReq = saveFotaVersionCheckPo(projectId, checkReqId, vin, checkSourceType, Long.valueOf(req.getBody().getBusinessId()), now, req.getBody().getOtaUpVersionCheck());
        if (!saveReq) {
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.SYS_ERROR));
        }

        //获取升级目标版本回滚策略
        Long strategyId = fotaPlanPo.getFotaStrategyId();
        final FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(strategyId);
        if(Objects.isNull(fotaStrategyPo)){
            log.warn("升级任务ecu策略数据异常.fotaPlanPo={}", fotaObjectPo);
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }

        //对中间过程集迭代操作
        log.info("tempAggregationInfos.size={}", tempAggregationInfos.size());
        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
        	FotaFirmwarePo fotaFirmwarePo = tempAggregationInfo.getFotaFirmwarePo();
            Long firmwareId = fotaFirmwarePo.getId();
            String firmwareVersionNo = tempAggregationInfo.getEcuModule().getFirmwareVersion();
            //车辆当前Ecu固件原始版本对象
            final FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getByVersionCode(projectId, firmwareId, firmwareVersionNo);
            if(Objects.isNull(fotaFirmwareVersionPo)){
                log.warn("未能找到上传固件原始版本号.tempAggregationInfo={}", tempAggregationInfo.toString());
                tempAggregationInfo.setValid(false);
                continue;
            }
            tempAggregationInfo.setFotaFirmwareVersionPo(fotaFirmwareVersionPo);

            //车辆当前Ecu固件目标版本对象
            Long targetVersionId = tempAggregationInfo.getFotaStrategyFirmwareListPo().getTargetVersionId();
            final FotaFirmwareVersionPo targetFotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(targetVersionId);
            if(Objects.isNull(targetFotaFirmwareVersionPo)){
                log.warn("未能找到上传固件目标版本号.tempAggregationInfo={}", tempAggregationInfo.toString());
                tempAggregationInfo.setValid(false);
                continue;
            }
            tempAggregationInfo.setTargetFotaFirmwareVersionPo(targetFotaFirmwareVersionPo);

//          UpgradeStrategyPo upgradeStrategyPo = upgradeStrategyDbService.findOne(fotaPlanPo.getId(), tempAggregationInfo.getFotaFirmwarePo().getId(), targetFotaFirmwareVersionPo.getId());

            Long originVersionId = fotaFirmwareVersionPo.getId();
            List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.findByVersionId(/*projectId, */originVersionId, targetVersionId);
            if(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
                fotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().filter(item -> {
                    boolean sameVersion = item.getStartFirmwareVerId().equals(item.getTargetFirmwareVerId());
                    if(sameVersion){
                        log.info("sameVersion={}, versionId={}", sameVersion, item.getStartFirmwareVerId());
                    }
                    return !sameVersion;
                }).collect(Collectors.toList());
            }
            if(MyCollectionUtil.isEmpty(fotaFirmwareVersionPathPos)){
                log.warn("未能找到有效升级路径.firmwareId={},fromVersionId={}, targetVersionId={}", tempAggregationInfo.getFotaFirmwarePo().getId(), originVersionId, targetVersionId);
                tempAggregationInfo.setValid(false);
                continue;
            }

            //确定所有所有可能的包id列表
            List<Long> pkgIds = fotaFirmwareVersionPathPos.stream().filter(item -> Enums.ZeroOrOneEnum.ONE.getValue() == item.getPkgUpload()).map(item -> {
                //TODO 此处逻辑需要修改
                /*if(Enums.FirmwareUpgradePathTypeEnum.isWholeType(item.getUpgradePathType())){
                    return targetFotaFirmwareVersionPo.getFullPkgId();
                }*/
                //tb_fota_firmware_version_path表中对应的记录都存在firmwarePkgId
                return item.getFirmwarePkgId();
            }).collect(Collectors.toList());

            if(MyCollectionUtil.isEmpty(pkgIds)) {
                log.warn("未能找到有效安装包.FotaFirmwareVersionPathPos={}", fotaFirmwareVersionPathPos.toString());
                tempAggregationInfo.setValid(false);
                continue;
            }
            //getPkg
            log.info("pkgIds={}", pkgIds.toString());
            List<FotaFirmwarePkgPo> fotaFirmwarePkgPos = fotaFirmwarePkgDbService.listByIds(pkgIds);

            //TODO 需要制定策略来决定下载哪个包
            /*
                若获取到包，此处需要根据策略选择包
                1、创建固件时如果选择了，“强制使用全量安装包选项”，此处只能选择全量包
                2、如果同时存在多年升级路径对应的包，可采取取包下载量最小的包
             */

            List<FotaFirmwarePkgPo> sortedFotaFirmwarePkgPos;
            //如果强制使用强制升级模式
            //TODO 此处逻辑需调整为采用单个策略信息
            //全量包
            if(Enums.FirmwareUpgradeModeTypeEnum.isWholeType(tempAggregationInfo.getFotaStrategyFirmwareListPo().getUpgradeMode())){
                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType));
            }else{
                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType).reversed());
            }
            /*if(Objects.nonNull(targetFotaFirmwareVersionPo.getIsForceFullUpdate()) && targetFotaFirmwareVersionPo.getIsForceFullUpdate() == 1){
                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType));
            }else{
                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType).reversed());
            }*/
            //此时可以确定使用的是差分包还是全量包
            FotaFirmwarePkgPo targetFotaFirmwarePkgPo = sortedFotaFirmwarePkgPos.get(0);
            paddingFotaFirmwarePkgPoFlashScript(targetFotaFirmwarePkgPo, fotaFirmwarePo);
            tempAggregationInfo.setTargetFotaFirmwarePkgPo(targetFotaFirmwarePkgPo);

            //如果需要回滚,需要获取到原始包相关信息
            Integer rollbackMode = fotaStrategyPo.getRollbackMode(); // 回滚模式； 1 - 激进策略， 0 - 保守策略
            rollbackMode = Objects.isNull(rollbackMode) ? 1 : rollbackMode; // 默认返回保守模式（需要回滚）
            if(Enums.ZeroOrOneEnum.ONE.getValue() == rollbackMode){
                FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwareVersionPo.getFullPkgId());
                if(Objects.isNull(fotaFirmwarePkgPo)) {
                    log.warn("未能找到有效原始安装包.fotaFirmwareVersionDo={}", fotaFirmwareVersionPo.toString());
                    tempAggregationInfo.setValid(false);
                    continue;
                }
                paddingFotaFirmwarePkgPoFlashScript(fotaFirmwarePkgPo, fotaFirmwarePo);
                tempAggregationInfo.setRollbackMode(rollbackMode);
                tempAggregationInfo.setFotaFirmwarePkgPo(fotaFirmwarePkgPo);
            }
            tempAggregationInfo.setValid(true);
        }

        //确定有效的列表
        tempAggregationInfos = tempAggregationInfos.stream().filter(item -> item.isValid()).sorted(Comparator.comparing(item -> item.getFotaStrategyFirmwareListPo().getOrderNum())).collect(Collectors.toList());
        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
            log.info("未能找到有效安装包.req={}", req.toString());
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
        }

        List<com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo> ecuFirmwareVersionInfos = Lists.newArrayList();
        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
            Integer groupSeq = tempAggregationInfo.getFotaStrategyFirmwareListPo().getDbGroupSeq(); // GroupSeq => DbGroupSeq
            Integer updateSeq = tempAggregationInfo.getFotaStrategyFirmwareListPo().getOrderNum();
            com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo ecuFirmwareVersionInfo = buildEcuFirmwareVersionInfo(tempAggregationInfo, groupSeq, updateSeq);
            ecuFirmwareVersionInfos.add(ecuFirmwareVersionInfo);
        }

        Long transId = IdWorker.getId();
        otaCommonPayload.setTransId(transId);
        otaCommonPayload.setTaskId(fotaPlanPo.getId());
        
        //插入升级验证表记录
        boolean insert = saveFotaVersionCheckVerifyPo(transId, checkReqId, projectId, fotaObjectPo.getId(), fotaPlanPo.getId(), fotaPlanObjListPo.getId(), fotaObjectPo.getObjectId(),now);
        if(!insert){
            log.warn("插入升级验证表记录失败.vin={}", vin);
            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.SYS_ERROR));
        }

        //补充更新升级计划中的升级对象状态为：检查到新版本
        update4FotaPlanObjListPo(fotaPlanObjListPo.getId(), now);

        //删除之前可能存在的任务详情
        deleteTaskDetails(fotaPlanPo.getId(), fotaPlanObjListPo.getId());

        //添加任务详情列表
        saveTaskDetails(fotaPlanPo.getProjectId(), fotaPlanPo.getId(), fotaPlanObjListPo.getId(), tempAggregationInfos);

        //补充前置升级条件
        List<UpgradeCondition> upgradeConditions = getUpgradeConditions(fotaPlanPo);

        //构建下发到汇聚平台的消息结构体
        OtaDownVersionCheckResponse otaDownVersionCheckResponse = buildOtaDownVersionCheckResponse(fotaPlanPo, fotaStrategyPo, ecuFirmwareVersionInfos, upgradeConditions);

        //更新版本检查请求中的状态为：检查到新版本
        update4FotaVersionCheckPo(checkReqId, now, otaDownVersionCheckResponse);

        //重新设置车辆缓存
        setFotaObjectCacheInfo(fotaObjectPo, fotaPlanObjListPo, transId, checkReqId);

        //返回检查到检查成功的消息
        return ok(req, transId, fotaPlanPo.getId(), otaDownVersionCheckResponse, null);
    }
    
    public List<TempAggregationInfo> assemblyAggregateFirmwareInfoList(List<FotaFirmwarePo> fotaFirmwareList, List<EcuModule> ecuModuleList, List<FotaStrategyFirmwareListPo> fotaPlanFirmwareListList) {

		Map<String, EcuModule> ecuModuleMap = ecuModuleList.stream().collect(Collectors.toMap(ecuModuleMapper(), Function.identity(), (k1, k2) -> k1));
		Set<String> ecukeyset = ecuModuleMap.keySet();

		Map<String, FotaFirmwarePo> fotaFirmwareMap = fotaFirmwareList.stream().collect(Collectors.toMap(fotaFirmwareMapper(), Function.identity(), (k1, k2) -> k1));
		Set<String> firmwarekeyset = fotaFirmwareMap.keySet();

		Map<Long, FotaStrategyFirmwareListPo> fotaPlanFirmwareListMap = fotaPlanFirmwareListList.stream().collect(Collectors.toMap(FotaStrategyFirmwareListPo::getFirmwareId, Function.identity(), (k1, k2) -> k1));
		List<String> theSameKey = intersection(ecukeyset, firmwarekeyset);
		List<TempAggregationInfo> aggregateFirmwareInfoList = theSameKey.stream().map(tempAggregationInfoMapper(fotaFirmwareMap, ecuModuleMap, fotaPlanFirmwareListMap)).collect(Collectors.toList());

		return aggregateFirmwareInfoList.stream().filter(it -> it.isValid()).collect(Collectors.toList());
	}
    
    public Function<String , TempAggregationInfo> tempAggregationInfoMapper(Map<String, FotaFirmwarePo> fotaFirmwareMap, Map<String, EcuModule> ecuModuleMap, Map<Long, FotaStrategyFirmwareListPo> fotaPlanFirmwareListMap) {
    	return (key) -> {
	    	FotaFirmwarePo firmware = fotaFirmwareMap.get(key);
			EcuModule ecumodule = ecuModuleMap.get(key);
			Long firmwareId = firmware.getId();
			FotaStrategyFirmwareListPo fotaPlanFirmwareList = fotaPlanFirmwareListMap.get(firmwareId);
			return TempAggregationInfo.builder().ecuModule(ecumodule).fotaFirmwarePo(firmware).fotaStrategyFirmwareListPo(fotaPlanFirmwareList).valid(Objects.nonNull(fotaPlanFirmwareList)).build();
    	};
    }
    
    /**
     * 保存任务详情
     * @param projectId
     * @param otaPlanObjId
     * @param tempAggregationInfos
     */
    public void saveTaskDetails(String projectId, long otaPlanId, long otaPlanObjId, List<TempAggregationInfo> tempAggregationInfos){
        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
            log.warn("不存在升级固件列表,放弃保存升级任务详情. otaPlanObjId={}, tempAggregationInfos={}", otaPlanObjId, Objects.toString(tempAggregationInfos));
            return;
        }
        List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = Lists.newArrayList();
        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
            FotaPlanTaskDetailPo fotaPlanTaskDetailPo = new FotaPlanTaskDetailPo();
            fotaPlanTaskDetailPo.setId(IdWorker.getId());
            fotaPlanTaskDetailPo.setOtaPlanObjId(otaPlanObjId);
            fotaPlanTaskDetailPo.setOtaPlanFirmwareId(tempAggregationInfo.getFotaStrategyFirmwareListPo().getId());
            fotaPlanTaskDetailPo.setProjectId(projectId);
            fotaPlanTaskDetailPo.setOtaPlanId(otaPlanId);
            fotaPlanTaskDetailPo.setSourceVersion(tempAggregationInfo.getFotaFirmwareVersionPo().getFirmwareVersionNo());
            fotaPlanTaskDetailPo.setCurrentVersion(tempAggregationInfo.getFotaFirmwareVersionPo().getFirmwareVersionNo());
            fotaPlanTaskDetailPo.setInstalledVersion(tempAggregationInfo.getTargetFotaFirmwareVersionPo().getFirmwareVersionNo());
            fotaPlanTaskDetailPo.setStartTime(new Date());
            fotaPlanTaskDetailPo.setStatus(Enums.PlanTaskDetailStatusEnum.UPGRADE_NO.getType());
            CommonUtil.wrapBasePo(fotaPlanTaskDetailPo, true);
            fotaPlanTaskDetailPos.add(fotaPlanTaskDetailPo);
        }
        boolean status = fotaPlanTaskDetailDbService.saveBatch(fotaPlanTaskDetailPos);
        if (!status) {
            log.warn("版本检查升级任务明细信息更新写入失败. otaPlanObjId={}, tempAggregationInfos={}", otaPlanObjId, Objects.toString(tempAggregationInfos));
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }
    }
    
    @Override
    public void afterInitialize() {
    	fotaVersionCheckService.register(RebuildFlagEnum.V2, this);
    }

}