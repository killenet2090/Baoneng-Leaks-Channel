package com.bnmotor.icv.tsp.device.job;

import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.job.dispatch.AsynQueryStatus;
import com.bnmotor.icv.tsp.device.job.dispatch.PermanentTaskDispatcher;
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
 * @ClassName: AsynQueryVehicleJob
 * @Description: 数据同步永久性任务
 * @author: zhangwei2
 * @date: 2020/10/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class VehicleDataAsynPermanentJob {
    /**
     * 任务每次查询条目
     */
    private static final Integer LIMIT = 100;

    @Resource
    private VehDataAsynTaskMapper taskMapper;
    @Resource
    private PermanentTaskDispatcher taskDispatcher;

    @XxlJob("vehicleDataAsynPermanentJob")
    public ReturnT<String> vehicleDataAsynPermanentJob(String param) {
        List<VehDataAsynTaskPo> tasks = taskMapper.listNoExecute(TaskType.PERMANENT.getType(), LIMIT, 0);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (VehDataAsynTaskPo task : tasks) {
                taskDispatcher.dispatch(task);

                VehDataAsynTaskPo updateTask = new VehDataAsynTaskPo();
                updateTask.setStatus(AsynQueryStatus.EXEXUTING.getStatus());
                updateTask.setUpdateBy(Constant.SYSTEM);
                updateTask.setUpdateTime(LocalDateTime.now());
                updateTask.setId(task.getId());
                taskMapper.updateById(updateTask);
            }
        }
        return ReturnT.SUCCESS;
    }
}
