package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;
import com.bnmotor.icv.tsp.device.job.dispatch.Handler;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.job.dispatch.TaskType;
import com.bnmotor.icv.tsp.device.mapper.VehicleTagMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: PermanentVehicleHandler
 * @Description: 永久性车辆数据同步处理器
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
@Handler(taskType = TaskType.PERMANENT, dataType = DataType.TAG, queryType = QueryType.ALL)
public class PermanentTagHandler extends AbstractAsynHandler {
    /**
     * 每批次查询的条目
     */
    private static final Integer LIMIT = 100;
    @Resource
    private VehicleTagMapper tagMapper;

    @Override
    public void processData(VehDataAsynTaskPo task) {
        List<VehicleTagPo> vehTagPos = tagMapper.listByTagId(task.getQueryCursor(), null, LIMIT);
        if (CollectionUtils.isEmpty(vehTagPos)) {
            return;
        }

        while (CollectionUtils.isNotEmpty(vehTagPos)) {
            Long fromId = processTag(task, vehTagPos);
            vehTagPos = tagMapper.listByTagId(null, fromId, LIMIT);
        }
    }
}
