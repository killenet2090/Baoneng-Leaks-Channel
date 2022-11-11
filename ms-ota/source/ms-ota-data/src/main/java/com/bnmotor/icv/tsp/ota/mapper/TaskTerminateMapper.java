/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.TaskTerminatePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <pre>
 *   <b>表名</b>：tb_task_terminate
 *   任务终止条件数据操作对象
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Mapper
public interface TaskTerminateMapper extends BaseMapper<TaskTerminatePo> {
    /**
     * 根据任务Id删除(历史原因，重新定义)
     * @param otaPlanId
     * @return
     */
    int deleteByOtaPlanIdPhysical(Long otaPlanId);

    /**
     * 根据任务获取终止条件
     * @param otaPlanId
     * @return
     */
    //TaskTerminateDo findByOtaPlanId(Long otaPlanId);
}
