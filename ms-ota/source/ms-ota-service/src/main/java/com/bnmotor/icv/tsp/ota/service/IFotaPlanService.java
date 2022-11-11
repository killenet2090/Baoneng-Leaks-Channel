package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanQuery;
import com.bnmotor.icv.tsp.ota.model.req.FotaPlanReq;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaPlanDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaPlanVo;

/*import com.bnmotor.icv.tsp.ota.common.enums.Enums;*/

/**
 * @ClassName: FotaPlanPo
 * @Description: OTA升级计划表 服务类
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaPlanService {

    /**
     * 分页查询升级任务
     * @param query
     * @return
     */
    IPage<FotaPlanVo> queryPage(FotaPlanQuery query);

    /**
     * 根据Id查询对象类
     * @param planId
     * @return
     */
    FotaPlanDetailVo getFotaPlanDetailVoById(Long planId);

    /**
     * 插入升级任务
     * @param fotaPlanReq
     * @return
     */
    long insertFotaPlan(FotaPlanReq fotaPlanReq);

    /**
     * 更新升级任务
     * @param fotaPlanReq
     * @return
     */
    Boolean updateFotaPlan(FotaPlanReq fotaPlanReq);

    /**
     * 删除升级任务
     * @param planId
     * @return
     */
    Integer deleteById(Long planId);

    /**
     * 是否存在已经在任务列表中的固件
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    boolean existPlanWithFirmware(Long firmwareId, Long firmwareVersionId);

    /**
     * 新增OTA升级计划
     * @param fotaPlanReq
     * @return
     */
    AddFotaPlanResultVo addFotaPlan(FotaPlanReq fotaPlanReq);

    /**
     * OTA升级计划表
     * @param fotaPlanReq
     * @return
     */
    /*boolean editFotaPlan(FotaPlanReq fotaPlanReq);*/

    /**
     * 获取存在有效的升级任务
     * @param vin
     * @return
     */
    //FotaPlanPo findValidOtaPlanByVin(String vin);

    /**
     * 获取存在有效的升级任务
     * 1、当前日期在任务时间周期内
     * 2、当前任务未结束，处于发布状态
     * @param objectId
     * @return  FotaPlanPo
     */
    //FotaPlanPo findValidOtaPlanByObjectId(Long objectId);

    /**
     * 获取存在有效的升级任务
     * @param fotaPlanObjListPo
     * @return
     */
    //FotaPlanPo findValidOtaPlan(FotaPlanObjListPo fotaPlanObjListPo);

    /**
     * 让任务失效
     * @param otaPlanId
     * @return
     */
    boolean setInvalid(Long otaPlanId);

    /**
     * 分页获取到期未结束的升级计划
     * @return
     */
    //IPage<FotaPlanPo> getExpiredFotaPlan(int offset, int pageSize, long currentId);

    /**
     *
     * @return
     */
    /*List<FotaPlanPo> getFotaPlans();*/
}
