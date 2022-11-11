package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.sdk.ota.domain.*;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.adam.sdk.ota.up.OtaUpVersionCheck;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventKit;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventType;
import com.bnmotor.icv.tsp.ota.config.MinIoConfig;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeMessageBase;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeNewVersionMessage;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.OtaMessageMapper;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaVersionCheckServiceImpl
 * @Description:  IFotaVersionCheckService服务实现类
 * @author xxc
 * @since 2020-07-15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Data
@Slf4j
public abstract class AbstractFotaVersionCheckStrategy implements IFotaVersionCheckStrategy {

    @Autowired
    MinIoConfig minIoConfig;

    @Autowired
    IFotaVersionCheckDbService fotaVersionCheckDbService;

    @Autowired
    IFotaObjectDbService fotaObjectDbService;

    @Autowired
    IFotaFirmwareListDbService fotaFirmwareListDbService;

    @Autowired
    IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Autowired
    IFotaFirmwareVersionPathDbService fotaFirmwareVersionPathDbService;

    @Autowired
    IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

    @Autowired
    IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

    @Autowired
    IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

    @Autowired
    IUpgradeStrategyDbService upgradeStrategyDbService;

    @Autowired
    IFotaUpgradeConditionDbService fotaUpgradeConditionDbService;

    @Autowired
    IUpgradeTaskConditionDbService upgradeTaskConditionDbService;

    @Autowired
    IFotaPlanTaskDetailDbService fotaPlanTaskDetailDbService;

    @Autowired
    IFotaStrategyDbService fotaStrategyDbService;

    @Autowired
    IFotaStrategyPreConditionDbService fotaStrategyPreConditionDbService;

    @Autowired
    ICommonExecutor commonExecutor;

    @Autowired
    TBoxDownHandler boxDownHandler;

    @Autowired
    OtaEventKit otaEventKit;

    @Autowired
    IFotaObjectCacheInfoService fotaObjectCacheInfoService;
    
    @Autowired
    IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;
    
    @Autowired
    IFotaConfigService fotaConfigService;
    
    @Autowired
	IFotaVersionCheckService fotaVersionCheckService;

    @Override
    public List<FotaFirmwarePo> queryFotaFirmwarePos(String vin) {
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);
        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
        MyAssertUtil.notNull(fotaObjectPo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);

