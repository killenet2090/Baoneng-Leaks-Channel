package com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn;

import com.bnmotor.icv.tsp.device.model.entity.DevicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleDevicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @ClassName: VehicleDevice
 * @Description: 车辆零部件
 * @author: zhangwei2
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDevice {
    @JsonIgnore
    private String vin;
    /**
     * 老设备唯一标识符
     */
    private String oldDeviceId;
    /**
     * 新设备唯一标识符
     */
    private String deviceId;
    /**
     * 设备生产序列号
     */
    private String sn;
    /**
     * 零部件类型
     */
    private Integer componentType;
    /**
     * 零部件类型名称
     */
    private String componentTypeName;
    /**
     * 零件型号:同一类型的零件不同的型号
     */
    private String componentModel;
    /**
     * 零件名称
     */
    private String componentName;
    /**
     * 固件代码固件代码由OTA平台同终端提前约定
     */
    private String hardwareVersion;
    /**
     * 原始版本
     */
    private String softwareVersion;

    /**
     * 物联网卡
     */
    private String iccid;

    public static VehicleDevice transform(VehiclePo vehiclePo, DevicePo devicePo) {
        VehicleDevice vehicleDevice = new VehicleDevice();
        vehicleDevice.setVin(vehiclePo.getVin());
        vehicleDevice.setDeviceId(devicePo.getDeviceId());
        vehicleDevice.setComponentType(devicePo.getDeviceType());
        vehicleDevice.setComponentName(devicePo.getDeviceName());
        return vehicleDevice;
    }
}
