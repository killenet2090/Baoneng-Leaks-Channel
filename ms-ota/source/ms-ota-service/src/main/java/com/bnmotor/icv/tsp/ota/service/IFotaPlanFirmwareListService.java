/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.service;


import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareListReq;

/**
 * <pre>
 * 该表用于定义一个OTA升级计划中需要升级哪几个软件
包含：
     1. 依赖的软件清单
                                               -业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface IFotaPlanFirmwareListService{
    /**
     * 保存信息
     *
     * @param  upgradeFirmwareListReq 该表用于定义一个OTA升级计划中需要升级哪几个软件
     * @return 保存数据ID
     */
     Boolean insertUpgradeFirmwareList(UpgradeFirmwareListReq upgradeFirmwareListReq);

    /**
     * 根据ID更新信息
     *
     * @param upgradeFirmwareListReq 该表用于定义一个OTA升级计划中需要升级哪几个软件
     * @return 更新数量
     */
    Boolean updateFotaPlanFirmwareList(UpgradeFirmwareListReq upgradeFirmwareListReq);

    /**
     * 获取升级任务清单列表
     * @param otaPlanId
     * @return
     */
    //List<FotaPlanFirmwareListPo> listByOtaPlanId(Long otaPlanId);

    /**
     * 获取升级任务清单列表
     * @param otaPlanId
     * @return
     */
    //List<FotaPlanFirmwareListPo> getByPlanIdWithFirmwareIds(Long otaPlanId, List<Long> firmwareIds);

    /**
     * 固件版本制作状态是否完成
     * @param id
     * @return
     */
    boolean isFirmwarePkgBuildedStatus(Long id);
    
    /**
     * V2版本检查包状态
     * @param planId
     * @return
     */
    boolean isFirmwarePkgBuildedStatusV2(Long planId);

}