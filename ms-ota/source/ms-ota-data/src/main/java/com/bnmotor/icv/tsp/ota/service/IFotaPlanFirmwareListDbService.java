/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanFirmwareListPo;

import java.util.List;

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
public interface IFotaPlanFirmwareListDbService extends IService<FotaPlanFirmwareListPo> {
    /**
     * 获取升级任务清单列表
     * @param otaPlanId
     * @return
     */
    List<FotaPlanFirmwareListPo> listByOtaPlanId(Long otaPlanId);

    /**
     * 获取升级任务清单列表
     * @param otaPlanId
     * @return
     */
    List<FotaPlanFirmwareListPo> getByPlanIdWithFirmwareIds(Long otaPlanId, List<Long> firmwareIds);

    /**
     *
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    List<FotaPlanFirmwareListPo> list(Long firmwareId, Long firmwareVersionId);
}
