/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.service;


/**
 * <pre>
 * 任务终止条件业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author handong
 * @version 1.1.0
 */
@Deprecated
public interface ITaskTerminateService /*extends IService<TaskTerminatePo>*/ {
    /**
     *
     * @param otaPlanId
     * @return
     */
    //TaskTerminatePo findByOtaPlanId(Long otaPlanId);
    /**
     * <pre>
     * 分页查询任务终止条件数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 任务终止条件数据集合
     */
    //List<TaskTerminate> queryPage(TaskTerminateQuery query);

    /**
     * <pre>
     * 根据ID查询任务终止条件数据
     * </pre>
     *
     * @param taskTerminateId 任务终止条件Id
     * @return 任务终止条件
     */
    //TaskTerminate queryById(Long taskTerminateId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param taskTerminate 任务终止条件
     * @return 保存数据ID
     */
    //Long insertTaskTerminate(TaskTerminate taskTerminate);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param taskTerminate 任务终止条件
     * @return 更新数量
     */
    //int updateTaskTerminate(TaskTerminate taskTerminate);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param taskTerminateId 任务终止条件Id
     * @return 删除数量
     */
    //int deleteById(Long taskTerminateId);

}
