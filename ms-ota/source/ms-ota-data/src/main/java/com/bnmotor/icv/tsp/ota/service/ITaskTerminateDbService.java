/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.TaskTerminatePo;

/**
 * <pre>
 * 任务终止条件业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author handong
 * @version 1.1.0
 */
public interface ITaskTerminateDbService extends IService<TaskTerminatePo> {
    /**
     *
     * @param otaPlanId
     * @return
     */
    TaskTerminatePo findByOtaPlanId(Long otaPlanId);
}