        return fotaConfigService.queryFotaFirmwarePosInner(fotaObjectPo);
    }

    /**
     * 逻辑流程：
     * 1、检查升级对象是否存在
     * 2、检查请求参数中是否有非法的固件信息
     * @param req
     * @return
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public OtaProtocol checkVersion(OtaProtocol req) {
//        OtaMessage otaMessage = req.getBody();
//        OtaCommonPayload otaCommonPayload = otaMessage.getOtaCommonPayload();
//        Integer checkSourceType = req.getBody().getOtaCommonPayload().getSourceType();
//        MyAssertUtil.notNull(checkSourceType, TBoxRespCodeEnum.OTA_RESP_CODE_PARAM_ERROR_SOURCE_TYPE);
//
//        String vin = req.getOtaMessageHeader().getVin();
//        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
//        //校验车辆是否存在
//        MyAssertUtil.notNull(fotaVinCacheInfo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//
//        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
//        //校验车辆是否存在
//        MyAssertUtil.notNull(fotaObjectPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//
//        //校验固件清单是否存在
//        List<FotaFirmwareListPo> fotaFirmwareListPos = fotaFirmwareListDbService.listAllByObjectId(fotaObjectPo.getId());
//        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaFirmwareListPos), TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//
//        MyAssertUtil.isTrue(!org.springframework.util.StringUtils.isEmpty(req.getBody().getOtaUpVersionCheck().getConfVersion()), TBoxRespCodeEnum.OTA_RESP_CODE_PARAM_ERROR_CONF_VERSION);
//        //检查是否有固件配置信息更新
//        if(!fotaObjectPo.getConfVersion().equals(req.getBody().getOtaUpVersionCheck().getConfVersion())){
//            log.warn("OTA云端配置信息有更新.vin={}", vin);
//            TboxAdamException tboxAdamException = ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_CONF_VERSION_NOT_MATCH);
//            //设置该消息不需要推送回APP
//            tboxAdamException.setPush2App(false);
//            return failCheckVersion(req, tboxAdamException);
//        }
//
//        List<Long> firmwareIds = MyCollectionUtil.map2NewList(fotaFirmwareListPos, FotaFirmwareListPo::getFirmwareId);
//        List<FotaFirmwarePo> fOtaFirmwarePos = fotaFirmwareDbService.listByIds(firmwareIds);
//
//        log.info("req.getBody().getEcuModules().size={}, fotaFirmwarePos.size={}", req.getBody().getOtaUpVersionCheck().getEcuModules().size(), fOtaFirmwarePos.size());
//        String projectId = fotaObjectPo.getProjectId();
//
//        List<com.bnmotor.icv.adam.sdk.ota.domain.EcuModule> ecuModules = req.getBody().getOtaUpVersionCheck().getEcuModules();
//        //检查是否匹配到后台固件管理平台列表
//        Function<FotaFirmwarePo, String> mapper1 = (FotaFirmwarePo fotaFirmware) -> fotaFirmware.getFirmwareCode();
//        Set<String> fotaFirmwareKeys = fOtaFirmwarePos.stream().collect(Collectors.mapping(mapper1, Collectors.toSet()));
//        
//        List<EcuModule> invalidEcuModule = ecuModules.stream().filter(ecuModule -> !fotaFirmwareKeys.contains(ecuModule.getFirmwareCode())).collect(Collectors.toList());
//        if (MyCollectionUtil.isNotEmpty(invalidEcuModule)) { // ECU不存在于列表中，直接返回失败
//        	log.warn("升级请求固件参数不合法.ecuModule={}", invalidEcuModule);
//        	return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//
//        //检查当前车辆是否存在有效的任务
//        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.findOneByObjectId(fotaObjectPo.getId());
//        if(Objects.isNull(fotaPlanObjListPo)){
//            log.warn("获取升级任务对象失败.fotaObjectDo.getId()={}", fotaObjectPo.getId());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//        //检查是否存在有效的升级任务
//        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaPlanObjListPo.getOtaPlanId());
//        if(Objects.isNull(fotaPlanPo)){
//            log.info("获取升级任务失败.fotaPlanObjListPo={}", fotaPlanObjListPo.toString());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//        boolean validPlanPo = MyBusinessUtil.validPlanPo(fotaPlanPo);
//        if(!validPlanPo){
//            log.info("升级任务已经失效.fotaPlanObjListPo={}", fotaPlanObjListPo.toString());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//        //检查是否存在有效的升级任务固件列表
//        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListDbService.listByOtaPlanId(fotaPlanPo.getId());
//        if(MyCollectionUtil.isEmpty(fotaPlanFirmwareListPos)){
//            log.info("获取升级任务清单列表失败.otaPlanDo.getId()={}", fotaPlanPo.getId());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//
//        //判断是否存在匹配的固件清单
////        Map<Long, FotaFirmwarePo> fotaFirmwarePosMap = fotaFirmwarePos.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
//        List<TempAggregationInfo> tempAggregationInfos = Lists.newArrayList();
//        tempAggregationInfos.addAll(assemblyAggregateFirmwareInfoList(fOtaFirmwarePos, ecuModules, fotaPlanFirmwareListPos));
//
//        //不存在匹配到任务的升级固件清单列表
//        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
//            log.info("不存在匹配到任务的升级固件清单列表.fotaPlanFirmwareListPos={}", fotaPlanFirmwareListPos.toString());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//
//        long checkReqId = IdWorker.getId();
//        //保存请求记录
//        LocalDateTime now = LocalDateTime.now();
//        boolean saveReq = saveFotaVersionCheckPo(projectId, checkReqId, vin, checkSourceType, Long.valueOf(req.getBody().getBusinessId()), now, req.getBody().getOtaUpVersionCheck());
//        if (!saveReq) {
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.SYS_ERROR));
//        }
//
//        //对中间过程集迭代操作
//        log.info("tempAggregationInfos.size={}", tempAggregationInfos.size());
//        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
//            Long firmwareId = tempAggregationInfo.getFotaFirmwarePo().getId();
//            String firmwareVersionNo = tempAggregationInfo.getEcuModule().getFirmwareVersion();
//            //车辆当前Ecu固件原始版本对象
//            final FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getByVersionCode(projectId, firmwareId, firmwareVersionNo);
//            if(Objects.isNull(fotaFirmwareVersionPo)){
//                log.warn("未能找到上传固件原始版本号.tempAggregationInfo={}", tempAggregationInfo.toString());
//                tempAggregationInfo.setValid(false);
//                continue;
//            }
//            tempAggregationInfo.setFotaFirmwareVersionPo(fotaFirmwareVersionPo);
//
//            //车辆当前Ecu固件目标版本对象
//            Long targetVersionId = tempAggregationInfo.getFotaPlanFirmwareListPo().getFirmwareVersionId();
//            final FotaFirmwareVersionPo targetFotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(targetVersionId);
//            if(Objects.isNull(targetFotaFirmwareVersionPo)){
//                log.warn("未能找到上传固件目标版本号.tempAggregationInfo={}", tempAggregationInfo.toString());
//                tempAggregationInfo.setValid(false);
//                continue;
//            }
//            tempAggregationInfo.setTargetFotaFirmwareVersionPo(targetFotaFirmwareVersionPo);
//
//            //获取升级目标版本回滚策略
//            UpgradeStrategyPo upgradeStrategyPo = upgradeStrategyDbService.findOne(fotaPlanPo.getId(), tempAggregationInfo.getFotaFirmwarePo().getId(), targetFotaFirmwareVersionPo.getId());
//            if(Objects.isNull(upgradeStrategyPo)){
//                log.warn("升级任务ecu策略数据异常");
//                tempAggregationInfo.setValid(false);
//                continue;
//            }
//
//            Long originVersionId = fotaFirmwareVersionPo.getId();
//            List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.findByVersionId(/*projectId, */originVersionId, targetVersionId);
//            if(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
//                fotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().filter(item -> {
//                    boolean sameVersion = item.getStartFirmwareVerId().equals(item.getTargetFirmwareVerId());
//                    if(sameVersion){
//                        log.info("sameVersion={}, versionId={}", sameVersion, item.getStartFirmwareVerId());
//                    }
//                    return !sameVersion;
//                }).collect(Collectors.toList());
//            }
//            if(MyCollectionUtil.isEmpty(fotaFirmwareVersionPathPos)){
//                log.warn("未能找到有效升级路径.firmwareId={},fromVersionId={}, targetVersionId={}", tempAggregationInfo.getFotaFirmwarePo().getId(), originVersionId, targetVersionId);
//                tempAggregationInfo.setValid(false);
//                continue;
//            }
//
//            //确定所有所有可能的包id列表
//            List<Long> pkgIds = fotaFirmwareVersionPathPos.stream().filter(item -> Enums.ZeroOrOneEnum.ONE.getValue() == item.getPkgUpload()).map(item -> {
//                if(Enums.FirmwareUpgradePathTypeEnum.isWholeType(item.getUpgradePathType())){
//                    return targetFotaFirmwareVersionPo.getFullPkgId();
//                }
//                return item.getFirmwarePkgId();
//            }).collect(Collectors.toList());
//
//            if(MyCollectionUtil.isEmpty(pkgIds)) {
//                log.warn("未能找到有效安装包.FotaFirmwareVersionPathPos={}", fotaFirmwareVersionPathPos.toString());
//                tempAggregationInfo.setValid(false);
//                continue;
//            }
//            //getPkg
//            log.info("pkgIds={}", pkgIds.toString());
//            List<FotaFirmwarePkgPo> fotaFirmwarePkgPos = (List)fotaFirmwarePkgDbService.listByIds(pkgIds);
//
//            //TODO 需要制定策略来决定下载哪个包
//            /*
//                若获取到包，此处需要根据策略选择包
//                1、创建固件时如果选择了，“强制使用全量安装包选项”，此处只能选择全量包
//                2、如果同时存在多年升级路径对应的包，可采取取包下载量最小的包
//             */
//
//            List<FotaFirmwarePkgPo> sortedFotaFirmwarePkgPos;
//            //如果强制使用强制升级模式
//            if(Objects.nonNull(targetFotaFirmwareVersionPo.getIsForceFullUpdate()) && targetFotaFirmwareVersionPo.getIsForceFullUpdate() == 1){
//                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType));
//            }else{
//                sortedFotaFirmwarePkgPos = MyCollectionUtil.newSortedList(fotaFirmwarePkgPos, Comparator.comparing(FotaFirmwarePkgPo::getPkgType).reversed());
//            }
//            //此时可以确定使用的是差分包还是全量包
//            FotaFirmwarePkgPo targetFotaFirmwarePkgPo = sortedFotaFirmwarePkgPos.get(0);
//            tempAggregationInfo.setTargetFotaFirmwarePkgPo(targetFotaFirmwarePkgPo);
//
//            //如果需要回滚,需要获取到原始包相关信息
//            if(Enums.ZeroOrOneEnum.ONE.getValue() == upgradeStrategyPo.getRollbackMode()){
//                FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwareVersionPo.getFullPkgId());
//                if(Objects.isNull(fotaFirmwarePkgPo)) {
//                    log.warn("未能找到有效原始安装包.fotaFirmwareVersionDo={}", fotaFirmwareVersionPo.toString());
//                    tempAggregationInfo.setValid(false);
//                    continue;
//                }
//                tempAggregationInfo.setRollbackMode(upgradeStrategyPo.getRollbackMode());
//                tempAggregationInfo.setFotaFirmwarePkgPo(fotaFirmwarePkgPo);
//            }
//            tempAggregationInfo.setValid(true);
//        }
//
//        //确定有效的列表
//        tempAggregationInfos = tempAggregationInfos.stream().filter(item -> item.isValid()).sorted(Comparator.comparing(item -> item.getFotaPlanFirmwareListPo().getUpgradeSeq())).collect(Collectors.toList());
//        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
//            log.info("未能找到有效安装包.req={}", req.toString());
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION));
//        }
//
//        List<com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo> ecuFirmwareVersionInfos = Lists.newArrayList();
//        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
//            int groupSeq = tempAggregationInfo.getFotaPlanFirmwareListPo().getGroupSeq();
//            int updateSeq = tempAggregationInfo.getFotaPlanFirmwareListPo().getUpgradeSeq();
//            com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo ecuFirmwareVersionInfo = buildEcuFirmwareVersionInfo(tempAggregationInfo, groupSeq, updateSeq);
//            ecuFirmwareVersionInfos.add(ecuFirmwareVersionInfo);
//        }
//
//        Long transId = IdWorker.getId();
//        otaCommonPayload.setTransId(transId);
//        otaCommonPayload.setTaskId(fotaPlanPo.getId());
//        
//        //插入升级验证表记录
//        boolean insert = saveFotaVersionCheckVerifyPo(transId, checkReqId, projectId, fotaObjectPo.getId(), fotaPlanPo.getId(), fotaPlanObjListPo.getId(), fotaObjectPo.getObjectId(),now);
//        if(!insert){
//            log.warn("插入升级验证表记录失败.vin={}", vin);
//            return failCheckVersion(req, ExceptionUtil.buildTboxAdamException(TBoxRespCodeEnum.SYS_ERROR));
//        }
//
//        //补充更新升级计划中的升级对象状态为：检查到新版本
//        update4FotaPlanObjListPo(fotaPlanObjListPo.getId(), now);
//
//        //删除之前可能存在的任务详情
//        deleteTaskDetails(fotaPlanPo.getId(), fotaPlanObjListPo.getId());
//
//        //添加任务详情列表
//        saveTaskDetails(fotaPlanPo.getProjectId(), fotaPlanPo.getId(), fotaPlanObjListPo.getId(), tempAggregationInfos);
//
//        //补充前置升级条件
//        List<UpgradeCondition> upgradeConditions = getUpgradeConditions(fotaPlanPo);
//
//        //构建下发到汇聚平台的消息结构体
//        OtaDownVersionCheckResponse otaDownVersionCheckResponse = buildOtaDownVersionCheckResponse(transId, fotaPlanPo, ecuFirmwareVersionInfos, upgradeConditions);
//
//        //更新版本检查请求中的状态为：检查到新版本
//        update4FotaVersionCheckPo(checkReqId, now, otaDownVersionCheckResponse);
//
//        //重新设置车辆缓存
//        setFotaObjectCacheInfo(fotaObjectPo, fotaPlanObjListPo, transId, checkReqId);
//
//        //返回检查到检查成功的消息
//        return ok(req, transId, fotaPlanPo.getId(), otaDownVersionCheckResponse, null);
//    }

    /**
     * 保存车辆缓存升级对象
     * @param fotaObjectPo
     * @param fotaPlanObjListPo
     * @param transId
     */
    public void setFotaObjectCacheInfo(FotaObjectPo fotaObjectPo, FotaPlanObjListPo fotaPlanObjListPo, Long transId, Long checkReqId) {
        FotaVinCacheInfo fotaVinCacheInfo = new FotaVinCacheInfo();
        fotaVinCacheInfo.setVin(fotaObjectPo.getObjectId());
        fotaVinCacheInfo.setObjectId(fotaObjectPo.getId());
        fotaVinCacheInfo.setOtaPlanId(fotaPlanObjListPo.getOtaPlanId());
        fotaVinCacheInfo.setOtaPlanObjectId(fotaPlanObjListPo.getId());// 对应记录表的id
        fotaVinCacheInfo.setTransId(transId);
        fotaVinCacheInfo.setCheckReqId(checkReqId);
        fotaObjectCacheInfoService.setFotaVinCacheInfo(fotaVinCacheInfo);
    }

    /**
     * 逻辑删除当次升级之前可能存在的历史数据
     * @param otaPlanObjId
     */
    public boolean deleteTaskDetails(long otaPlanId, long otaPlanObjId){
        fotaPlanTaskDetailDbService.deleteByOtaPlanObjId(otaPlanId, otaPlanObjId);
        return true;
    }

    /**
     * 保存任务详情
     * @param projectId
     * @param otaPlanObjId
     * @param tempAggregationInfos
     */
