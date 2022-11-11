package com.bnmotor.icv.tsp.device.job;

import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.job.dispatch.AsynQueryStatus;
import com.bnmotor.icv.tsp.device.job.dispatch.OnceTaskDispatcher;
import com.bnmotor.icv.tsp.device.job.dispatch.TaskType;
import com.bnmotor.icv.tsp.device.mapper.VehDataAsynTaskMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: VehicleDataAsynOnceJob
 * @Description: 数据同步一次性任务
 * @author: zhangwei2
 * @date: 2020/10/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class VehicleDataAsynOnceJob {
    private static final Integer LIMIT = 10;
    @Resource
    private VehDataAsynTaskMapper aysnQueryMapper;
    @Resource
    private OnceTaskDispatcher taskDispatcher;

    @XxlJob("vehicleDataAsynOnceJob")
    public ReturnT<String> vehicleDataAsynOnceJob(String param) {
        List<VehDataAsynTaskPo> noExecuteQuerys = aysnQueryMapper.listNoExecute(TaskType.ONCE.getType(), LIMIT, 0);
        while (CollectionUtils.isNotEmpty(noExecuteQuerys)) {
            for (VehDataAsynTaskPo task : noExecuteQuerys) {
                taskDispatcher.dispatch(task);

                VehDataAsynTaskPo updateTask = new VehDataAsynTaskPo();
                updateTask.setStatus(AsynQueryStatus.SUCCESSED.getStatus());
                updateTask.setUpdateBy(Constant.SYSTEM);
                updateTask.setUpdateTime(LocalDateTime.now());
                updateTask.setId(task.getId());
                aysnQueryMapper.updateById(updateTask);
            }
            noExecuteQuerys = aysnQueryMapper.listNoExecute(TaskType.ONCE.getType(), LIMIT, 0);
        }
        return ReturnT.SUCCESS;
    }
}
