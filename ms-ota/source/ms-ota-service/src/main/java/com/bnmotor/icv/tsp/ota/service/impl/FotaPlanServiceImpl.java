package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.RebuildFlagEnum;
import com.bnmotor.icv.tsp.ota.mapper.*;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanQuery;
import com.bnmotor.icv.tsp.ota.model.req.FotaPlanReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareReq;
import com.bnmotor.icv.tsp.ota.model.resp.*;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author xxc
 * @ClassName: FotaPlanPo
 * @Description: OTA升级计划表 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-07
 */

@Slf4j
@Service("fotaPlanService")
public class FotaPlanServiceImpl implements IFotaPlanService {
    @Autowired
    private IUpgradeTaskConditionDbService upgradeTaskConditionDbService;

    @Autowired
    private IUpgradeStrategyDbService upgradeStrategyDbService;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaPlanTaskDetailDbService planTaskDetailDbService;

    @Autowired
    private IFotaPlanObjListDbService planObjListDbService;

    @Autowired
    private IFotaPlanFirmwareListService fotaPlanFirmwareListService;

    @Autowired
    private IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

    @Autowired
    private ITaskTerminateDbService taskTerminateDbService;

    @Autowired
    private IFotaPlanObjListService fotaPlanObjListService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    @Override
    public IPage<FotaPlanVo> queryPage(FotaPlanQuery query) {
    	// 查询V1版本任务
    	query.setRebuildFlag(RebuildFlagEnum.V1.getFlag());
        IPage<FotaPlanPo> fotaPlanPos = fotaPlanDbService.queryPage(query);
        IPage<FotaPlanVo> fotaPlanVos = new Page<>(fotaPlanPos.getCurrent(), fotaPlanPos.getSize(), fotaPlanPos.getTotal());
        fotaPlanVos.setTotal(fotaPlanPos.getTotal());
        if (MyCollectionUtil.isNotEmpty(fotaPlanPos.getRecords())) {
            LocalDateTime now = LocalDateTime.now();
            List<FotaPlanVo> vos= fotaPlanPos.getRecords().stream().map(item -> {
                item.setIdStr(Long.toString(item.getId()));
                item.setName(item.getPlanName());
//                item.setStartTime(item.getPlanStartTime());
//                item.setFinishTime(item.getPlanEndTime());

                item.setTaskStatus(item.getPublishState());

                if(now.isAfter(MyDateUtil.uDateToLocalDateTime(item.getPlanEndTime()))){
                    //历史原因，多写了一个字段
                    item.setPublishState(PublishStateEnum.IN_PUBLISHING.getState());
                    item.setPlanStatus(item.getPublishState());
                    item.setTaskStatus(item.getPublishState());
                }
                FotaPlanVo vo=new FotaPlanVo();
                BeanUtils.copyProperties(item, vo);
                if(fotaPlanFirmwareListService.isFirmwarePkgBuildedStatus(item.getId())) {
                    vo.setIsEnableOfStart(Enums.ZeroOrOneEnum.ONE.getValue());
                }else{
                    vo.setIsEnableOfStart(Enums.ZeroOrOneEnum.ZERO.getValue());
                }
                return vo;
            }).collect(Collectors.toList());
            fotaPlanVos.setRecords(vos);
        }
        return fotaPlanVos;
    }

