package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.req.*;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;
import com.bnmotor.icv.tsp.ota.model.resp.PlanObjectListDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.TaskTotalStatisticsVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: FotaPlanObjListPo
 * @Description: OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆 服务类
 * @author xxc
 * @since 2020-07-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaPlanObjListService{
    /**
     * 根据对象Id获取最新升级对象
     * @param objectId
     * @return
     */
    //FotaPlanObjListPo findOneByObjectId(Long objectId);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param fotaPlanObjList
     * @return 更新数量
     */
    AddFotaPlanResultVo updateFotaPlanObjList(UpgradeObjectListReq fotaPlanObjList);

    /**
     * 新增升级对象清单
     * @param upgradeObjectListReq
     * @return AddFotaPlanResultVo
     */
    AddFotaPlanResultVo insertUpgradeTaskObjectList(UpgradeObjectListReq upgradeObjectListReq);

    /**
     * 新增任务添加验证
     * @param upgradeTaskObjectReqList
     * @return
     */
    List<ExistValidPlanObjVo> validate4AddFotaPlan(List<UpgradeTaskObjectReq> upgradeTaskObjectReqList);

    /**
     * 检查一个车辆是否在一个有效的任务中
     * @param otaObjectId
     * @return
     */
    ExistValidPlanObjVo checkValidate4AddFotaPlan(Long otaObjectId);

    /**
     * 任务进度详细统计
     * @param req
     * @return
     */
    List<TaskTotalStatisticsVo> taskProcessStatistics(TaskProcessStatisticsReq req);

    /**
     * 升级时间段统计
     * @param req
     * @return
     */
    List<TaskTotalStatisticsVo> upgradeTimeStatistics(UpgradeTimeStatisticsReq req);

    /**
     * 任务监控
     * @param req
     * @return 更新是否成功
     */
    Map<String,Object> getPlanObjectListDetails(TaskMonitoriedReq req);

    /**
     * 查询车辆升级记录
     * @param req
     * @return
     */
    PlanObjectListDetailVo queryVoById(TaskMonitoriedECUReq req);

    /**
     *
     * @param planId
     * @return
     */
    //List<FotaPlanObjListPo> listByOtaPlanId(Long planId);
}
