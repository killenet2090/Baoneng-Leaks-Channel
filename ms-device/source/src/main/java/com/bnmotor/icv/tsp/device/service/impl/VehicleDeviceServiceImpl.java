package com.bnmotor.icv.tsp.device.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.UpdateVehDeviceBindedDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceBindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceRebindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceUnbindDto;
import com.bnmotor.icv.tsp.device.model.response.device.CacheDeviceModelInfo;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceSimVo;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.ReplacementTimesVo;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehDeviceBindHistory;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehicleDeviceVo;
import com.bnmotor.icv.tsp.device.service.IDeviceService;
import com.bnmotor.icv.tsp.device.service.IVehicleDeviceService;
import com.bnmotor.icv.tsp.device.service.mq.producer.KafkaSender;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: VehicleDeviceServiceImpl
 * @Description: 车辆绑定零部件服务实现
 * @author: zhangwei2
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class VehicleDeviceServiceImpl implements IVehicleDeviceService {
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private VehicleDeviceMapper vehDeviceMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceReplacementMapper replacementMapper;
    @Resource
    private DeviceModelInfoMapper infoMapper;
    @Resource
    private IDeviceService deviceService;
    @Resource
    private KafkaSender kafkaSender;
    @Resource
    private VehLocalCache vehLocalCache;

    @Value("${adam.kafka.producer.topic.dataIncrementSync}")
    private String dataIncrementSync;

    @Override
    public List<VehicleDeviceVo> listDevice(String vin, Integer deviceType) {
        VehiclePo vehiclePo = vehicleMapper.selectByVin(vin);
        if (vehiclePo == null) {
            return Collections.EMPTY_LIST;
        }

        List<VehicleDevicePo> vehDevices = vehDeviceMapper.listByVinAndDeviceType(vin, deviceType);
        if (CollectionUtils.isEmpty(vehDevices)) {
            return Collections.EMPTY_LIST;
        }

        List<String> deviceIds = vehDevices.stream().map(VehicleDevicePo::getDeviceId).collect(Collectors.toList());
        List<DevicePo> devices = deviceMapper.listByDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(devices)) {
            return Collections.EMPTY_LIST;
        }

        List<ReplacementTimesVo> replacementPos = replacementMapper.countReplacementTimes(vin);
        Map<Integer, Integer> deviceTypeToTimes = replacementPos.stream().collect(Collectors.toMap(ReplacementTimesVo::getDeviceType, ReplacementTimesVo::getReplacementTimes));

        Map<String, DevicePo> deviceIdToDevice = devices.stream().collect(Collectors.toMap(DevicePo::getDeviceId, device -> device));
        return vehDevices.stream().map(vehDevice -> {
            VehicleDeviceVo deviceVo = new VehicleDeviceVo();
            deviceVo.setId(String.valueOf(vehDevice.getId()));
            deviceVo.setDeviceId(vehDevice.getDeviceId());
            deviceVo.setDeviceType(vehDevice.getDeviceType());
            deviceVo.setUpdateTime(vehDevice.getUpdateTime());
            deviceVo.setDeviceTypeName(vehLocalCache.getDeviceTypeName(vehDevice.getDeviceType()));
            deviceVo.setReplaceTimes(deviceTypeToTimes.get(vehDevice.getDeviceType()));
            DevicePo devicePo = deviceIdToDevice.get(vehDevice.getDeviceId());
            if (devicePo != null) {
                deviceVo.setProductSn(devicePo.getProductSn());
                deviceVo.setName(devicePo.getDeviceName());
                deviceVo.setHardVersion(devicePo.getHardwareVersion());
                deviceVo.setSoftwareVersion(devicePo.getSoftwareVersion());
                deviceVo.setDeviceModel(devicePo.getDeviceModel());
                deviceVo.setSupplierName(devicePo.getSupplierName());
            }
            return deviceVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DeviceSimVo> listDeviceSim(List<String> vins) {
        List<VehicleDevicePo> vehDevices = vehDeviceMapper.listByVins(vins);
        if (CollectionUtils.isEmpty(vehDevices)) {
            return Collections.emptyList();
        }

        Map<String, String> deviceIdToVin = vehDevices.stream().collect(Collectors.toMap(VehicleDevicePo::getDeviceId, VehicleDevicePo::getVin));
        List<String> deviceIds = vehDevices.stream().map(VehicleDevicePo::getDeviceId).collect(Collectors.toList());
        List<DevicePo> devices = deviceMapper.listByDeviceIds(deviceIds);
        return devices.stream().filter(o -> !StringUtils.isEmpty(o.getIccid())).map(o -> {
            DeviceSimVo simVo = new DeviceSimVo();
            simVo.setVin(deviceIdToVin.get(o.getDeviceId()));
            simVo.setDeviceId(o.getDeviceId());
            simVo.setDeviceType(o.getDeviceType());
            simVo.setIccid(o.getIccid());
            return simVo;
        }).collect(Collectors.toList());
    }

    @Override
    public DeviceSimVo getVehicleSim(String iccid) {
        DevicePo devicePo = deviceMapper.getByIccidId(iccid);
        if (devicePo == null) {
            return null;
        }

        VehicleDevicePo vehicleDevicePo = vehDeviceMapper.getByDeviceId(devicePo.getDeviceId());
        if (vehicleDevicePo == null) {
            return null;
        }

        DeviceSimVo simVo = new DeviceSimVo();
        simVo.setVin(vehicleDevicePo.getVin());
        simVo.setIccid(iccid);
        simVo.setDeviceId(vehicleDevicePo.getDeviceId());
        simVo.setDeviceType(vehicleDevicePo.getDeviceType());
        return simVo;
    }

    @Override
    public List<VehDeviceBindHistory> listBindHistory(String vin, Integer deviceType) {
        List<DeviceReplacementPo> replacementPos = replacementMapper.listReplacements(vin, deviceType);
        if (CollectionUtils.isEmpty(replacementPos)) {
            return Collections.emptyList();
        }

        List<String> deviceIds = replacementPos.stream().map(DeviceReplacementPo::getDeviceId).collect(Collectors.toList());
        List<DevicePo> devices = deviceMapper.listByDeviceIds(deviceIds);
        Map<String, DevicePo> deviceIdToDevice = devices.stream().collect(Collectors.toMap(DevicePo::getDeviceId, o -> o));

        List<Long> modelInfoIds = replacementPos.stream().map(DeviceReplacementPo::getDeviceModelInfoId).collect(Collectors.toList());
        List<DeviceModelInfoPo> modelInfos = infoMapper.selectBatchIds(modelInfoIds);
        Map<Long, DeviceModelInfoPo> modelInfoToModel = modelInfos.stream().collect(Collectors.toMap(DeviceModelInfoPo::getId, o -> o));

        return replacementPos.stream().map(o -> {
            VehDeviceBindHistory bindHistory = new VehDeviceBindHistory();
            BeanUtils.copyProperties(o, bindHistory);
            bindHistory.setReplaceTime(o.getCreateTime());
            DevicePo devicePo = deviceIdToDevice.get(o.getDeviceId());
            if (devicePo != null) {
                bindHistory.setDeviceName(devicePo.getDeviceName());
                bindHistory.setProductSn(devicePo.getProductSn());
            }

            DeviceModelInfoPo infoPo = modelInfoToModel.get(o.getDeviceModelInfoId());
            if (infoPo != null) {
                bindHistory.setDeviceModel(infoPo.getDeviceModel());
                bindHistory.setSupplierName(infoPo.getSupplierName());
            }
            return bindHistory;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void bind(VehDeviceBindDto bindDto) {
        // 1.校验
        // 1.1 校验车辆,零部件存在性
        VehiclePo vehiclePo = checkAndReturnVehicle(bindDto.getVin());

        // 1.2 校验车辆和零部件绑定关系一对一校验
        checkVehDeviceBinded(bindDto.getVin(), bindDto.getDeviceType());

        // 2.判读是否存在未绑定的设备
        DevicePo device = deviceMapper.getNotBindingBySn(bindDto.getProductSn());
        if (device == null) {
            log.warn("不存在可用设备,productSn:{0}", bindDto.getProductSn());
            throw new AdamException(BusinessExceptionEnums.NOT_EXIST_DEVICE_AVALIABLE);
        }

        // 3.更新车辆设备信息表
        VehicleDevicePo insertVehDevice = new VehicleDevicePo();
        insertVehDevice.setDeviceType(device.getDeviceType());
        insertVehDevice.setVin(bindDto.getVin());
        insertVehDevice.setDeviceId(device.getProductSn());
        insertVehDevice.setDeviceName(device.getDeviceName());
        insertVehDevice.setVersion(1);
        insertVehDevice.setDelFlag(0);
        insertVehDevice.setCreateBy(bindDto.getUpdateBy());
        insertVehDevice.setCreateTime(LocalDateTime.now());
        vehDeviceMapper.insert(insertVehDevice);

        // 4.发送更新零部件绑定关系消息
        sendVehDeviceBindMsg(null, vehiclePo, device);
    }

    /**
     * 发送车辆零部件绑定消息
     *
     * @param vehiclePo 车辆实体对象
     */
    private void sendVehDeviceBindMsg(String oldDeviceId, VehiclePo vehiclePo, DevicePo devicePo) {
        VehicleDevice vehicleDevice = VehicleDevice.transform(vehiclePo, devicePo);

        CacheDeviceModelInfo info = deviceService.getDeviceModelInfo(devicePo.getDeviceType(), devicePo.getDeviceModel());
        if (info != null) {
            vehicleDevice.setComponentName(info.getChiName() != null ? info.getChiName() : info.getEngName());
        }
        vehicleDevice.setComponentModel(devicePo.getDeviceModel());
        vehicleDevice.setSn(devicePo.getProductSn());
        vehicleDevice.setComponentType(devicePo.getDeviceType());
        vehicleDevice.setComponentTypeName(vehLocalCache.getDeviceTypeName(devicePo.getDeviceType()));
        vehicleDevice.setHardwareVersion(devicePo.getHardwareVersion());
        vehicleDevice.setSoftwareVersion(devicePo.getSoftwareVersion());
        vehicleDevice.setOldDeviceId(oldDeviceId);
        Vehicle vehicle = Vehicle.transform(vehiclePo);
        vehicle.setVehDevices(Collections.singletonList(vehicleDevice));
        DataSysnMessage<Vehicle> message = Vehicle.decorateMsg(ActionType.UPDATE.getType(), BusinessType.ALL.getType(), DataType.VEHICLE.getType(), vehicle);
        kafkaSender.sendDataAysnMsg(dataIncrementSync, message);
    }

    @Override
    @Transactional
    public void unbind(VehDeviceUnbindDto unbindDto) {
        // 1.校验车辆，设备绑定关系
        checkAndReturnVehicle(unbindDto.getVin());
        VehicleDevicePo vehDevice = checkAndReturnVehDeviceRebind(unbindDto.getVin(), unbindDto.getDeviceType());
        if (vehDevice == null) {
            return;
        }

        // 2.更新绑定关系
        vehDeviceMapper.delete(vehDevice.getId());

        DevicePo device = deviceMapper.getByDeviceId(vehDevice.getDeviceId());
        if (device == null) {
            throw new AdamException(RespCode.USER_INVALID_INPUT);
        }

        // 3.插入绑定历史表
        DeviceModelInfoPo infoPo = infoMapper.selectByDeviceTypeAndModel(unbindDto.getDeviceType(), device.getDeviceModel());
        if (infoPo == null) {
            log.warn("系统存在脏数据,不存在的零部件型号:{0}", device.getDeviceModel());
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_NOT_EXIST);
        }

        DeviceReplacementPo replacementPo = new DeviceReplacementPo();
        BeanUtils.copyProperties(vehDevice, replacementPo);
        replacementPo.setReplaceChannel(unbindDto.getReplaceChannel());
        replacementPo.setBindTime(vehDevice.getCreateTime());
        replacementPo.setReplaceReason(unbindDto.getReplaceReason());
        replacementPo.setHardwareVersion(device.getHardwareVersion());
        replacementPo.setSoftwareVersion(device.getSoftwareVersion());
        replacementPo.setCreateBy(unbindDto.getUpdateBy() != null ? unbindDto.getUpdateBy() : String.valueOf(ReqContext.getUid()));
        replacementPo.setCreateTime(LocalDateTime.now());
        if (infoPo != null) {
            replacementPo.setDeviceModelInfoId(infoPo.getId());
        }
        replacementMapper.insert(replacementPo);

        // 8.发送删除零部件绑定关系消息
        sendUnbindMsg(unbindDto.getVin(), Collections.singletonList(device.getDeviceId()));
    }

    /**
     * 发送解绑增量消息
     *
     * @param vin        车架号
     * @param produceSns 设备id集合
     */
    private void sendUnbindMsg(String vin, List<String> produceSns) {
        DataSysnMessage message = new DataSysnMessage();
        message.setAction(ActionType.DELETE.getType());
        message.setBusinessType(BusinessType.ALL.getType());
        message.setType(DataType.VEHICLE.getType());
        VehDeviceDelete deviceDelete = new VehDeviceDelete();
        deviceDelete.setVin(vin);
        deviceDelete.setDeviceIds(produceSns);
        message.setData(deviceDelete);
        kafkaSender.sendDataAysnMsg(dataIncrementSync, message);
    }

    /**
     * 车辆存在性校验，校验成功后返回车辆信息
     *
     * @param vin 车架号
     * @return 车架号
     */
    private VehiclePo checkAndReturnVehicle(String vin) {
        VehiclePo vehiclePo = vehicleMapper.selectByVin(vin);
        if (vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }

        return vehiclePo;
    }

    /**
     * 校验车辆零部件一对一关系
     *
     * @param vin        车架号
     * @param deviceType 设备类型
     */
    private void checkVehDeviceBinded(String vin, Integer deviceType) {
        //  校验车辆是否绑定同类型设备
        List<VehicleDevicePo> vehDevices = vehDeviceMapper.listByVinAndDeviceType(vin, deviceType);
        if (CollectionUtils.isNotEmpty(vehDevices) && vehDevices.get(0) != null) {
            log.warn("该车辆已经绑定同种类型设备vin:{0},deviceType:{1}", vin, deviceType);
            throw new AdamException(BusinessExceptionEnums.MODEL_DEVIVE_HAS_BINDED);
        }
    }

    /**
     * 校验车辆零部件换绑关系
     *
     * @param vin        车架号
     * @param deviceType 设备类型
     */
    private VehicleDevicePo checkAndReturnVehDeviceRebind(String vin, Integer deviceType) {
        // 2 校验车辆是否绑定同类型设备
        List<VehicleDevicePo> vehDevices = vehDeviceMapper.listByVinAndDeviceType(vin, deviceType);
        if (CollectionUtils.isEmpty(vehDevices)) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_NOT_BINDED);
        }
        if (vehDevices.size() != 1) {
            throw new AdamException(BusinessExceptionEnums.MODEL_DEVIVE_HAS_BINDED);
        }

        return vehDevices.get(0);
    }

    @Override
    public void rebind(VehDeviceRebindDto rebindDto) {
        // 1.校验车辆存在性
        VehiclePo vehiclePo = vehicleMapper.selectByVin(rebindDto.getVin());
        if (vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }

        // 2.校验零件型号信息
        DeviceModelInfoPo infoPo = infoMapper.selectByDeviceTypeAndModel(rebindDto.getDeviceType(), rebindDto.getDeviceModel());
        if (infoPo == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_NOT_EXIST);
        }

        // 3.校验车辆零部件一对一绑定
        VehicleDevicePo vehDevice = checkAndReturnVehDeviceRebind(rebindDto.getVin(), rebindDto.getDeviceType());
        DevicePo device = checkAndReturnDevice(vehDevice.getDeviceId());
        if (device.getProductSn().equals(rebindDto.getProductSn())) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_BINDED_SAME_DEVICE);
        }

        // 4.查询零部件是否有当前待绑定的零件，如果没有则进行插入
        DevicePo newDevice = deviceMapper.getNotBindingBySn(rebindDto.getProductSn());
        if (newDevice == null) {
            log.warn("不存在可用设备,productSn:{0}", rebindDto.getProductSn());
            throw new AdamException(BusinessExceptionEnums.NOT_EXIST_DEVICE_AVALIABLE);
        }
        if (!newDevice.getDeviceModel().equals(rebindDto.getDeviceModel())) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_RELATION_ERROR);
        }

        // 5.删除老的绑定
        vehDeviceMapper.deleteByVinAndType(rebindDto.getVin(), rebindDto.getDeviceType());

        // 6.插入车辆零部件绑定关系
        VehicleDevicePo newVehDevice = transformInsertVehDevice(rebindDto.getVin(), rebindDto.getDeviceType(), newDevice.getDeviceId(), device.getDeviceName());
        vehDeviceMapper.insert(newVehDevice);

        // 7.插入换件历史
        DevicePo oldDevice = deviceMapper.getByDeviceId(vehDevice.getDeviceId());
        if (oldDevice == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_NOT_EXIST);
        }
        DeviceModelInfoPo oldModelInfo = infoMapper.selectByDeviceTypeAndModel(oldDevice.getDeviceType(), oldDevice.getDeviceModel());
        if (oldModelInfo == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_NOT_EXIST);
        }
        DeviceReplacementPo replacementPo = new DeviceReplacementPo();
        BeanUtils.copyProperties(vehDevice, replacementPo);
        replacementPo.setBindTime(vehDevice.getCreateTime());
        if (oldDevice != null) {
            replacementPo.setHardwareVersion(oldDevice.getHardwareVersion());
            replacementPo.setSoftwareVersion(oldDevice.getSoftwareVersion());
        }
        replacementPo.setDeviceModelInfoId(oldModelInfo.getId());
        replacementPo.setCreateBy(String.valueOf(ReqContext.getUid()));
        replacementPo.setCreateTime(LocalDateTime.now());
        replacementPo.setDeviceModelInfoId(oldModelInfo.getId());
        replacementMapper.insert(replacementPo);

        // 8.发送更新零部件绑定关系消息
        sendVehDeviceBindMsg(vehDevice.getDeviceId(), vehiclePo, newDevice);
    }

    private VehicleDevicePo transformInsertVehDevice(String vin, Integer deviceType, String deviceId, String deviceName) {
        VehicleDevicePo vehicleDevicePo = new VehicleDevicePo();
        vehicleDevicePo.setDeviceType(deviceType);
        vehicleDevicePo.setVin(vin);
        vehicleDevicePo.setDeviceId(deviceId);
        vehicleDevicePo.setDeviceName(deviceName);
        vehicleDevicePo.setVersion(1);
        vehicleDevicePo.setDelFlag(0);
        vehicleDevicePo.setCreateBy(String.valueOf(ReqContext.getUid()));
        vehicleDevicePo.setCreateTime(LocalDateTime.now());
        return vehicleDevicePo;
    }

    private VehicleDevicePo transformUpdateVehDevice(Long id, String deviceId) {
        VehicleDevicePo vehicleDevicePo = new VehicleDevicePo();
        vehicleDevicePo.setId(id);
        vehicleDevicePo.setDeviceId(deviceId);
        vehicleDevicePo.setUpdateBy(String.valueOf(ReqContext.getUid()));
        vehicleDevicePo.setUpdateTime(LocalDateTime.now());
        return vehicleDevicePo;
    }

    private void transformUpdateDevice(DevicePo device, String deviceModel, String hardVersion, String sortwareVersion) {
        if (!StringUtils.isEmpty(deviceModel)) {
            device.setDeviceModel(deviceModel);
        }
        if (!StringUtils.isEmpty(hardVersion)) {
            device.setHardwareVersion(hardVersion);
        }
        if (!StringUtils.isEmpty(sortwareVersion)) {
            device.setSoftwareVersion(sortwareVersion);
        }
        device.setUpdateBy(String.valueOf(ReqContext.getUid()));
        device.setUpdateTime(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updateBinded(UpdateVehDeviceBindedDto updateBinded) {
        // 1.存在性校验
        VehicleDevicePo vehDevice = vehDeviceMapper.selectById(updateBinded.getId());
        if (vehDevice == null) {
            throw new AdamException(BusinessExceptionEnums.BINDED_NOT_EXIST);
        }

        // 2.校验零件型号信息
        DeviceModelInfoPo infoPo = infoMapper.selectByDeviceTypeAndModel(vehDevice.getDeviceType(), updateBinded.getDeviceModel());
        if (infoPo == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_NOT_EXIST);
        }

        // 3.校验数据老设备是否存在
        DevicePo device = checkAndReturnDevice(vehDevice.getDeviceId());

        // 4.更新设备信息
        if (!device.getProductSn().equals(updateBinded.getProductSn())) {
            device = deviceMapper.getNotBindingBySn(updateBinded.getProductSn());
            if (device == null) {
                log.warn("不存在可用设备,productSn:{0}", updateBinded.getProductSn());
                throw new AdamException(BusinessExceptionEnums.NOT_EXIST_DEVICE_AVALIABLE);
            }
        }
        if (!device.getDeviceModel().equals(updateBinded.getDeviceModel())) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_MODEL_RELATION_ERROR);
        }

        transformUpdateDevice(device, updateBinded.getDeviceModel(), updateBinded.getHardVersion(), updateBinded.getSoftwareVersion());
        deviceMapper.updateById(device);

        // 5.更新
        VehicleDevicePo updateVehDevice = transformUpdateVehDevice(updateBinded.getId(), device.getDeviceId());
        vehDeviceMapper.updateById(updateVehDevice);

        // 6.发送更新零部件绑定关系消息
        sendVehDeviceBindMsg(vehDevice.getDeviceId(), checkAndReturnVehicle(vehDevice.getVin()), device);
    }

    private DevicePo checkAndReturnDevice(String deviceId) {
        DevicePo device = deviceMapper.getByDeviceId(deviceId);
        if (device == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_NOT_EXIST);
        }
        return device;
    }
}
