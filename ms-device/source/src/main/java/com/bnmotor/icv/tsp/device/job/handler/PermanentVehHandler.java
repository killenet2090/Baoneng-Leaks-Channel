package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.common.enums.dataaysn.MessageType;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;
import com.bnmotor.icv.tsp.device.job.dispatch.Handler;
import com.bnmotor.icv.tsp.device.job.dispatch.QueryType;
import com.bnmotor.icv.tsp.device.job.dispatch.TaskType;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import com.bnmotor.icv.tsp.device.model.request.feign.MqMessageDto;
import com.bnmotor.icv.tsp.device.service.IVehTagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: PermanentVehicleHandler
 * @Description: 永久性车辆数据同步处理器
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
@Handler(taskType = TaskType.PERMANENT, dataType = DataType.VEHICLE, queryType = QueryType.ALL)
public class PermanentVehHandler extends AbstractAsynHandler {
    /**
     * 每批次查询的条目
     */
    private static final Integer LIMIT = 100;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private IVehTagService tagService;

    @Override
    public void processData(VehDataAsynTaskPo task) {
        LocalDateTime time = LocalDateTime.now();
        time = time.minusMinutes(6);

        List<VehiclePo> vehicles = vehicleMapper.listByFromId(time, null, task.getQueryCursor(), LIMIT);
        if (CollectionUtils.isEmpty(vehicles)) {
            return;
        }

        while (CollectionUtils.isNotEmpty(vehicles)) {
            Long fromId = processVehicle(task, vehicles);
            for (VehiclePo vehiclePo : vehicles) {
                sendMessageToOperatingBackgroud(vehiclePo);
            }
            vehicles = vehicleMapper.listByFromId(time, null, fromId, LIMIT);
        }
    }

    /**
     * 发送消息到运营后台
     *
     * @param vehiclePo 车辆对象
     */
    private void sendMessageToOperatingBackgroud(VehiclePo vehiclePo) {
        List<VehicleTagPo> tagPos = tagService.listByVin(vehiclePo.getVin());
        List<Long> labelIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagPos)) {
            labelIds = tagPos.stream().map(VehicleTagPo::getTagId).collect(Collectors.toList());
        }

        MqMessageDto messageDto = new MqMessageDto();
        messageDto.setConfigId(vehiclePo.getConfigurationId());
        messageDto.setVin(vehiclePo.getVin());
        messageDto.setType(MessageType.INCREMENT.getType());
        messageDto.setTagIds(labelIds);
        mqSender.sendMqMsg(messageDto);
    }
}
