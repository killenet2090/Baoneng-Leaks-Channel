package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanObjListMapper;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.*;
import com.bnmotor.icv.tsp.ota.model.resp.*;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xxc
 * @ClassName: FotaPlanObjListServiceImpl
 * @Description: OTA升级计划对象清单定义一次升级中包含哪些需要升级的车辆 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-17
 */

@Service
@Slf4j
public class FotaPlanObjListServiceImpl implements IFotaPlanObjListService {
    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IUpgradeStrategyDbService upgradeStrategyDbService;

    @Autowired
    private IFotaPlanFirmwareListService fotaPlanFirmwareListService;

    @Autowired
    private IFotaPlanFirmwareListDbService fotaPlanFirmwareListDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    /*@Override
    public FotaPlanObjListPo findOneByObjectId(Long objectId) {
        if (Objects.isNull(objectId)) {
            log.warn("参数异常.objectId={}", objectId);
            return null;
        }
        //按照创建时间降序排列，获取最新的升级任务对象
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<FotaPlanObjListPo>();
        queryWrapper.eq("ota_object_id", objectId);
        queryWrapper.orderByDesc("create_time");
        List<FotaPlanObjListPo> fotaPlanObjListPos = list(queryWrapper);
        return (MyCollectionUtil.isNotEmpty(fotaPlanObjListPos)) ? fotaPlanObjListPos.get(0) : null;
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddFotaPlanResultVo updateFotaPlanObjList(UpgradeObjectListReq upgradeObjectListReq) {
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
    public AddFotaPlanResultVo insertUpgradeTaskObjectList(UpgradeObjectListReq upgradeObjectListReq) {
        List<UpgradeTaskObjectReq> upgradeTaskObjectReqList = upgradeObjectListReq.getUpgradeTaskObjectReqList();
        if (MyCollectionUtil.isEmpty(upgradeTaskObjectReqList)) {
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
        }

        //历史原因，添加以下补偿代码
        LocalDateTime now = LocalDateTime.now();
        Long otaPlanId = upgradeTaskObjectReqList.get(0).getTaskId();
        List<FotaPlanObjListPo> fotaPlanObjListPos = Lists.newArrayList();
        FotaPlanPo existFotaPlanPo = fotaPlanDbService.getById(otaPlanId);
        upgradeTaskObjectReqList.forEach(item -> {
            MyAssertUtil.notNull(item.getObjectId(), OTARespCodeEnum.DATA_NOT_FOUND);
            //历史原因，需要转
            item.setOtaObjectId(item.getObjectId());
            item.setOtaPlanId(item.getTaskId());

            FotaPlanObjListPo objListDo = new FotaPlanObjListPo();
            objListDo.setId(IdWorker.getId());
            objListDo.setOtaPlanId(item.getTaskId());
            objListDo.setOtaObjectId(item.getObjectId());
            objListDo.setVin(item.getVin());
            objListDo.setCurrentArea(item.getCurrentArea());
            objListDo.setSaleArea(item.getSaleArea());
            objListDo.setVersion(item.getVersion());
            objListDo.setDelFlag(item.getDelFlag());
            objListDo.setResult(0);

            objListDo.setTargetVersion(existFotaPlanPo.getTargetVersion());

            FotaObjectPo exitFotaObjectPo = fotaObjectDbService.getById(item.getObjectId());
            objListDo.setSourceVersion(exitFotaObjectPo.getCurrentVersion());
            objListDo.setCurrentVersion(exitFotaObjectPo.getCurrentVersion());

            String userId = Objects.nonNull(item.getCreateBy()) ? item.getCreateBy() : Objects.nonNull(upgradeObjectListReq.getCreateBy()) ? upgradeObjectListReq.getCreateBy() : CommonConstant.USER_ID_SYSTEM;
            CommonUtil.wrapBasePo(objListDo, userId, now, true);
            fotaPlanObjListPos.add(objListDo);
        });
        List<ExistValidPlanObjVo> existValidPlanObjVos = validate4AddFotaPlan(upgradeTaskObjectReqList);
        if (MyCollectionUtil.isNotEmpty(existValidPlanObjVos)) {
            log.warn("选择的车辆已经在有效的升级任务中，不能继续选择该车辆。existValidPlanObjs={}", existValidPlanObjVos.toString());
            return AddFotaPlanResultVo.builder().msg("选择的车辆已经在有效的升级任务中，不能继续选择该车辆").result(0).existValidPlanObjVos(existValidPlanObjVos).build();
        }

        //删除可能存在的升级车辆列表
        /*QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", otaPlanId);
        List<FotaPlanObjListPo> existFotaPlanObjListPos = fotaPlanObjListDbService.getBaseMapper().selectList(queryWrapper);
*/
        List<FotaPlanObjListPo> existFotaPlanObjListPos = fotaPlanObjListDbService.listByOtaPlanId(otaPlanId);

