package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;
import com.bnmotor.icv.tsp.device.job.dispatch.Handler;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.job.dispatch.TaskType;
import com.bnmotor.icv.tsp.device.mapper.VehModelDeviceMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.bnmotor.icv.tsp.device.model.entity.VehModelDevicePo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: PermanentDeviceHandler
 * @Description: 永久性零部件数据同步处理器
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Handler(taskType = TaskType.PERMANENT, dataType = DataType.VEH_DEVICE_MODEL, queryType = QueryType.ALL)
public class PermanentDeviceModelHandler extends AbstractAsynHandler {
    /**
     * 每批次查询的条目
     */
    private static final Integer LIMIT = 100;
    @Resource
    private VehModelDeviceMapper vehModelDeviceMapper;

    @Override
    public void processData(VehDataAsynTaskPo task) {
        List<VehModelDevicePo> pos = vehModelDeviceMapper.listByFromId(task.getQueryCursor(), LIMIT);
        if (CollectionUtils.isEmpty(pos)) {
            return;
        }

        while (CollectionUtils.isNotEmpty(pos)) {
            Long fromId = processDevice(task, pos);
            pos = vehModelDeviceMapper.listByFromId(fromId, LIMIT);
        }
    }
}
