package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @ClassName: FotaPlanObjListPo
 * @Description: OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆 服务类
 * @author xxc
 * @since 2020-07-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaPlanObjListDbService extends IService<FotaPlanObjListPo> {
    /**
     * 根据对象Id获取最新升级对象
     * @param objectId
     * @return
     */
    FotaPlanObjListPo findOneByObjectId(Long objectId);

    /**
     *
     * @param planId
     * @return
     */
    List<FotaPlanObjListPo> listByOtaPlanId(Long planId);

    /**
     *
     * @param otaObjectId
     * @return
     */
    List<FotaPlanObjListPo> listByOtaObjectId(Long otaObjectId);

    /**
     * 分页查询
     * @param planId
     * @param current
     * @param pageSize
     * @return
     */
    IPage<FotaPlanObjListPo> queryPage(Long planId, int current, int pageSize);
    
    /**
     * 查询指定升级计划下的车辆数量
     * @param <T>
     * @param <R>
     * @param fotaObjList
     * @return
     */
    <T, R> List<R> queryVehicleConunt(List<T> fotaObjList, Function function);

    /**
     * 按任务时间查询车辆
     * @param otaObjectId
     * @param targetTime
     * @return
     */
    List<FotaPlanObjListPo> queryPlanObjectListByTime(Long otaObjectId, Date targetTime);

    /**
     *
     * @param otaPlanId
     * @param currentStatus
     * @param status
     */
    void updateInvalidStatus(Long otaPlanId, Integer currentStatus, Integer status);
}