//    public void saveTaskDetails(String projectId, long otaPlanId, long otaPlanObjId, List<TempAggregationInfo> tempAggregationInfos){
//        if(MyCollectionUtil.isEmpty(tempAggregationInfos)){
//            log.warn("不存在升级固件列表,放弃保存升级任务详情. otaPlanObjId={}, tempAggregationInfos={}", otaPlanObjId, Objects.toString(tempAggregationInfos));
//            return;
//        }
//        List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = Lists.newArrayList();
//        for (TempAggregationInfo tempAggregationInfo : tempAggregationInfos) {
//            FotaPlanTaskDetailPo fotaPlanTaskDetailPo = new FotaPlanTaskDetailPo();
//            fotaPlanTaskDetailPo.setId(IdWorker.getId());
//            fotaPlanTaskDetailPo.setOtaPlanObjId(otaPlanObjId);
//            fotaPlanTaskDetailPo.setOtaPlanFirmwareId(tempAggregationInfo.getFotaPlanFirmwareListPo().getId());
//            fotaPlanTaskDetailPo.setProjectId(projectId);
//            fotaPlanTaskDetailPo.setOtaPlanId(otaPlanId);
//            fotaPlanTaskDetailPo.setSourceVersion(tempAggregationInfo.getFotaFirmwareVersionPo().getFirmwareVersionNo());
//            fotaPlanTaskDetailPo.setCurrentVersion(tempAggregationInfo.getFotaFirmwareVersionPo().getFirmwareVersionNo());
//            fotaPlanTaskDetailPo.setInstalledVersion(tempAggregationInfo.getTargetFotaFirmwareVersionPo().getFirmwareVersionNo());
//            fotaPlanTaskDetailPo.setStartTime(new Date());
//            fotaPlanTaskDetailPo.setStatus(Enums.PlanTaskDetailStatusEnum.UPGRADE_NO.getType());
//            CommonUtil.wrapBasePo(fotaPlanTaskDetailPo, true);
//            fotaPlanTaskDetailPos.add(fotaPlanTaskDetailPo);
//        }
//        boolean status = fotaPlanTaskDetailDbService.saveBatch(fotaPlanTaskDetailPos);
//        if (!status) {
//            log.warn("版本检查升级任务明细信息更新写入失败. otaPlanObjId={}, tempAggregationInfos={}", otaPlanObjId, Objects.toString(tempAggregationInfos));
//            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
//        }
//    }

    /**
     * 保存请求记录
     * @param projectId
     * @param checkReqId
     * @param vin
     * @param businessId
     * @param now
     * @return
     */
    public boolean saveFotaVersionCheckPo(String projectId, long checkReqId, String vin, Integer checkSourceType, Long businessId, LocalDateTime now, OtaUpVersionCheck otaUpVersionCheck){
        //保存请求记录：此处逻辑有待考虑
        FotaVersionCheckPo fotaVersionCheckPo = new FotaVersionCheckPo();
        fotaVersionCheckPo.setId(checkReqId);
        fotaVersionCheckPo.setProjectId(projectId);
        fotaVersionCheckPo.setReqTime(MyDateUtil.localDateTimeToUDate(now));
        fotaVersionCheckPo.setCheckSourceType(checkSourceType);
        fotaVersionCheckPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
        fotaVersionCheckPo.setVehicleId(vin);
        fotaVersionCheckPo.setBusinessId(businessId);
        try {
            fotaVersionCheckPo.setCheckParam(JsonUtil.toJson(otaUpVersionCheck));
        }catch (Exception ex){
            log.error("请求参数转换为json异常.otaUpVersionCheck={}", otaUpVersionCheck.toString());
            return false;
        }
        boolean saveReq = fotaVersionCheckDbService.save(fotaVersionCheckPo);
        if (!saveReq) {
            log.warn("保存固件版本升级请求记录失败.checkReqId={}, vin={}, businessId={}", checkReqId, vin, businessId);
        }
        return saveReq;
    }

    /**
     * 插入升级验证表记录
     * @param transId
     * @param checkReqId
     * @param projectId
     * @param objectId
     * @param planId
     * @param otaPlanObjectId
     * @param vin
     * @return
     */
    public boolean saveFotaVersionCheckVerifyPo(Long transId, Long checkReqId, String projectId, Long objectId, Long planId, Long otaPlanObjectId, String vin, LocalDateTime now){
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = new FotaVersionCheckVerifyPo();
        fotaVersionCheckVerifyPo.setId(transId);
        fotaVersionCheckVerifyPo.setCheckReqId(checkReqId);
        fotaVersionCheckVerifyPo.setProjectId(projectId);
        fotaVersionCheckVerifyPo.setObjectId(objectId);
        fotaVersionCheckVerifyPo.setOtaPlanId(planId);
        fotaVersionCheckVerifyPo.setOtaPlanObjectId(otaPlanObjectId);
        fotaVersionCheckVerifyPo.setVin(vin);
        fotaVersionCheckVerifyPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
        boolean insert = fotaVersionCheckVerifyDbService.save(fotaVersionCheckVerifyPo);
        if(!insert){
            log.warn("插入版本检查数据记录失败.fotaVersionCheckVerifyPo={}", fotaVersionCheckVerifyPo.toString());
        }
        return insert;
    }

    /**
     * 更新FotaVersionCheckPo
     * @param checkReqId
     * @param now
     */
    public void update4FotaVersionCheckPo(Long checkReqId, LocalDateTime now, OtaDownVersionCheckResponse otaDownVersionCheckResponse){
        FotaVersionCheckPo fotaVersionCheckPo = new FotaVersionCheckPo();
        fotaVersionCheckPo.setId(checkReqId);
        fotaVersionCheckPo.setCheckResult(AppEnums.CheckVersionResultEnum.NEW_VERSION.getType());
        fotaVersionCheckPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        fotaVersionCheckPo.setUpdateTime(now);
        try {
            fotaVersionCheckPo.setCheckResponse(JsonUtil.toJson(otaDownVersionCheckResponse));
        }catch (Exception ex){
            log.error("版本请求结果响应转换为json异常.otaDownVersionCheckResponse={}", otaDownVersionCheckResponse.toString());
            throw new AdamException("版本请求结果响应转换为json异常");
        }
        fotaVersionCheckDbService.updateById(fotaVersionCheckPo);
    }

    /**
     * 更新FotaPlanObjListPo
     * @param otaObjctId
     * @param now
     */
    public void update4FotaPlanObjListPo(Long otaObjctId, LocalDateTime now){
        FotaPlanObjListPo existFotaPlanObjListPo = new FotaPlanObjListPo();
        existFotaPlanObjListPo.setId(otaObjctId);
        existFotaPlanObjListPo.setStatus(Enums.TaskObjStatusTypeEnum.CHECK_VERIFY.getType());
        existFotaPlanObjListPo.setResult(0);
        existFotaPlanObjListPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        existFotaPlanObjListPo.setUpdateTime(now);
        fotaPlanObjListDbService.updateById(existFotaPlanObjListPo);
    }

    /**
     * 获取前置检查条件列表
     * @param fotaPlanPo
     * @return
     */
    public List<UpgradeCondition> getUpgradeConditions(FotaPlanPo fotaPlanPo){
        //如果是新版本任务接口
        if(Objects.nonNull(fotaPlanPo.getFotaStrategyId())){
            log.info("新版本接口,走策略获取条件逻辑");
            /*FotaStrategyPo fotaStrategyPo = fotaStrategyDbService.getById(fotaPlanPo.getFotaStrategyId());*/
            List<FotaStrategyPreConditionPo> fotaStrategyPreConditionPos = fotaStrategyPreConditionDbService.listByOtaStrategyId(fotaPlanPo.getFotaStrategyId());
            log.info("该任务策略关联前置条件列表.size={}", MyCollectionUtil.size(fotaStrategyPreConditionPos));
            return MyCollectionUtil.newCollection(fotaStrategyPreConditionPos, item ->{
                //当前主要用来解析算术表达式：a > 10之类
                UpgradeCondition upgradeCondition = new UpgradeCondition();
                //condCode标志
                upgradeCondition.setCondCode(item.getCondCode());
                upgradeCondition.setCondValue(item.getValue());
                upgradeCondition.setCondValueType(item.getValueType());
                upgradeCondition.setOperatorType(item.getOperatorType());
                upgradeCondition.setOperatorValue(item.getOperatorValue());
                return upgradeCondition;
            });
        }

        Long otaPlanId = fotaPlanPo.getId();
        List<UpgradeTaskConditionPo> upgradeTaskConditionPos = upgradeTaskConditionDbService.selectList(otaPlanId);
        if(MyCollectionUtil.isNotEmpty(upgradeTaskConditionPos)){
            List<Long> upgradeTaskConditionDoIds = upgradeTaskConditionPos.stream().map(item -> item.getConditionId()).collect(Collectors.toList());
            List<FotaUpgradeConditionPo> fotaUpgradeConditionPos = fotaUpgradeConditionDbService.getBaseMapper().selectBatchIds(upgradeTaskConditionDoIds);
            List<UpgradeCondition> upgradeConditions = MyCollectionUtil.map2NewList(fotaUpgradeConditionPos, (FotaUpgradeConditionPo item) -> OtaMessageMapper.INSTANCE.fotaUpgradeConditionPo2OtaUpgradeCondition(item));
            return upgradeConditions;
        }
        return Collections.emptyList();
    }

    /**
     * 构建升级主体信息
     * @param fotaPlanPo
     * @param ecuFirmwareVersionInfos
     * @return
     */
    public OtaDownVersionCheckResponse buildOtaDownVersionCheckResponse(final FotaPlanPo fotaPlanPo, final FotaStrategyPo fotaStrategyPo, final List<com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo> ecuFirmwareVersionInfos, final List<com.bnmotor.icv.adam.sdk.ota.domain.UpgradeCondition> taskPreConditions){
        OtaDownVersionCheckResponse otaDownVersionCheckResponse = new OtaDownVersionCheckResponse();

        //暂时整车版本都使用任务定义版本号
        otaDownVersionCheckResponse.setEntireVersionNo(fotaPlanPo.getTargetVersion());
        otaDownVersionCheckResponse.setTargetVersionNo(fotaPlanPo.getTargetVersion());
        otaDownVersionCheckResponse.setDisclaimer(fotaPlanPo.getDisclaimer());
        otaDownVersionCheckResponse.setDownloadTips(fotaPlanPo.getDownloadTips());
        otaDownVersionCheckResponse.setTaskTips(fotaPlanPo.getTaskTips());
        otaDownVersionCheckResponse.setUpgradeMode(fotaPlanPo.getUpgradeMode());

        //设置需要下载包的总大小
        otaDownVersionCheckResponse.setEstimatedDownloadPackageSize(ecuFirmwareVersionInfos.stream().mapToLong(item -> {
            long fileSize = Long.valueOf(item.getDstPkgInfo().getFileSize()).longValue();
            fileSize += Objects.nonNull(item.getSrcPkgInfo()) ? Long.valueOf(item.getSrcPkgInfo().getFileSize()).longValue() : 0L;
            return fileSize;
        }).sum());
        //TODO
        /*long estimatedTime = 0;
        if(MyCollectionUtil.isNotEmpty(ecuFirmwareVersionInfos)){
            for(EcuFirmwareVersionInfo ecuFirmwareVersionInfo : ecuFirmwareVersionInfos){
                    estimatedTime += ecuFirmwareVersionInfo.getDstPkgInfo().getEstimatedUpgradeTime();
            }
        }*/

        //设置总体的升级时长
        /*otaDownVersionCheckResponse.setEstimatedUpgradeTime(Long.valueOf(estimatedTime).intValue());*/
        if(Objects.nonNull(fotaStrategyPo)){
            otaDownVersionCheckResponse.setEstimatedUpgradeTime(fotaStrategyPo.getEstimateUpgradeTime());
        }
        otaDownVersionCheckResponse.setUpgradeConditions(taskPreConditions);
        otaDownVersionCheckResponse.setEcuFirmwareVersionInfos(ecuFirmwareVersionInfos);

        //TODO 待定值：1=网络判断类型待定，2=是否自动下载
        otaDownVersionCheckResponse.setNetworkType(1);
        //是否自动下载:0=手工提示下载,1=自动下载
        otaDownVersionCheckResponse.setAutoDown(1);
        // reqId这个地方定义无效
        otaDownVersionCheckResponse.setCheckResult(AppEnums.CheckVersionResultEnum.NEW_VERSION.getType());
        return otaDownVersionCheckResponse;
    }

    /**
     * 构建ecu固件版本信息
     * @param tempAggregationInfo
     * @param groupSeq
     * @param updateSeq
     * @return
     */
    public com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo buildEcuFirmwareVersionInfo(final TempAggregationInfo tempAggregationInfo, Integer groupSeq, Integer updateSeq){
        com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo ecuFirmwareVersionInfo = new com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo();
        ecuFirmwareVersionInfo.setGroupSeq(groupSeq);
        ecuFirmwareVersionInfo.setUpgradeSeq(updateSeq);
        ecuFirmwareVersionInfo.setFirmwareId(tempAggregationInfo.getFotaFirmwarePo().getId());
        ecuFirmwareVersionInfo.setFirmwareCode(tempAggregationInfo.getFotaFirmwarePo().getFirmwareCode());

        //设置
        ecuFirmwareVersionInfo.setSrcPkgInfo(buildPkgInfo(tempAggregationInfo.getFotaFirmwareVersionPo(), tempAggregationInfo.getFotaFirmwarePkgPo()));
        ecuFirmwareVersionInfo.setDstPkgInfo(buildPkgInfo(tempAggregationInfo.getTargetFotaFirmwareVersionPo(), tempAggregationInfo.getTargetFotaFirmwarePkgPo()));

        //TODO
        ecuFirmwareVersionInfo.setRollbackMode(tempAggregationInfo.getRollbackMode());
        ecuFirmwareVersionInfo.setEcuId(tempAggregationInfo.getFotaFirmwarePo().getComponentCode());
        ecuFirmwareVersionInfo.setDiagnose(tempAggregationInfo.getFotaFirmwarePo().getDiagnose());
        //暂时都设置为依赖其他项目的升级包
        ecuFirmwareVersionInfo.setDependenyFlag(1);
        return ecuFirmwareVersionInfo;
    }

    /**
     * 构建单个升级包对象
     * @param fotaFirmwareVersionPo
     * @param fotaFirmwarePkgPo
     * @return
     */
    public PkgInfo buildPkgInfo(FotaFirmwareVersionPo fotaFirmwareVersionPo, FotaFirmwarePkgPo fotaFirmwarePkgPo){
        if(Objects.nonNull(fotaFirmwarePkgPo)) {
            PkgInfo pkgInfo = new PkgInfo();
            pkgInfo.setDownloadUrl(MyBusinessUtil.relaceDownloadUrl(minIoConfig.getTboxPkgDownloadDomain(), fotaFirmwarePkgPo.getReleasePkgCdnDownloadUrl()));
            pkgInfo.setEncryptAlg(fotaFirmwarePkgPo.getReleasePkgEncryptAlg());
            pkgInfo.setEncryptSecret(fotaFirmwarePkgPo.getReleasePkgEncryptSecret());
            pkgInfo.setFileSize(fotaFirmwarePkgPo.getReleasePkgFileSize());
            pkgInfo.setOriginFileSize(fotaFirmwarePkgPo.getOriginalPkgSize());
            pkgInfo.setShaCode(StringUtils.lowerCase(fotaFirmwarePkgPo.getReleasePkgShaCode()));
            pkgInfo.setOriginShaCode(StringUtils.lowerCase(fotaFirmwarePkgPo.getOriginalPkgShaCode()));
            pkgInfo.setEstimatedUpgradeTime(fotaFirmwarePkgPo.getEstimateFlashTime());
            
            // 暂时使用解密密钥判断升级包是否已经加密
            Integer isSecret = Objects.isNull(fotaFirmwarePkgPo.getReleasePkgEncryptSecret()) ? 2: 1; // 是否是加密包：1是加密包 2否
            pkgInfo.setIsSecret(isSecret);
            pkgInfo.setPkgId(fotaFirmwarePkgPo.getId());
            pkgInfo.setPkgType(fotaFirmwarePkgPo.getPkgType());
            pkgInfo.setScriptUrl(MyBusinessUtil.relaceDownloadUrl(minIoConfig.getTboxPkgDownloadDomain(), fotaFirmwarePkgPo.getFlashScriptUrl())); // 填充刷写脚本
            pkgInfo.setSign(fotaFirmwarePkgPo.getReleasePkgSign());
            pkgInfo.setSignAlg(fotaFirmwarePkgPo.getReleasePkgSignAlg());
            pkgInfo.setFirmwareVersion(fotaFirmwareVersionPo.getFirmwareVersionNo());
            return pkgInfo;
        }
        return null;
    }
    
    /**
     * 填充固件刷写脚本
     * @param fotaFirmwarePkgPo
     * @param fotaFirmwarePo
     */
    public void paddingFotaFirmwarePkgPoFlashScript(FotaFirmwarePkgPo fotaFirmwarePkgPo, FotaFirmwarePo fotaFirmwarePo) {
    	String flashScriptUrl = fotaFirmwarePkgPo.getFlashScriptUrl();
    	if (StringUtils.isBlank(flashScriptUrl)) {
    		flashScriptUrl = fotaFirmwarePo.getFlashScriptUrl();
    		fotaFirmwarePkgPo.setFlashScriptUrl(flashScriptUrl);
    	}
    }

    /**
     * 升级对象中间类
     */
    @Data
    @Builder
    public static class TempAggregationInfo{
        /**
         * 客户端传递参数
         */
        private com.bnmotor.icv.adam.sdk.ota.domain.EcuModule ecuModule;
        /**
         * 对应固件信息
         */
        private FotaFirmwarePo fotaFirmwarePo;
        /**
         * 升级任务中的固件清单列表
         */
        @Deprecated
        private FotaPlanFirmwareListPo fotaPlanFirmwareListPo;
        
        private FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo;
        
        private FotaStrategyPo fotaStrategyPo;

        /**
         * 客户端当前固件版本
         */
        private FotaFirmwareVersionPo fotaFirmwareVersionPo;
        /**
         *  升级目标固件版本
         */
        private FotaFirmwareVersionPo targetFotaFirmwareVersionPo;
        /**
         *
         */
        private FotaFirmwarePkgPo targetFotaFirmwarePkgPo;

        /**
         *
         */
        private FotaFirmwarePkgPo fotaFirmwarePkgPo;

        /**
         * 当前任务细节
         */
        private FotaPlanTaskDetailPo fotaPlanTaskDetailPo;

        /**
         * 回滚模式
         */
        private Integer rollbackMode;

        /**
         * 是否有效
         */
        private boolean valid;
    }

    /**
     * 正常版本检查响应
     *
     * @param req
     * @param transId
     * @param taskId
     * @param otaDownVersionCheckResponse
     * @return
     */
    public OtaProtocol ok(OtaProtocol req, Long transId, Long taskId, OtaDownVersionCheckResponse otaDownVersionCheckResponse, TboxAdamException tboxAdamException){
        /*OtaProtocol resp = new OtaProtocol();
        resp.setOtaMessageHeader(OtaMessageMapper.INSTANCE.otaMessageHeader2OtaMessageHeader(req.getOtaMessageHeader()));

        resp.setBody(new OtaMessage());
        resp.getBody().setOtaCommonPayload(new OtaCommonPayload());
        resp.getBody().getOtaCommonPayload().setTaskId(taskId);
        resp.getBody().getOtaCommonPayload().setTransId(transId);
        //补充reqId
        resp.getBody().getOtaCommonPayload().setReqId(IdWorker.getId());
        resp.getBody().setBusinessId(req.getBody().getBusinessId());
        resp.getBody().setOtaDownVersionCheckResponse(otaDownVersionCheckResponse);
        resp.getBody().setBusinessType(com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_RESP.getType());
        resp.getBody().setTimestamp(Instant.now().toEpochMilli());
        log.info("响应新版本检查结果={}", resp.toString());*/

        Consumer<OtaProtocol> respConsumer = resp ->{
            resp.getBody().setOtaCommonPayload(new OtaCommonPayload());
            resp.getBody().getOtaCommonPayload().setTaskId(taskId);
            resp.getBody().getOtaCommonPayload().setTransId(transId);
            //补充reqId
            resp.getBody().getOtaCommonPayload().setReqId(IdWorker.getId());
            resp.getBody().getOtaCommonPayload().setErrorCode(200);

            if(Objects.nonNull(tboxAdamException)){
                resp.getBody().getOtaCommonPayload().setErrorCode(tboxAdamException.getCode());
                resp.getBody().getOtaCommonPayload().setErrorMsg(tboxAdamException.getMessage());
            }

            resp.getBody().setOtaDownVersionCheckResponse(otaDownVersionCheckResponse);
            log.info("响应新版本检查结果={}", resp.toString());
        };
        OtaProtocol resp = TBoxUtil.wrapTBoxUpBusiness(req, respConsumer, com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_RESP);
        return resp;
    }

    /**
     * 返回版本检查失败的响应
     * @param req
     * @return
     */
    @Override
    public OtaProtocol failCheckVersion(OtaProtocol req, TboxAdamException tboxAdamException){
        OtaDownVersionCheckResponse otaDownVersionCheckResponse = new OtaDownVersionCheckResponse();
        otaDownVersionCheckResponse.setCheckResult(AppEnums.CheckVersionResultEnum.NO_VERSION.getType());
        OtaProtocol resp = ok(req, 0L, 0L, otaDownVersionCheckResponse, tboxAdamException);

        /*OtaUpVersionCheck otaUpVersionCheck = req.getBody().getOtaUpVersionCheck();
        OtaCommonPayload otaCommonPayload = req.getBody().getOtaCommonPayload();
        //如果版本检查请求来自于APP,且检查失败，此处直接推送回APP
        if(Objects.nonNull(otaUpVersionCheck) && Objects.nonNull(otaCommonPayload.getSourceType()) && otaCommonPayload.getSourceType() == AppEnums.CheckVersionSourceTypeEnum.FROM_APP.getType()) {
            String vin = req.getOtaMessageHeader().getVin();
            Supplier<FotaObjectPo> fotaObjectDoSupplier = () -> fotaObjectDbService.findByObjectId(vin);
            //TODO 此处定义需要
            commonExecutor.execute(()-> {
                //TODO
                OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage = OtaUpgradeNewVersionMessage.builder().newVersion(false).otaProtocol(req).transId(null).reqId(0L).fotaObjectPoSupplier(fotaObjectDoSupplier).build();
                OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().data(otaUpgradeNewVersionMessage).otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.NEW_VERSION.getType()).data(otaUpgradeNewVersionMessage).build();
                otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_UPGRADE_MESSAGE, otaUpgradeMessageBase);
            }, "推送版本检查无新版本消息到APP");
        }else{
            log.warn("版本检查失败，版本检查参数异常或来源不为APP,不给APP推送消息.");
        }*/
        failCheckVersion4App(req);
        return resp;
    }

    public void failCheckVersion4App(OtaProtocol req){
        OtaUpVersionCheck otaUpVersionCheck = req.getBody().getOtaUpVersionCheck();
        OtaCommonPayload otaCommonPayload = req.getBody().getOtaCommonPayload();
        //如果版本检查请求来自于APP,且检查失败，此处直接推送回APP
        if(Objects.nonNull(otaUpVersionCheck) && Objects.nonNull(otaCommonPayload.getSourceType()) && otaCommonPayload.getSourceType() == AppEnums.ClientSourceTypeEnum.FROM_APP.getType()) {
            String vin = req.getOtaMessageHeader().getVin();
            Supplier<FotaObjectPo> fotaObjectDoSupplier = () -> fotaObjectDbService.findByObjectId(vin);
            //TODO 此处定义需要
            commonExecutor.execute(()-> {
                //TODO
                OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage = OtaUpgradeNewVersionMessage.builder().newVersion(false).otaProtocol(req).transId(null).reqId(0L).fotaObjectPoSupplier(fotaObjectDoSupplier).build();
                OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().data(otaUpgradeNewVersionMessage).otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.NEW_VERSION.getType()).data(otaUpgradeNewVersionMessage).vin(vin).build();
                otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_UPGRADE_MESSAGE, otaUpgradeMessageBase);
            }, "推送版本检查无新版本消息到APP");
        }else{
            log.warn("版本检查失败，版本检查参数异常或来源不为APP,不给APP推送消息.");
        }
    }

    @Override
    public OtaProtocol newVersionFromOta(String vin) {
        log.info("来自云端的升级通知");
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_FROM_OTA, vin, null, IdWorker.getId());
        boxDownHandler.send(otaProtocol);
        return otaProtocol;
    }
    
