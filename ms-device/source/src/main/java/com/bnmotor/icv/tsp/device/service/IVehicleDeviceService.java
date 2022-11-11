package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.model.request.vehDevice.UpdateVehDeviceBindedDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceBindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceRebindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceUnbindDto;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceSimVo;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehDeviceBindHistory;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehicleDeviceVo;

import java.util.List;

/**
 * @ClassName: IVehicleDeviceService
 * @Description: 车辆绑定零部件服务类
 * @author: zhangwei2
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehicleDeviceService {
    /**
     * 根据车辆vin码和设备类型查看该车辆绑定设备
     *
     * @param vin        车辆唯一标识
     * @param deviceType 车辆类型
     * @return 相关设备集合
     */
    List<VehicleDeviceVo> listDevice(String vin, Integer deviceType);

    /**
     * 根据车架号获取车辆绑定的sim卡
     *
     * @param vins 车架号集合
     * @return 车辆绑定的sim卡
     */
    List<DeviceSimVo> listDeviceSim(List<String> vins);

    /**
     * @param iccid 物联网卡号
     */
    DeviceSimVo getVehicleSim(String iccid);

    /**
     * 查询零部件换件历史记录
     *
     * @param vin        车架号
     * @param deviceType 设备类型
     * @return 换件历史记录
     */
    List<VehDeviceBindHistory> listBindHistory(String vin, Integer deviceType);

    /**
     * 车辆零部件绑定
     *
     * @param bindDto 绑定请求
     */
    void bind(VehDeviceBindDto bindDto);

    /**
     * 车辆零部件解绑
     *
     * @param unbindDto 解绑请求
     */
    void unbind(VehDeviceUnbindDto unbindDto);

    /**
     * 重新绑定
     *
     * @param rebindDto 重绑请求
     */
    void rebind(VehDeviceRebindDto rebindDto);

    /**
     * 更新已经绑定了的记录
     *
     * @param updateBinded 更新已经绑定的记录
     */
    void updateBinded(UpdateVehDeviceBindedDto updateBinded);
}