    @Override
    public FotaPlanDetailVo getFotaPlanDetailVoById(Long planId) {
        FotaPlanPo fotaPlanPo = fotaPlanDbService.getBaseMapper().selectById(planId);

        if(Objects.nonNull(fotaPlanPo.getObjectParentId())) {
            DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(fotaPlanPo.getObjectParentId());
            fotaPlanPo.setNodeNamePath(deviceTreeNodePo.getNodeNamePath());
        }

        fotaPlanPo.setIdStr(Long.toString(fotaPlanPo.getId()));
        List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.listByOtaPlanId(planId);
        //如果已经选择了车辆信息
        if(MyCollectionUtil.isNotEmpty(fotaPlanObjListPos)) {
            List<FotaObjectPo> fotaObjectPos = fotaObjectDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListPos, item -> item.getOtaObjectId()));
            Map<Long, FotaObjectPo> fotaObjectDoMap = fotaObjectPos.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
            //补充currentArea和saleArea属性字段
            fotaPlanObjListPos.forEach(item -> {
                FotaObjectPo fotaObjectPo = fotaObjectDoMap.get(item.getOtaObjectId());
                if (Objects.nonNull(fotaObjectPo)) {
                    item.setCurrentArea(fotaObjectPo.getCurrentArea());
                    item.setSaleArea(fotaObjectPo.getSaleArea());
                }
            });
        }

        //包装升级ecu列表
        List<FotaPlanFirmwareListVo> fotaPlanFirmwareLists = null;
        /*QueryWrapper<FotaPlanFirmwareListPo> fotaPlanFirmwareListPoQueryWrapper = new QueryWrapper<>();
        fotaPlanFirmwareListPoQueryWrapper.eq("plan_id", planId);*/
        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListDbService.listByOtaPlanId(planId);
        if(MyCollectionUtil.isNotEmpty(fotaPlanFirmwareListPos)) {
            fotaPlanFirmwareLists = fotaPlanFirmwareListPos.stream().map(fotaPlanFirmwareListPo -> {
                FotaPlanFirmwareListVo fotaPlanFirmwareListVo = new FotaPlanFirmwareListVo();
                BeanUtils.copyProperties(fotaPlanFirmwareListPo, fotaPlanFirmwareListVo);
                fotaPlanFirmwareListVo.setTaskId(fotaPlanFirmwareListPo.getPlanId());

                FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(fotaPlanFirmwareListPo.getFirmwareId());
                BeanUtils.copyProperties(fotaFirmwarePo, fotaPlanFirmwareListVo);
                fotaPlanFirmwareListVo.setFirmwareId(Long.toString(fotaFirmwarePo.getId()));
                fotaPlanFirmwareListVo.setEcuId(Long.toString(fotaFirmwarePo.getTreeNodeId()));
                fotaPlanFirmwareListVo.setFirmwareVersionId(Long.toString(fotaPlanFirmwareListPo.getFirmwareVersionId()));

                FotaFirmwareVersionPo fotaFirmwareVersion = fotaFirmwareVersionDbService.getById(fotaPlanFirmwareListPo.getFirmwareVersionId());
                fotaPlanFirmwareListVo.setFirmwareVersion(fotaFirmwareVersion.getFirmwareVersionNo());

                /*QueryWrapper<UpgradeStrategyPo> upgradeStrategyDoQueryWrapper = new QueryWrapper<UpgradeStrategyPo>();
                upgradeStrategyDoQueryWrapper.eq("ota_plan_id", fotaPlanFirmwareListPo.getPlanId());
                upgradeStrategyDoQueryWrapper.eq("firmware_id", fotaPlanFirmwareListPo.getFirmwareId());
                upgradeStrategyDoQueryWrapper.eq("firmware_version_id", fotaPlanFirmwareListPo.getFirmwareVersionId());
                UpgradeStrategyPo upgradeStrategyPo = upgradeStrategyDbService.getOne(upgradeStrategyDoQueryWrapper);*/

                UpgradeStrategyPo upgradeStrategyPo = upgradeStrategyDbService.findOne(fotaPlanFirmwareListPo.getPlanId(), fotaPlanFirmwareListPo.getFirmwareId(), fotaPlanFirmwareListPo.getFirmwareVersionId());

                if(Objects.nonNull(upgradeStrategyPo)) {
                    fotaPlanFirmwareListVo.setMaxConcurrent(upgradeStrategyPo.getMaxConcurrent());
                    fotaPlanFirmwareListVo.setRollbackMode(upgradeStrategyPo.getRollbackMode());
                }
                return fotaPlanFirmwareListVo;
            }).sorted(Comparator.comparing(FotaPlanFirmwareListVo::getUpgradeSeq)).collect(Collectors.toList());
        }

        //设置返回对象列表信息
        FotaPlanDetailVo fotaPlanDetailVO = new FotaPlanDetailVo();
        fotaPlanDetailVO.setFotaPlanFirmwareLists(fotaPlanFirmwareLists);
        List<FotaPlanObjListVo> fotaPlanObjListVos = MyCollectionUtil.map2NewList(fotaPlanObjListPos, item ->{
            FotaPlanObjListVo fotaPlanObjListVo = new FotaPlanObjListVo();
            BeanUtils.copyProperties(item, fotaPlanObjListVo);
            fotaPlanObjListVo.setOtaPlanId(Long.toString(item.getOtaPlanId()));
            fotaPlanObjListVo.setOtaObjectId(Long.toString(item.getOtaObjectId()));
            fotaPlanObjListVo.setObjectId(fotaPlanObjListVo.getOtaObjectId());
            return fotaPlanObjListVo;
        });
        fotaPlanDetailVO.setFotaPlanObjLists(fotaPlanObjListVos);

        fotaPlanPo.setIdStr(Long.toString(fotaPlanPo.getId()));
        fotaPlanPo.setName(fotaPlanPo.getPlanName());
        fotaPlanPo.setStartTime(fotaPlanPo.getPlanStartTime());
        fotaPlanPo.setFinishTime(fotaPlanPo.getPlanEndTime());
        fotaPlanDetailVO.setFotaPlanTaskDetail(fotaPlanPo);

        fotaPlanDetailVO.setFotaUpgradeConditionList(((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).queryByOtaPlanId(planId));
        fotaPlanDetailVO.setTaskTerminate(taskTerminateDbService.findByOtaPlanId(planId));
        return fotaPlanDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insertFotaPlan(FotaPlanReq fotaPlanReq) {
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        BeanUtils.copyProperties(fotaPlanReq, fotaPlanPo);
        LocalDateTime now = LocalDateTime.now();
        fotaPlanPo.setCreateTime(now);
        fotaPlanPo.setUpdateTime(now);
        fotaPlanPo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
        fotaPlanPo.setVersion(0);
        fotaPlanPo.setTargetVersion(fotaPlanReq.getTargetVersion());
        fotaPlanPo.setPublishState(PublishStateEnum.UNPUBLISH.getState());
        fotaPlanPo.setRebuildFlag(RebuildFlagEnum.V1.getFlag());

        /*
        因为一些历史原因，下面的写法有些恶心
         */
        fotaPlanPo.setPlanName(fotaPlanReq.getName());
        fotaPlanPo.setPlanStartTime(fotaPlanReq.getStartTime());
        fotaPlanPo.setPlanEndTime(fotaPlanReq.getFinishTime());
        fotaPlanPo.setPlanDesc(fotaPlanReq.getNewVersionTips());

        boolean status = fotaPlanDbService.save(fotaPlanPo);
        if (!status) {
            log.error("任务基础信息写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }

        //添加终止条件记录
        saveTaskTerminate(fotaPlanReq, fotaPlanPo);
        return fotaPlanPo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFotaPlan(FotaPlanReq fotaPlanReq) {
        if (Objects.isNull(fotaPlanReq.getId()) || fotaPlanReq.getId() <= 0) {
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
        }
        if (Objects.nonNull(fotaPlanReq.getIsEnable()) && fotaPlanReq.getIsEnable() > 0) {
            FotaPlanPo existFotaPlanPo = fotaPlanDbService.getById(fotaPlanReq.getId());
            if(PublishStateEnum.inPublishState(existFotaPlanPo.getPublishState())){
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_INVALID_NOT_ALLOWED_ENABLED);
            }

            // 启用时候判断firmwarePkg状态
            if(!fotaPlanFirmwareListService.isFirmwarePkgBuildedStatus(fotaPlanReq.getId())){
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.BUILD_DATA_NOT_FINISH);
            }
        }
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        BeanUtils.copyProperties(fotaPlanReq, fotaPlanPo);

        //MyAssertUtil.notNull(fotaPlanReq.getObjectParentId(), "参数异常");
        if(Objects.nonNull(fotaPlanReq.getObjectParentId())) {
            fotaPlanPo.setObjectParentId(Long.toString(fotaPlanReq.getObjectParentId()));
        }
        fotaPlanPo.setPlanName(fotaPlanReq.getName());
        fotaPlanPo.setPlanStartTime(fotaPlanReq.getStartTime());
        fotaPlanPo.setPlanEndTime(fotaPlanReq.getFinishTime());
        fotaPlanPo.setTargetVersion(fotaPlanReq.getTargetVersion());
        LocalDateTime now = LocalDateTime.now();
        fotaPlanPo.setCreateBy(null);
        fotaPlanPo.setUpdateBy(fotaPlanReq.getUpdateBy());
        if(Objects.isNull(fotaPlanReq.getUpdateBy())){
            fotaPlanPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        }
        fotaPlanPo.setUpdateTime(now);
        fotaPlanPo.setRebuildFlag(RebuildFlagEnum.V1.getFlag());
        boolean update = fotaPlanDbService.updateById(fotaPlanPo);
        if (!update) {
            log.error("任务基础信息更新写入失败. 任务ID : [ {} ]", fotaPlanReq.getId());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }

        //判断是否需要更新终止条件
        boolean doUpdateTerminate = true;
        if (Objects.isNull(fotaPlanReq.getMaxDownloadFailed()) && Objects.isNull(fotaPlanReq.getMaxInstallFailed()) && Objects.isNull(fotaPlanReq.getMaxVerifyFailed())) {
            doUpdateTerminate = false;
        }
        if (doUpdateTerminate) {
            TaskTerminatePo taskTerminatePo = new TaskTerminatePo();
            BeanUtils.copyProperties(fotaPlanReq, taskTerminatePo);
            TaskTerminatePo existTaskTerminate = taskTerminateDbService.findByOtaPlanId(fotaPlanReq.getId());
            taskTerminatePo.setId(existTaskTerminate.getId());
            taskTerminatePo.setOtaPlanId(existTaskTerminate.getOtaPlanId());
            update = taskTerminateDbService.updateById(taskTerminatePo);
            if (!update) {
                log.error("任务终止条件信息更新写入失败. 任务ID : [ {} ]", fotaPlanReq.getId());
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
            }
        }

        //补充更新任务缓存对象
        fotaObjectCacheInfoService.setFotaPlanCacheInfo(fotaPlanDbService.getById(fotaPlanPo.getId()));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(Long planId) {
        FotaPlanPo fotaPlanPo = fotaPlanDbService.getBaseMapper().selectById(planId);
        //TODO 魔数
        if (fotaPlanPo.getPublishState() > PublishStateEnum.IN_PUBLISHING.getState()) {
            log.warn("当前任务待发布状态，不允许删除");
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_CANT_NOT_DELETE);
        }
        //实际是逻辑删除
        ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((FotaPlanFirmwareListMapper)fotaPlanFirmwareListDbService.getBaseMapper()).deleteByPlanIdPhysical(planId);
        ((FotaPlanTaskDetailMapper)planTaskDetailDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((TaskTerminateMapper)taskTerminateDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(planId);
        return ((FotaPlanMapper)fotaPlanDbService.getBaseMapper()).deleteByIdPhysical(planId);
    }

    @Override
    public boolean existPlanWithFirmware(Long firmwareId, Long firmwareVersionId) {
        if (Objects.isNull(firmwareId) || Objects.isNull(firmwareVersionId)) {
            return false;
        }
         List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListDbService.list(firmwareId, firmwareVersionId);
        return MyCollectionUtil.isNotEmpty(fotaPlanFirmwareListPos) && Objects.nonNull(fotaPlanFirmwareListPos.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddFotaPlanResultVo addFotaPlan(FotaPlanReq fotaPlanReq) {
        List<ExistValidPlanObjVo> existValidPlanObjVos = fotaPlanObjListService.validate4AddFotaPlan(fotaPlanReq.getUpgradeTaskObjectList());
        if(MyCollectionUtil.isNotEmpty(existValidPlanObjVos)){
            log.warn("选择的车辆已经在有效的升级任务中，不能继续选择该车辆。existValidPlanObjs={}", existValidPlanObjVos.toString());
            return AddFotaPlanResultVo.builder().result(0).msg(existValidPlanObjVos.toString()).existValidPlanObjVos(existValidPlanObjVos).build();
        }

        MyAssertUtil.notNull(fotaPlanReq.getObjectParentId(), "设备树节点信息为空");
        MyAssertUtil.notEmpty(fotaPlanReq.getUpgradeTaskObjectList(), "车辆列表参数不能为空");
        MyAssertUtil.notEmpty(fotaPlanReq.getUpgradeFirmwareList(), "ECU列表参数不能为空");
        MyAssertUtil.notEmpty(fotaPlanReq.getUpgradeConditionList(), "前置条件列表参数不能为空");
        // 数据是否存在
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(fotaPlanReq.getObjectParentId());
        MyAssertUtil.notNull(deviceTreeNodePo, "设备树节点信息有误");

        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        BeanUtils.copyProperties(fotaPlanReq, fotaPlanPo);
        fotaPlanPo.setId(IdWorker.getId());
        LocalDateTime now = LocalDateTime.now();
        fotaPlanPo.setPublishState(PublishStateEnum.UNPUBLISH.getState()/*Enums.PlanStatusTypeEnum.CREATED.getType()*/);
        fotaPlanPo.setProjectId(CommonUtil.buildProjectId(fotaPlanReq.getProjectId()));
        fotaPlanPo.setTargetVersion(fotaPlanReq.getTargetVersion());
        fotaPlanPo.setPlanName(fotaPlanReq.getName());
        fotaPlanPo.setPlanStartTime(fotaPlanReq.getStartTime());
        fotaPlanPo.setPlanEndTime(fotaPlanReq.getFinishTime());
        fotaPlanPo.setPlanDesc(fotaPlanReq.getNewVersionTips());
        fotaPlanPo.setCreateTime(now);
        boolean status = fotaPlanDbService.save(fotaPlanPo);
        if (!status) {
            log.error("任务基础信息写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }
        //添加升级任务对象列表
        savePlanObjLists(fotaPlanReq, fotaPlanPo);

        //添加任务升级的ecu列表(包括策略)
        savePlanFirmwareLists(fotaPlanReq, fotaPlanPo);

        //补充添加任务前置条件
        saveUpgradeTaskConditions(fotaPlanReq, fotaPlanPo);

        //插入终止条件
        saveTaskTerminate(fotaPlanReq, fotaPlanPo);

        return AddFotaPlanResultVo.builder().otaPlanId(Long.toString(fotaPlanPo.getId())).build();
    }

    /**
     * 添加任务对象列表
     * @param fotaPlanReq
     * @param fotaPlanPo
     */
    private void savePlanObjLists(final FotaPlanReq fotaPlanReq, final FotaPlanPo fotaPlanPo){
        List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanReq.getUpgradeTaskObjectList().stream().map(
            item -> {
                FotaObjectPo exitFotaObjectPo = fotaObjectDbService.getById(item.getOtaObjectId());
                FotaPlanObjListPo planObjListDo = new FotaPlanObjListPo();
                planObjListDo.setId(IdWorker.getId());
                planObjListDo.setOtaPlanId(fotaPlanPo.getId());
                planObjListDo.setOtaObjectId(item.getOtaObjectId());
                planObjListDo.setSourceVersion(exitFotaObjectPo.getCurrentVersion());
                planObjListDo.setCurrentVersion(exitFotaObjectPo.getCurrentVersion());
                planObjListDo.setTargetVersion(fotaPlanReq.getTargetVersion());
                planObjListDo.setResult(0);
                planObjListDo.setCreateTime(fotaPlanPo.getCreateTime());
                return planObjListDo;
            }
        ).collect(Collectors.toList());
        boolean saveBatch = planObjListDbService.saveBatch(fotaPlanObjListPos);
        if (!saveBatch) {
            log.error("任务升级对象信息写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }

        //添加升级车辆对象缓存
        fotaPlanObjListPos.forEach(item ->{
            FotaObjectPo fotaObjectPo = fotaObjectDbService.getById(item.getOtaObjectId());
            if(Objects.isNull(fotaObjectPo)){
                log.warn("升级车辆信息异常.item={}", item);
            }
            FotaVinCacheInfo fotaVinCacheInfo = new FotaVinCacheInfo();
            fotaVinCacheInfo.setVin(fotaObjectPo.getObjectId());
            fotaVinCacheInfo.setObjectId(fotaObjectPo.getId());
            fotaVinCacheInfo.setOtaPlanId(item.getOtaPlanId());
            fotaVinCacheInfo.setOtaPlanObjectId(item.getId());
            fotaObjectCacheInfoService.setFotaVinCacheInfo(fotaVinCacheInfo);
        });
    }

    /**
     * 添加任务升级的ecu列表
     * @param fotaPlanReq
     * @param fotaPlanPo
     */
    private void savePlanFirmwareLists(final FotaPlanReq fotaPlanReq, final FotaPlanPo fotaPlanPo){
        List<UpgradeFirmwareReq> upgradeFirmwareList = fotaPlanReq.getUpgradeFirmwareList();
        List<FotaPlanFirmwareListPo> planFirmwareListDos = Lists.newArrayList();
        List<UpgradeStrategyPo> upgradeStrategyPos = Lists.newArrayList();
        upgradeFirmwareList.forEach(
                item -> {
                    FotaPlanFirmwareListPo planFirmwareListDo = new FotaPlanFirmwareListPo();
                    planFirmwareListDo.setId(IdWorker.getId());
                    planFirmwareListDo.setProjectId(fotaPlanPo.getProjectId());
                    planFirmwareListDo.setFirmwareId(item.getFirmwareId());
                    planFirmwareListDo.setFirmwareVersionId(item.getFirmwareVersionId());
                    planFirmwareListDo.setUpgradeSeq(item.getUpgradeSeq());
                    planFirmwareListDo.setCreateTime(fotaPlanPo.getCreateTime());
                    planFirmwareListDo.setCreateBy(fotaPlanPo.getCreateBy());
                    planFirmwareListDo.setPlanId(fotaPlanPo.getId());
                    planFirmwareListDo.setRollbackMode(item.getRollbackMode());
                    planFirmwareListDos.add(planFirmwareListDo);

                    UpgradeStrategyPo upgradeStrategyPo = new UpgradeStrategyPo();
                    upgradeStrategyPo.setFirmwareId(item.getFirmwareId());
                    upgradeStrategyPo.setFirmwareVersionId(item.getFirmwareVersionId());
                    upgradeStrategyPo.setMaxConcurrent(item.getMaxConcurrent());
                    upgradeStrategyPo.setRollbackMode(item.getRollbackMode());
                    upgradeStrategyPo.setOtaPlanId(fotaPlanPo.getId());
                    upgradeStrategyPo.setCreateBy(fotaPlanPo.getCreateBy());
                    upgradeStrategyPos.add(upgradeStrategyPo);
                }
        );
        boolean saveBatch = fotaPlanFirmwareListDbService.saveBatch(planFirmwareListDos);
        if (!saveBatch) {
            log.error("任务固件信息更新写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }

        int status = ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).saveAllInBatch(upgradeStrategyPos);
        if (status < 1) {
            log.error("任务策略信息更新写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }
    }

    /**
     * 添加策略
     * @param fotaPlanReq
     * @param fotaPlanPo
     */
    private void saveUpgradeStrategys(final FotaPlanReq fotaPlanReq, final FotaPlanPo fotaPlanPo){
        List<UpgradeStrategyPo> upgradeStrategyPos = fotaPlanReq.getUpgradeFirmwareList().stream().map(
                item -> {
                    UpgradeStrategyPo upgradeStrategyPo = new UpgradeStrategyPo();
                    upgradeStrategyPo.setFirmwareId(item.getFirmwareId());
                    upgradeStrategyPo.setFirmwareVersionId(item.getFirmwareVersionId());
                    upgradeStrategyPo.setMaxConcurrent(item.getMaxConcurrent());
                    upgradeStrategyPo.setRollbackMode(item.getRollbackMode());
                    upgradeStrategyPo.setOtaPlanId(fotaPlanPo.getId());
                    upgradeStrategyPo.setCreateBy(fotaPlanPo.getCreateBy());
                    return upgradeStrategyPo;
                }
        ).collect(Collectors.toList());
        int status1 = ((UpgradeStrategyMapper) upgradeStrategyDbService.getBaseMapper()).saveAllInBatch(upgradeStrategyPos);
        if (status1 < 1) {
            log.error("任务策略信息更新写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }
    }

    /**
     * 补充添加任务前置条件
     * @param fotaPlanReq
     * @param fotaPlanPo
     */
    private void saveUpgradeTaskConditions(final FotaPlanReq fotaPlanReq, final FotaPlanPo fotaPlanPo){
        //更新前置条件检查
        List<UpgradeTaskConditionPo> upgradeTaskConditionPos = fotaPlanReq.getUpgradeConditionList().stream().map(item -> {
            item.setProjectId(fotaPlanPo.getProjectId());
            item.setOtaPlanId(fotaPlanPo.getId());
            UpgradeTaskConditionPo upgradeTaskConditionPo = new UpgradeTaskConditionPo();
            BeanUtils.copyProperties(item, upgradeTaskConditionPo);
            return upgradeTaskConditionPo;
        }).collect(Collectors.toList());
        int saveAllInBatch = ((UpgradeTaskConditionMapper)upgradeTaskConditionDbService.getBaseMapper()).saveAllInBatch(upgradeTaskConditionPos);
        if (saveAllInBatch < 1) {
            log.error("任务前置信息更新写入失败,任务基本信息：[ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }
    }

    /**
     * 补充添加终止条件
     * @param fotaPlanReq
     * @param fotaPlanPo
     */
    private void saveTaskTerminate(final FotaPlanReq fotaPlanReq, final FotaPlanPo fotaPlanPo){
        //插入终止条件
        TaskTerminatePo taskTerminate = new TaskTerminatePo();
        BeanUtils.copyProperties(fotaPlanReq, taskTerminate);
        taskTerminate.setId(IdWorker.getId());
        taskTerminate.setOtaPlanId(fotaPlanPo.getId());
        boolean save = taskTerminateDbService.save(taskTerminate);
        if (!save) {
            log.error("任务终止条件信息写入失败. 任务基本信息 : [ {} ]", fotaPlanReq.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_SAVE_FAIL);
        }
    }

    @Override
    public boolean setInvalid(Long otaPlanId) {
        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        fotaPlanPo.setId(otaPlanId);
        fotaPlanPo.setPublishState(PublishStateEnum.INVALID.getState());
        return fotaPlanDbService.updateById(fotaPlanPo);
    }
}