//    public List<TempAggregationInfo> assemblyAggregateFirmwareInfoList(List<FotaFirmwarePo> fotaFirmwareList, List<EcuModule> ecuModuleList, List<FotaPlanFirmwareListPo> fotaPlanFirmwareListList) {
//
//		Map<String, EcuModule> ecuModuleMap = ecuModuleList.stream().collect(Collectors.toMap(ecuModuleMapper(), Function.identity(), (k1, k2) -> k1));
//		Set<String> ecukeyset = ecuModuleMap.keySet();
//
//		Map<String, FotaFirmwarePo> fotaFirmwareMap = fotaFirmwareList.stream().collect(Collectors.toMap(fotaFirmwareMapper(), Function.identity(), (k1, k2) -> k1));
//		Set<String> firmwarekeyset = fotaFirmwareMap.keySet();
//
//		Map<Long, FotaPlanFirmwareListPo> fotaPlanFirmwareListMap = fotaPlanFirmwareListList.stream().collect(Collectors.toMap(FotaPlanFirmwareListPo::getFirmwareId, Function.identity(), (k1, k2) -> k1));
//		List<String> theSameKey = intersection(ecukeyset, firmwarekeyset);
//		List<TempAggregationInfo> aggregateFirmwareInfoList = theSameKey.stream().map(tempAggregationInfoMapper(fotaFirmwareMap, ecuModuleMap, fotaPlanFirmwareListMap)).collect(Collectors.toList());
//
//		return aggregateFirmwareInfoList.stream().filter(it -> it.isValid()).collect(Collectors.toList());
//	}
    
    public <T> List<T> intersection(Set<T> t1, Set<T> t2) {
		return t1.stream().filter(i -> t2.contains(i)).distinct().collect(Collectors.toList());
	}
    
    public Function<FotaFirmwarePo, String> fotaFirmwareMapper() {
		return (fotaFirmware) -> padding(fotaFirmware.getFirmwareCode(), fotaFirmware.getDiagnose(), fotaFirmware.getComponentCode());
	}
    
    public Function<EcuModule, String> ecuModuleMapper() {
		return (ecuModule) -> padding(ecuModule.getFirmwareCode(), ecuModule.getDiagnose(), ecuModule.getEcuId());
	}
    
//    public Function<String , TempAggregationInfo> tempAggregationInfoMapper(Map<String, FotaFirmwarePo> fotaFirmwareMap, Map<String, EcuModule> ecuModuleMap, Map<Long, FotaPlanFirmwareListPo> fotaPlanFirmwareListMap) {
//    	Function<String, TempAggregationInfo> func = (key) -> {
//	    	FotaFirmwarePo firmware = fotaFirmwareMap.get(key);
//			EcuModule ecumodule = ecuModuleMap.get(key);
//			Long firmwareId = firmware.getId();
//			FotaPlanFirmwareListPo fotaPlanFirmwareList = fotaPlanFirmwareListMap.get(firmwareId);
//			return TempAggregationInfo.builder().ecuModule(ecumodule).fotaFirmwarePo(firmware).fotaPlanFirmwareListPo(fotaPlanFirmwareList).valid(Objects.nonNull(fotaPlanFirmwareList)).build();
//    	};
//    	return func;
//    }

    public String padding(String... str) {
		return StringUtils.join(str, "#");
	}

	public abstract OtaProtocol checkVersion(OtaProtocol otaProtocol);
	
	@PostConstruct
	public void afterInitialize() {
		
	}

}