        if(MyCollectionUtil.isNotEmpty(existFotaPlanObjListPos)){
            existFotaPlanObjListPos.forEach(item ->{
                fotaObjectCacheInfoService.delFotaVinCacheInfo(item.getVin());
            });
        }
        ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).deleteByOtaPlanIdPhysical(otaPlanId);
        boolean saveBatch = fotaPlanObjListDbService.saveBatch(fotaPlanObjListPos);
        if (!saveBatch) {
            log.error("任务升级对象信息写入失败. 任务ID : [ {} ]", upgradeTaskObjectReqList.get(0).getOtaPlanId());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_UPDATED);
        }

        //添加到redis缓存
        fotaPlanObjListPos.forEach(item ->{
            FotaVinCacheInfo fotaVinCacheInfo = new FotaVinCacheInfo();
            fotaVinCacheInfo.setVin(item.getVin());
            fotaVinCacheInfo.setObjectId(item.getOtaObjectId());
            fotaVinCacheInfo.setOtaPlanId(item.getOtaPlanId());
            fotaVinCacheInfo.setOtaPlanObjectId(item.getId());
            fotaObjectCacheInfoService.setFotaVinCacheInfo(fotaVinCacheInfo);
        });

        //先删除可能已经存在的任务明细
        /*QueryWrapper<FotaPlanFirmwareListPo> fotaPlanFirmwareListWrapper = new QueryWrapper<>();
        fotaPlanFirmwareListWrapper.eq("plan_id", otaPlanId);
        //兼容问题所以屏蔽
        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListDbService.list(fotaPlanFirmwareListWrapper);*/
        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListDbService.listByOtaPlanId(otaPlanId);

        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(fotaPlanFirmwareListPos), "任务ECU清单不存在，请检查");

        //TODO 此处代码逻辑有待考虑
        /*QueryWrapper<UpgradeStrategyPo> upgradeStrategyDoQueryWrapper = new QueryWrapper<>();
        upgradeStrategyDoQueryWrapper.eq("ota_plan_id", otaPlanId);
        List<UpgradeStrategyPo> upgradeStrategyPos = upgradeStrategyDbService.list(upgradeStrategyDoQueryWrapper);*/


        List<UpgradeStrategyPo> upgradeStrategyPos = upgradeStrategyDbService.listByOtaPlanId(otaPlanId);


        MyAssertUtil.isTrue(MyCollectionUtil.isNotEmpty(upgradeStrategyPos), "任务策略信息不存在，请检查");
        Map<Long, UpgradeStrategyPo> upgradeStrategyDoMap = upgradeStrategyPos.stream().collect(Collectors.toMap(UpgradeStrategyPo::getFirmwareId, Function.identity()));
        for (FotaPlanFirmwareListPo fotaPlanFirmwareListPo : fotaPlanFirmwareListPos) {
            if(Objects.nonNull(upgradeStrategyDoMap.get(fotaPlanFirmwareListPo.getId()))) {
                fotaPlanFirmwareListPo.setRollbackMode(upgradeStrategyDoMap.get(fotaPlanFirmwareListPo.getId()).getRollbackMode());
            }
        }
        fotaPlanFirmwareListDbService.saveOrUpdateBatch(fotaPlanFirmwareListPos);

        FotaPlanPo fotaPlanPo = new FotaPlanPo();
        fotaPlanPo.setPublishState(PublishStateEnum.UNPUBLISH.getState());
        fotaPlanPo.setId(otaPlanId);
        fotaPlanPo.setUpdateTime(now);
        fotaPlanPo.setUpdateBy(upgradeObjectListReq.getUpdateBy());
        fotaPlanDbService.updateById(fotaPlanPo);
        return AddFotaPlanResultVo.builder().result(1).otaPlanId(Long.toString(otaPlanId)).build();
    }

    @Override
    public List<ExistValidPlanObjVo> validate4AddFotaPlan(List<UpgradeTaskObjectReq> upgradeTaskObjectReqList) {
        //添加逻辑保证车辆只能存在于一个有效的升级任务中
        List<ExistValidPlanObjVo> existValidPlanObjs = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        for (UpgradeTaskObjectReq upgradeTaskObjectReq : upgradeTaskObjectReqList) {

            //添加逻辑保证车辆只能存在于一个有效的升级任务中
/*            QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ota_object_id", upgradeTaskObjectReq.getOtaObjectId());
            List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.list(queryWrapper);*/

            List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.listByOtaObjectId(upgradeTaskObjectReq.getOtaObjectId());

            if (MyCollectionUtil.isNotEmpty(fotaPlanObjListPos)) {
                /*QueryWrapper<FotaPlanPo> fotaPlanQueryWrapper = new QueryWrapper<>();
                fotaPlanQueryWrapper.in("id", MyCollectionUtil.map2NewList(fotaPlanObjListPos, FotaPlanObjListPo::getOtaPlanId));*/
                List<FotaPlanPo> fotaPlanPos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListPos, FotaPlanObjListPo::getOtaPlanId));
                if (MyCollectionUtil.isNotEmpty(fotaPlanPos)) {
                    for (FotaPlanPo fotaPlanPo : fotaPlanPos) {
                        boolean inRange = now.isBefore(MyDateUtil.uDateToLocalDateTime(fotaPlanPo.getPlanEndTime()));
                        boolean isInvalid = PublishStateEnum.inPublishState(fotaPlanPo.getPublishState());
                        if (inRange && !isInvalid) {
                            ExistValidPlanObjVo existValidPlanObj = new ExistValidPlanObjVo();
                            existValidPlanObj.setOtaPlanId(fotaPlanPo.getId());
                            existValidPlanObj.setOtaObjectId(upgradeTaskObjectReq.getOtaObjectId());
                            existValidPlanObj.setOtaPlanName(fotaPlanPo.getPlanName());
                            //

                            FotaObjectPo fotaObectPo = fotaObjectDbService.getById(upgradeTaskObjectReq.getOtaObjectId());
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

    @Override
    public ExistValidPlanObjVo checkValidate4AddFotaPlan(Long otaObjectId) {
        //添加逻辑保证车辆只能存在于一个有效的升级任务中
        /*QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<FotaPlanObjListPo>();
        queryWrapper.eq("ota_object_id", otaObjectId);*/
        List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.listByOtaObjectId(otaObjectId);

        ExistValidPlanObjVo existValidPlanObj = null;
        if (MyCollectionUtil.isNotEmpty(fotaPlanObjListPos)) {
            /*QueryWrapper<FotaPlanPo> fotaPlanQueryWrapper = new QueryWrapper<FotaPlanPo>();
            fotaPlanQueryWrapper.in("id", MyCollectionUtil.map2NewList(fotaPlanObjListPos, FotaPlanObjListPo::getOtaPlanId));*/
            List<FotaPlanPo> fotaPlanPos = fotaPlanDbService.listByIds(MyCollectionUtil.map2NewList(fotaPlanObjListPos, FotaPlanObjListPo::getOtaPlanId));
            if (MyCollectionUtil.isNotEmpty(fotaPlanPos)) {
                LocalDateTime now = LocalDateTime.now();
                for (FotaPlanPo fotaPlanPo : fotaPlanPos) {
                    /**
                     * 判断是否可以添加：
                     * 1、当前日期已经在任务结束日期之后
                     */
                    boolean flag = now.isAfter(MyDateUtil.uDateToLocalDateTime(fotaPlanPo.getPlanEndTime()));
                    //flag = flag || fotaPlanPo.getPlanStatus().equals(Enums.PlanStatusTypeEnum.INVALID.getType());
                    flag = flag || !PublishStateEnum.inPublishState(fotaPlanPo.getPublishState());
                    if (!flag) {
                        existValidPlanObj = new ExistValidPlanObjVo();
                        existValidPlanObj.setOtaPlanId(fotaPlanPo.getId());
                        existValidPlanObj.setOtaObjectId(otaObjectId);
                        existValidPlanObj.setOtaPlanName(fotaPlanPo.getPlanName());
                        FotaObjectPo fotaObectDo = fotaObjectDbService.getById(otaObjectId);
                        MyAssertUtil.notNull(fotaObectDo, "非法的车辆对象");
                        existValidPlanObj.setVin(fotaObectDo.getObjectId());
                    }
                }
            }
        }
        return existValidPlanObj;
    }

    @Override
    public List<TaskTotalStatisticsVo> taskProcessStatistics(TaskProcessStatisticsReq req) {
        boolean contains = req.getEndTime().contains("23:59:59");
        if (!contains) {
            req.setEndTime(req.getEndTime().trim() + " 23:59:59");
        }
        Integer method = req.getStatisticalMethod();
        Enums.StatisticalMethodEnum statisticalMethodEnum = Enums.StatisticalMethodEnum.getByType(method);
        MyAssertUtil.notNull(statisticalMethodEnum, "统计枚举类型参数异常");
        List<DateReportVo> dateReportVos = null;
        List<Map<String, Object>> maps = null;
        List<String> dateRangeList = null;
        try {
            Date startDate = MyDateUtil.parseDate(req.getStartTime());
            Date endDate = MyDateUtil.parseDateTime(req.getEndTime());
            switch (statisticalMethodEnum){
                case DAILY_REPORT:
                    maps = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryDailyReport(req.getTaskId(), req.getStartTime(), req.getEndTime());
                    dateRangeList = MyDateUtil.computeDayRangeDateExStrs(startDate, endDate);
                    break;
                case WEEKLY_REPORT:
                    maps = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryWeeklyReport(req.getTaskId(), req.getStartTime(), req.getEndTime());
                    dateRangeList = MyDateUtil.computeWeekRangeDateExStrs(startDate, endDate);
                    break;
                case MONTHLY_REPORT:
                    maps = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryMonthlyReport(req.getTaskId(), req.getEndTime());
                    dateRangeList = MyDateUtil.computeMonthRangeDateExStrs(startDate, endDate);
                    break;
                default:
                    break;
            }
            dateReportVos = MyCollectionUtil.map2NewList(maps, item ->{
                DateReportVo dateReportVo = new DateReportVo();
                MyMapUtil.mapToBean(item, dateReportVo);
                return dateReportVo;
            });
            List<TaskTotalStatisticsVo> taskTotalStatisticsVos = wrapTaskTotalStatistics(dateReportVos, dateRangeList);
            return taskTotalStatisticsVos;
        } catch (ParseException e) {
            log.error("解析Date报错.TaskProcessStatisticsReq:{}", req, e);
        }
        return null;
    }

    @Override
    public List<TaskTotalStatisticsVo> upgradeTimeStatistics(UpgradeTimeStatisticsReq req) {
        try {
            Date endDate = MyDateUtil.parseDate(req.getEndTime());
            List<Map<String, Object>> maps = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryTimeReport(req.getTaskId(), req.getEndTime());
            List<DateReportVo> dateReportPos = MyCollectionUtil.map2NewList(maps, item ->{
                DateReportVo dateReportVo = new DateReportVo();
                MyMapUtil.mapToBean(item, dateReportVo);
                return dateReportVo;
            });
            List<String> dateRangeList = MyDateUtil.computeTimeRangeDateExStrs(endDate);
            List<TaskTotalStatisticsVo> taskTotalStatisticsVos = wrapTaskTotalStatistics(dateReportPos, dateRangeList);
            return taskTotalStatisticsVos;
        } catch (ParseException e) {
            log.error("解析Date报错.TaskProcessStatisticsReq:{}", req);
            return null;
        }
    }

    @Override
    public Map<String, Object> getPlanObjectListDetails(TaskMonitoriedReq req) {
        int count = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).countPageByPlanId(req.getTaskId(), req.getVin(), req.getTaskStatus(), req.getExecutedStatus());
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        req.setTotalItem(count);
        if (count == 0) {
            map.put(CommonConstant.TOTAL, 0);
            map.put(CommonConstant.DATA, Collections.emptyList());
        } else {

            List<Map<String, Object>> maps = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryPageByPlanId(req.getTaskId(), req.getVin(), req
            .getTaskStatus(), req.getExecutedStatus(), req.getStartRow(), req.getPageSize());
            List<PlanObjectListDetailVo> planObjectListDetailVoList = MyCollectionUtil.map2NewList(maps, item ->{
                PlanObjectListDetailVo planObjectListDetailVo = new PlanObjectListDetailVo();
                MyMapUtil.mapToBean(item, planObjectListDetailVo);
                return planObjectListDetailVo;
            });
            for (PlanObjectListDetailVo objectListDetailVo : planObjectListDetailVoList) {
                Enums.TaskObjMonitoriedStatusTypeEnum executedStatus = Enums.TaskObjMonitoriedStatusTypeEnum.getByType(objectListDetailVo.getExecutedStatusCode());
                if (executedStatus != null) {
                    objectListDetailVo.setExecutedStatus(executedStatus.getDesc());
                }
                //Enums.PlanMonitoriedStatusTypeEnum taskStatus = Enums.PlanMonitoriedStatusTypeEnum.getByType(objectListDetailVo.getTaskStatusCode());
                PublishStateEnum taskStatus = PublishStateEnum.getByState(objectListDetailVo.getTaskStatusCode());
                if (taskStatus != null) {
                    objectListDetailVo.setTaskStatus(taskStatus.getKey());
                }
                Enums.UpgradeResultTypeEnum resultTypeEnum = Enums.UpgradeResultTypeEnum.getByType(objectListDetailVo.getResultCode());
                if (resultTypeEnum != null) {
                    objectListDetailVo.setResult(resultTypeEnum.getDesc());
                }
            }
            map.put(CommonConstant.TOTAL, count);
            map.put(CommonConstant.DATA, planObjectListDetailVoList);

        }
        return map;
    }

    @Override
    public PlanObjectListDetailVo queryVoById(TaskMonitoriedECUReq req) {
        Map<String, Object> map = ((FotaPlanObjListMapper)fotaPlanObjListDbService.getBaseMapper()).queryVoById(req.getId());
        PlanObjectListDetailVo planObjectListDetailVo = new PlanObjectListDetailVo();
        MyMapUtil.mapToBean(map, planObjectListDetailVo);
        return planObjectListDetailVo;
    }

    /*@Override
    public List<FotaPlanObjListPo> listByOtaPlanId(Long planId) {
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<FotaPlanObjListPo>();
        queryWrapper.eq("ota_plan_id", planId);
        List<FotaPlanObjListPo> fotaPlanObjListPos = list(queryWrapper);
        return MyCollectionUtil.safeList(fotaPlanObjListPos);
    }*/

    /**
     * 拆包封包
     *
     * @param dateReportVos
     * @param dateRangeList
     * @return
     */
    private List<TaskTotalStatisticsVo> wrapTaskTotalStatistics(List<DateReportVo> dateReportVos, List<String> dateRangeList) {
        if(MyCollectionUtil.isEmpty(dateReportVos) || MyCollectionUtil.isEmpty(dateReportVos)){
            log.warn("参数为空.dateReportPos.size={}, dateRangeList.size={}", MyCollectionUtil.size(dateReportVos), MyCollectionUtil.size(dateRangeList));
            return Collections.emptyList();
        }

        //部分状态码给到不同标识阶段 0-10状态码：版本检查 11-29状态码：下载过程 30-100状态码：安装过程 100以上状态码：安装结果
        List<TaskTotalStatisticsVo> taskTotalStatisticsVos = dateRangeList.stream().map(item -> {
            TaskTotalStatisticsVo vo = new TaskTotalStatisticsVo();
            vo.setDate(item);
            return vo;
        }).collect(Collectors.toList());
        final List<DateReportVo> dateReportPoList = dateReportVos;
        taskTotalStatisticsVos.forEach(item1 -> {
            dateReportPoList.forEach(item2 -> {
                if (item2.getDate().equals(item1.getDate())) {
                    if ((CommonConstant.NOT_UPGRADE_STATUS_RANGE_START <= item2.getStatus() && item2.getStatus()
                            <= CommonConstant.NOT_UPGRADE_STATUS_RANGE_END)
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_WAIT.getType()
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_BEGIN.getType()) {
                        item1.setNotUpgrade(item1.getNotUpgrade() + item2.getCount());
                    } else if (item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.getType()) {
                        item1.setUpgradeSuccess(item2.getCount());
                    } else if (item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.getType()
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_FAIL.getType()) {
                        item1.setUpgradeFail(item2.getCount());
                    } else if (item2.getStatus() == Enums.TaskObjStatusTypeEnum.STOP_NO_PLAN.getType()
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.STOP_NO_DOWNLOAD.getType()
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.STOP_NO_INSTALLED.getType()
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL.getType()) {
                        item1.setCancel(item1.getCancel() + item2.getCount());
                    } else if ((CommonConstant.UPGRADING_STATUS_RANGE_START <= item2.getStatus() && item2.getStatus()
                            <= CommonConstant.UPGRADING_STATUS_RANGE_END)
                            || item2.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_PRECHECK_FAIL.getType()
                    ) {
                        item1.setUpgrading(item1.getUpgrading() + item2.getCount());
                    }
                }
            });
        });
        return taskTotalStatisticsVos;
    }
}
