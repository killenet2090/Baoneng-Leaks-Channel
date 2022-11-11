/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.model.entity.TaskTerminatePo;
import com.bnmotor.icv.tsp.ota.mapper.TaskTerminateMapper;
import com.bnmotor.icv.tsp.ota.service.ITaskTerminateDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  任务终止条件默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Service
@AllArgsConstructor
public class TaskTerminateDbServiceImpl extends ServiceImpl<TaskTerminateMapper, TaskTerminatePo> implements ITaskTerminateDbService {

    @Override
    public TaskTerminatePo findByOtaPlanId(Long otaPlanId) {
        QueryWrapper<TaskTerminatePo> queryWrapper = new QueryWrapper<TaskTerminatePo>();
        queryWrapper.eq("ota_plan_id", otaPlanId);
        return getOne(queryWrapper);
    }
}
