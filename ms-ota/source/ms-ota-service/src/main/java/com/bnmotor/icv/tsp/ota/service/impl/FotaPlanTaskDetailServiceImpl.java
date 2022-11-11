package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.DeviceTreeNodeLevelEnum;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanTaskDetailMapper;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.TaskFailStatisticsFilterReq;
import com.bnmotor.icv.tsp.ota.model.req.TaskMonitoriedECUReq;
import com.bnmotor.icv.tsp.ota.model.resp.PlanFirmwareDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.PlanObjectListDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.TaskFailStatisticsVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyMapUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xxc
 * @ClassName: FotaPlanTaskDetailPo
 * @Description: OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
 * 在创建升级计划时创建升级 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-08
 */
@Service
@Slf4j
public class FotaPlanTaskDetailServiceImpl implements IFotaPlanTaskDetailService {
    @Autowired
    private IFotaPlanTaskDetailDbService fotaPlanTaskDetailDbService;

	@Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

	@Autowired
    private IFotaPlanObjListService planObjListService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Override
    public List<TaskFailStatisticsVo> taskFailStatistics(TaskFailStatisticsFilterReq req) {
        List<TaskFailStatisticsVo> taskFailStatisticsVos;
        List<Map<String, Object>> maps;
        if (StringUtils.isNotEmpty(req.getTaskName()) || StringUtils.isNotEmpty(req.getVin()) || req.getEcuId().longValue() != 0) {
            maps = ((FotaPlanTaskDetailMapper) fotaPlanTaskDetailDbService.getBaseMapper()).queryTaskFailReport(req.getVin(), req.getEcuId(), req.getTaskName());
            //TODO
        } else {
            List<DeviceTreeNodePo> deviceTreeNodePoList = deviceTreeNodeDbService.listByTreeLevel(DeviceTreeNodeLevelEnum.CONF.getLevel());
            List<Long> treeNodeIds = MyCollectionUtil.map2NewList(deviceTreeNodePoList, DeviceTreeNodePo::getId);
            maps = ((FotaPlanTaskDetailMapper) fotaPlanTaskDetailDbService.getBaseMapper()).queryTaskFailReportByTreeNodeId(treeNodeIds);
            //TODO
        }
        /*List<PlanFirmwareDetailVo> */taskFailStatisticsVos = MyCollectionUtil.map2NewList(maps, item ->{
            TaskFailStatisticsVo taskFailStatisticsVo = new TaskFailStatisticsVo();
            MyMapUtil.mapToBean(item, taskFailStatisticsVo);
            return taskFailStatisticsVo;
        });

        /*taskFailStatisticsVos = Lists.newArrayList();*/
        return taskFailStatisticsVos;
    }

    @Override
    public  Map<String, Object> getPlanTaskDetails(TaskMonitoriedECUReq req) {
        Map<String,Object> map = Maps.newHashMapWithExpectedSize(3);
        PlanObjectListDetailVo planObjListDo = planObjListService.queryVoById(req);
        planObjListDo.setExecutedStatus(Enums.TaskObjStatusTypeEnum.getByType(planObjListDo.getExecutedStatusCode()).getDesc());
        planObjListDo.setTaskStatus(PublishStateEnum.getByState(planObjListDo.getTaskStatusCode()).getKey());
        planObjListDo.setResult(Enums.UpgradeResultTypeEnum.getByType(planObjListDo.getResultCode()).getDesc());
        map.put(CommonConstant.TASK_INFO,planObjListDo);

        int count = ((FotaPlanTaskDetailMapper)fotaPlanTaskDetailDbService.getBaseMapper()).countByPlanObjId(req.getId());
        map.put(CommonConstant.TOTAL,count);
            map.put(CommonConstant.DATA,Collections.emptyList());
        if (count > 0) {
            List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = fotaPlanTaskDetailDbService.listByOtaPlanObjId(Long.parseLong(req.getId()));

            Long otaPlanId = fotaPlanTaskDetailPos.get(0).getOtaPlanId();
            List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(fotaPlanDbService.getById(otaPlanId).getFotaStrategyId());
            Map<Long, FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPoMap = MyCollectionUtil.toMap(fotaStrategyFirmwareListPos, FotaStrategyFirmwareListPo::getId);

            final List<PlanFirmwareDetailVo> planFirmwareDetailVos = Lists.newArrayList();
            fotaPlanTaskDetailPos.forEach(item ->{
                PlanFirmwareDetailVo planFirmwareDetailVo = new PlanFirmwareDetailVo();
                planFirmwareDetailVo.setSourceVersion(item.getSourceVersion());
                planFirmwareDetailVo.setCurrentVersion(item.getCurrentVersion());

                Enums.PlanTaskDetailStatusEnum planTaskDetailStatus = Enums.PlanTaskDetailStatusEnum.getByType(item.getStatus());
                if(planTaskDetailStatus!=null){
                    planFirmwareDetailVo.setStatus(planTaskDetailStatus.getDesc());
                }

                planFirmwareDetailVo.setRollbackMode(1);
                //策略中单个固件记录数据item.getOtaPlanFirmwareId()
                FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo = fotaStrategyFirmwareListPoMap.get(item.getOtaPlanFirmwareId());
                if(Objects.nonNull(fotaStrategyFirmwareListPo)){
                    FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(fotaStrategyFirmwareListPo.getFirmwareId());
                    planFirmwareDetailVo.setComponentCode(fotaFirmwarePo.getComponentCode());
                    planFirmwareDetailVo.setComponentName(fotaFirmwarePo.getComponentName());
                    planFirmwareDetailVo.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());
                    planFirmwareDetailVo.setFirmwareName(fotaFirmwarePo.getFirmwareName());

                    planFirmwareDetailVo.setGroup(fotaStrategyFirmwareListPo.getDbGroupSeq());

                    FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(fotaStrategyFirmwareListPo.getTargetVersionId());
                    planFirmwareDetailVo.setTargetVersion(fotaFirmwareVersionPo.getFirmwareVersionNo());
                }
                planFirmwareDetailVos.add(planFirmwareDetailVo);
            });

            /*List<Map<String, Object>> maps = ((FotaPlanTaskDetailMapper)fotaPlanTaskDetailDbService.getBaseMapper()).queryByPlanObjId(req.getId());
            List<PlanFirmwareDetailVo> planFirmwareDetailVos = MyCollectionUtil.map2NewList(maps, item ->{
                PlanFirmwareDetailVo planFirmwareDetailVo = new PlanFirmwareDetailVo();
                MyMapUtil.mapToBean(item, planFirmwareDetailVo);
                return planFirmwareDetailVo;
            });*/
            /*for (PlanFirmwareDetailVo objectListDetailVo : planFirmwareDetailVos) {
                Enums.PlanTaskDetailStatusEnum planTaskDetailStatus = Enums.PlanTaskDetailStatusEnum.getByType(objectListDetailVo.getStatusCode());
                if(planTaskDetailStatus!=null){
                    objectListDetailVo.setStatus(planTaskDetailStatus.getDesc());
                }
            }*/
            map.put(CommonConstant.DATA,planFirmwareDetailVos);
        }
        return map;
    }
}