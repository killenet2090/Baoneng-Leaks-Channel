package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.mapper.VehDataAsynTaskMapper;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.response.device.CacheDeviceModelInfo;
import com.bnmotor.icv.tsp.device.service.IDeviceService;
import com.bnmotor.icv.tsp.device.service.mq.producer.KafkaSender;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: AbstractAsynHandler
 * @Description: 抽象处理器
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public abstract class AbstractAsynHandler implements IAsynHandler {
    @Resource
    private VehDataAsynTaskMapper taskMapper;
    @Resource
    private IDeviceService deviceService;
    @Resource
    protected KafkaSender mqSender;
    @Resource
    private VehLocalCache vehLocalCache;

    /**
     * 更新任务
     *
     * @param taskId 任务号
     * @param fromId 起始条目
     */
    private void updateTask(Long taskId, Long fromId) {
        VehDataAsynTaskPo task = new VehDataAsynTaskPo();
        task.setId(taskId);
        task.setUpdateBy(Constant.SYSTEM);
        task.setQueryCursor(fromId);
        task.setUpdateTime(LocalDateTime.now());
        taskMapper.updateById(task);
    }

    /**
     * 发送任务
     *
     * @param topic           队列
     * @param dataSysnMessage 消息
     */
    private void sentMsg(String topic, DataSysnMessage dataSysnMessage) {
        mqSender.sendDataAysnMsg(topic, dataSysnMessage);
    }

    /**
     * 处理公共车辆数据信息
     */
    public Long processVehicle(VehDataAsynTaskPo task, List<VehiclePo> data) {
        Long fromId = data.get(data.size() - 1).getId();

        List<String> vins = data.stream().map(VehiclePo::getVin).collect(Collectors.toList());
        List<VehicleDevice> vehDevices = deviceService.listVehDevices(vins);

        Map<String, List<VehicleDevice>> group = new HashMap<>();

        if (CollectionUtils.isNotEmpty(vehDevices)) {
            group = vehDevices.stream().peek(o -> {
                o.setComponentTypeName(vehLocalCache.getDeviceTypeName(o.getComponentType()));
                CacheDeviceModelInfo info = deviceService.getDeviceModelInfo(o.getComponentType(), o.getComponentModel());
                if (info != null) {
                    o.setComponentName(info.getChiName() != null ? info.getChiName() : info.getEngName());
                }
            }).collect(Collectors.groupingBy(VehicleDevice::getVin));
        }

        for (VehiclePo vehiclePo : data) {
            Vehicle vehicle = Vehicle.transform(vehiclePo);
            vehicle.setVehDevices(group.get(vehiclePo.getVin()));

            DataSysnMessage<Vehicle> dataSysnMessage = Vehicle.decorateMsg(ActionType.ADD.getType(), task.getBusinessType(), task.getDataType(), vehicle);
            sentMsg(task.getTopic(), dataSysnMessage);
        }

        // 更新游标
        updateTask(task.getId(), fromId);

        return fromId;
    }

    /**
     * 处理设备数据
     */
    public Long processDevice(VehDataAsynTaskPo task, List<VehModelDevicePo> data) {
        Long fromId = data.get(data.size() - 1).getId();
        for (VehModelDevicePo devicePo : data) {
            VehDeviceModel vehDeviceModel = VehDeviceModel.transform(devicePo, vehLocalCache);
            CacheDeviceModelInfo info = deviceService.getDeviceModelInfo(devicePo.getDeviceType(), devicePo.getDeviceModel());
            if (info != null) {
                vehDeviceModel.setComponentName(info.getChiName() != null ? info.getChiName() : info.getEngName());
            }

            DataSysnMessage<VehDeviceModel> message = VehDeviceModel.decorateMsg(task.getBusinessType(), task.getDataType(), vehDeviceModel);
            sentMsg(task.getTopic(), message);
        }

        updateTask(task.getId(), fromId);
        return fromId;
    }


    /**
     * 处理标签数据
     */
    public Long processTag(VehDataAsynTaskPo task, List<VehicleTagPo> data) {
        Long fromId = data.get(data.size() - 1).getId();
        for (VehicleTagPo tagPo : data) {
            VehTag vehTag = VehTag.transform(tagPo);
            DataSysnMessage<VehTag> dataSysnMessage = VehTag.decorateMsg(task.getBusinessType(), task.getDataType(), vehTag);
            sentMsg(task.getTopic(), dataSysnMessage);
        }

        // 更新游标
        updateTask(task.getId(), fromId);
        return fromId;
    }
}
