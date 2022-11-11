package com.bnmotor.icv.tsp.ota.service.v2;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.req.v2.*;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanDetailV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanIsEnableV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanV2Vo;

import java.util.Date;

public interface IFotaPlanV2Service{

    /**
     * 分页查询升级任务
     * @param query
     * @return
     */
    IPage<FotaPlanV2Vo> queryPage(FotaPlanV2Query query);
    
    /**
     * 根据Id查询对象类
     * @param planId
     * @return
     */
    FotaPlanDetailV2Vo getFotaPlanDetailVoById(Long planId);

    /**
     * 插入升级任务
     * @param fotaPlanV2Req
     * @return
     */
    FotaPlanPo insertFotaPlan(FotaPlanV2Req fotaPlanV2Req);

    /**
     * 更新升级任务
     * @param fotaPlanReq
     * @return
     */
    Long updateFotaPlan(FotaPlanV2Req fotaPlanReq);

    /**
     * 删除升级任务
     * @param planId
     * @return
     */
    Integer deleteById(Long planId);

    /**
     * 是否启用或者延迟使用
     * @param fotaPlanIsEnableV2Req
     * @return
     */
    FotaPlanIsEnableV2RespVo isEnableFotaPlan(FotaPlanIsEnableV2Req fotaPlanIsEnableV2Req);

    /**
     * 审核任务处理
     * @param fotaPlanApproveV2Req
     * @return
     */
    Boolean approveFotaPlan(FotaPlanApproveV2Req fotaPlanApproveV2Req);

    /**
     * 发布任务处理
     * @param fotaPlanPublishV2Req
     * @return
     */
    Boolean publishFotaPlan(FotaPlanPublishV2Req fotaPlanPublishV2Req);
    
    /**
     * 查询用户
     * @param planId
     * @return
     */
    FotaPlanPo selectFotaPlan(Long planId);

    /**
     *
     * @param datetime
     * @param current
     * @param pagesize
     * @return
     */
    IPage<FotaPlanPo> queryDelayPlanPage(Date datetime, Integer current, Integer pagesize);

    /**
     *
     * @param fotaPlanPo
     * @param fotaPlanV2Req
     */
    void wrapExtraData(FotaPlanPo fotaPlanPo, FotaPlanV2Req fotaPlanV2Req);
    
}