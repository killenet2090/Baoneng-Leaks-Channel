package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.job.dispatch.Handler;
import com.bnmotor.icv.tsp.device.job.dispatch.TaskType;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: VehAllAsynHandler
 * @Description: 全量同步车辆信息
 * @author: zhangwei2
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
@Handler(taskType = TaskType.ONCE, dataType = DataType.VEHICLE, queryType = QueryType.ALL)
public class OnceVehHandler extends AbstractAsynHandler {
    /**
     * 每批次查询的条目
     */
    private static final Integer LIMIT = 100;
    @Resource
    private VehicleMapper vehicleMapper;

    @Override
    public void processData(VehDataAsynTaskPo task) {
        List<VehiclePo> vehicles = vehicleMapper.listByFromId(null, null, task.getQueryCursor(), LIMIT);
        if (CollectionUtils.isEmpty(vehicles)) {
            return;
        }

        while (CollectionUtils.isNotEmpty(vehicles)) {
            Long fromId = processVehicle(task, vehicles);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                log.warn("异步同步数据任务出错");
            }

            vehicles = vehicleMapper.listByFromId(null, null, fromId, LIMIT);
        }
    }
}
