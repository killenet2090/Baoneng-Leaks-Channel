package com.bnmotor.icv.tsp.ota.controller.inner;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.Enums.UpgradeResultTypeEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.req.*;
import com.bnmotor.icv.tsp.ota.model.resp.PlanFirmwareDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.PlanObjectListDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.TaskFailStatisticsVo;
import com.bnmotor.icv.tsp.ota.model.resp.TaskTotalStatisticsVo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanTaskDetailService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(value = "数据统计", tags = {"数据统计"})
@RestController
@Slf4j
public class DataStatisticsController extends AbstractController {
    @Autowired
    private IFotaPlanObjListService planObjListService;

    @Autowired
    private IFotaPlanTaskDetailService planTaskDetailService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @ApiOperation(value = "任务进度详细统计", notes = "任务进度详细统计", response = TaskTotalStatisticsVo.class)
    @PostMapping("/v1/dataStatistics/taskProcessStatistics")
    public ResponseEntity taskProcessStatistics(@RequestBody TaskProcessStatisticsReq req) {
        List<TaskTotalStatisticsVo> taskTotalStatisticsResps = planObjListService.taskProcessStatistics(req);
        return RestResponse.ok(taskTotalStatisticsResps);
    }

    @ApiOperation(value = "升级时间段分布", notes = "升级时间段分布", response = TaskTotalStatisticsVo.class)
    @PostMapping("/v1/dataStatistics/upgradeTimeStatistics")
    public ResponseEntity upgradeTimeStatistics(@RequestBody UpgradeTimeStatisticsReq req) {
        List<TaskTotalStatisticsVo> taskTotalStatisticsResps = planObjListService.upgradeTimeStatistics(req);
        return RestResponse.ok(taskTotalStatisticsResps);
    }

    @GetMapping("/v1/dataStatistics/taskMonitoriedVehicles")
    @ApiOperation(value = "任务监控车辆列表", notes = "任务监控车辆列表", response = PlanObjectListDetailVo.class)
    public ResponseEntity taskMonitoriedVehicles(TaskMonitoriedReq req) {
        Map<String, Object> planObjectListDetails = planObjListService.getPlanObjectListDetails(req);
        return RestResponse.ok(planObjectListDetails);
    }

    @GetMapping("/v1/dataStatistics/taskMonitoringECUInfo")
    @ApiOperation(value = "任务监控ECU列表", notes = "任务监控ECU列表", response = PlanFirmwareDetailVo.class)
    public ResponseEntity taskMonitoriedEcu(TaskMonitoriedECUReq req) {
        if (StringUtils.isNotEmpty(req.getId())) {
            Map<String, Object> resultMap = planTaskDetailService.getPlanTaskDetails(req);
            return RestResponse.ok(resultMap);
        }else{
            return RestResponse.error(OTARespCodeEnum.DATA_NOT_NULL);
        }
    }

    @PostMapping("/v1/dataStatistics/taskFailStatistics")
    @ApiOperation(value = "任务失败情况统计", notes = "任务失败情况统计", response = TaskFailStatisticsVo.class)
    public ResponseEntity taskFailStatistics(@RequestBody TaskFailStatisticsFilterReq req) {
        List<TaskFailStatisticsVo> taskFailStatisticsResqs = planTaskDetailService.taskFailStatistics(req);
        return RestResponse.ok(taskFailStatisticsResqs);
    }

    @PostMapping("/v1/dataStatistics/vehicleHistoryTaskStatistics")
    @ApiOperation(value = "车辆历史任务情况统计", notes = "车辆历史任务情况统计", response = Void.class)
    public ResponseEntity vehicleHistoryTaskStatistics() {
        return RestResponse.ok(null);
    }

    @GetMapping("/v1/dataStatistics/getTaskStatusList")
    @ApiOperation(value = "获得任务状态集合", notes = "获得任务状态集合", response = PublishStateEnum.class)
    public ResponseEntity getTaskStatusList() {
        return RestResponse.ok(PublishStateEnum.typeDescList);
    }

    @GetMapping("/v1/dataStatistics/getExecutedStatusList")
    @ApiOperation(value = "获得升级结果集合", notes = "获得升级结果集合", response = UpgradeResultTypeEnum.class)
    public ResponseEntity getExecutedStatusList() {
        return RestResponse.ok(Enums.UpgradeResultTypeEnum.typeDescList);
    }

    @GetMapping("/v1/dataStatistics/getTaskList")
    @ApiOperation(value = "获得任务集合", notes = "获得任务集合", response = Map.class)
    public ResponseEntity getTaskList() {
        List<FotaPlanPo> fotaPlanPoList = fotaPlanDbService.getFotaPlans();
        if (MyCollectionUtil.isEmpty(fotaPlanPoList)) {
            log.warn("获取任务集合为空");
            return RestResponse.ok(null);
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        fotaPlanPoList.forEach(item -> {
            Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
            map.put(CommonConstant.TASK_ID, Objects.toString(item.getId()));
            map.put(CommonConstant.TASK_NAME, item.getPlanName());
            resultList.add(map);
        });
        return RestResponse.ok(resultList);
    }
}
